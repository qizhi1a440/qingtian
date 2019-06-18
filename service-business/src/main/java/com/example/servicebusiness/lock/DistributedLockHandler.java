package com.example.servicebusiness.lock;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁类
 */
@Component
public class DistributedLockHandler {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockHandler.class);
    //private final static long LOCK_EXPIRE = 30 * 1000L;//单个业务持有锁的时间30s，防止死锁
    private final static long LOCK_EXPIRE = 1000L;//单个业务持有锁的时间30s，防止死锁 不能小于1s 否则过期时间无效
    private final static long LOCK_TRY_INTERVAL = 30L;//默认30ms尝试一次
    private final static long LOCK_TRY_TIMEOUT = 20 * 1000L;//默认尝试20s
    //守护线程执行延长超时任务
    private SurvivalClamProcessor survivalClamProcessor;
    //守护线程
    private Thread daemonThread;

    @Autowired
    private StringRedisTemplate template;


    /**
     * 尝试获取全局锁
     *
     * @param lock 锁的名称
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock) {
        return getLock(lock, LOCK_TRY_TIMEOUT, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁
     * @param lock 锁对象
     * @param timeout 获取锁的超时时间 单位ms
     * @return 获取结果 true false
     */
    public boolean tryLock(Lock lock,long timeout){
        return getLock(lock,timeout,LOCK_TRY_INTERVAL,LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁
     *
     * @param lock        锁对象
     * @param timeout     获取锁的超时时间 ms
     * @param tryInterval 多少毫秒尝试获取一次
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long timeout, long tryInterval) {
        return getLock(lock, timeout, tryInterval, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁
     *
     * @param lock           锁对象
     * @param timeout        获取锁的超时时间
     * @param tryInterval    多少毫秒尝试获取一次
     * @param lockExpireTime 锁的过期时间
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long timeout, long tryInterval, long lockExpireTime) {
        return getLock(lock, timeout, tryInterval, lockExpireTime);
    }


    /**
     * redis获取全局锁
     * @param lock 锁对象
     * @param timeout 获取锁的超时时间
     * @param lockTryInterval 多少ms尝试获取锁
     * @param lockExpire 获取成功后锁的过期时间
     * @return 获取结果 true成功 false失败
     */
    private boolean getLock(Lock lock, long timeout, long lockTryInterval, long lockExpire) {
        if(StringUtils.isEmpty(lock.getName())||StringUtils.isEmpty(lock.getValue())){
            return false;
        }
        try{
            long startTime=System.currentTimeMillis();
            //System.out.println(Thread.currentThread().getName()+"准备获取锁！");
            ValueOperations<String, String> ops = template.opsForValue();
            //循环获取锁
            while(true){
                //获取锁超时
                if (System.currentTimeMillis()-startTime>timeout){
                    logger.debug("线程:"+Thread.currentThread().getName()+",获取锁超时！");
                    //System.out.println(Thread.currentThread().getName()+"获取锁超时！");
                    return false;
                }
                //开始获取锁
                if(ops.setIfAbsent(lock.getName(), lock.getValue(),lockExpire,TimeUnit.MILLISECONDS)) {
                    //System.out.println(Thread.currentThread().getName()+"获取锁！");
                    //当获取锁成功时 创建一个守护线程 用于当业务时间过长 可能超出锁过期时间时 延长锁过期时间
                    /* 守护线程 */
                    survivalClamProcessor = new SurvivalClamProcessor(lock.getName(), lock.getValue(), lockExpire);
                    daemonThread = new Thread(survivalClamProcessor);
                    daemonThread.setDaemon(true);
                    daemonThread.start();

                    return true;
                }
                // 获取锁间隔
                Thread.sleep(lockTryInterval);
            }

        }catch (InterruptedException e){
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 释放锁
     * @param lock
     */
    public void releaseLock(Lock lock){
        //System.out.println(Thread.currentThread().getName()+"准备释放锁！");
        ValueOperations<String, String> ops = template.opsForValue();
        try {
            String curVal = ops.get(lock.getName());
            if (StringUtils.isNotEmpty(curVal) && curVal.equals(lock.getValue())) {
                //System.out.println(Thread.currentThread().getName()+"释放锁！");
                ops.getOperations().delete(lock.getName());
                //释放锁成功后 停止守护线程的工作  并且打断守护线程  以便线程及时被回收
                if(null!=survivalClamProcessor){
                    survivalClamProcessor.stop();
                }
                if(null!=daemonThread){
                    daemonThread.interrupt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
