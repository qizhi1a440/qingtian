package com.example.servicebusiness.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LuaUtils {

    private static StringRedisTemplate template;

    @Autowired
    public void setTemplate(StringRedisTemplate template) {
        LuaUtils.template = template;
    }

    public static Object expandLockTime(String key,String value,long lockTime){
        RedisScript redisScript = RedisScript.of("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire', KEYS[1],ARGV[2]) else return 0 end",Long.class);

        List<String> list = new ArrayList<String>();
        list.add(key);
        Object result = template.execute(redisScript,new StringRedisSerializer(),new StringRedisSerializer(),list,value,lockTime/1000+"");
        System.out.println("延长业务线程持有锁时间结果:"+result);
        return result;
    }

}
