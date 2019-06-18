package com.example.servicebusiness.controller;

import com.example.servicebusiness.lock.DistributedLockHandler;
import com.example.servicebusiness.lock.Lock;
import com.example.servicebusiness.lock.SurvivalClamProcessor;
import com.example.servicebusiness.service.TestDistributedService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class BController {

    //@Autowired
    private RedissonClient redissonClient;

    @RequestMapping("/redissonLock")
    public String testRedissonLock() throws InterruptedException {

        Lock lock = new Lock("name", Thread.currentThread().getName());
        RLock rLock = redissonClient.getLock(lock.getName());
        rLock.lock(20, TimeUnit.SECONDS);
        try{
            for (int i=0;i<10;i++){
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(100);
            }
        } catch(Exception e){

        }finally{
            rLock.unlock();
        }
        return Thread.currentThread().getName();
    }

    @Autowired
    private DistributedLockHandler distributedLockHandler;

    @RequestMapping("/lock")
    public String testLock() throws InterruptedException {
        //lock 的value为请求的唯一的标识 例如uuid  name为此段锁业务的标识 也应是唯一的 可根据业务或代码取名
        Lock lock = new Lock("name", Thread.currentThread().getName());
        if (distributedLockHandler.tryLock(lock)){
            try{
                for (int i=0;i<10;i++){
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(120);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                distributedLockHandler.releaseLock(lock);
            }
        }
        return Thread.currentThread().getName();
    }

    @Autowired
    private TestDistributedService testDistributedService;



    @RequestMapping("/test")
    public String test(@RequestParam String name){
        return testDistributedService.test(name);
    }

}


