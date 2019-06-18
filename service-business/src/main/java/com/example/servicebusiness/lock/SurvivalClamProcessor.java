package com.example.servicebusiness.lock;

public class SurvivalClamProcessor implements Runnable {

    private String key;
    private String value;
    private long lockTime;

    public volatile  Boolean signal=Boolean.TRUE;

    public SurvivalClamProcessor(String key, String value, long lockTime) {
        this.key = key;
        this.value = value;
        this.lockTime = lockTime;
    }
    public void stop(){
        this.signal=Boolean.FALSE;
    }

    @Override
    public void run() {
        long waitTime=lockTime*2/3;//设置当业务时间达到超时时间三分之二时 重新加超时时间
        while (signal){
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //减少执行无用的执行脚本
            if (signal){
                Object result = LuaUtils.expandLockTime(key, value, lockTime);
            }
        }
    }
}
