package com.example.service1.service;

import com.example.service1.api.HiFeign;
import com.example.service1.dto.HiDTO;
import com.example.service1.mapper.HiMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.seata.spring.annotation.GlobalLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class HiService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HiFeign hiFeign;
    @Autowired
    private HiMapper hiMapper;

    //@HystrixCommand(fallbackMethod = "errInfo")
    //@GlobalLock
    //@Transactional(rollbackFor = Exception.class)
    public String hi(String name) {

        hiMapper.insertHi(name);
        //hiFeign.ha(name);
        return "成功";
        /*return hiFeign.ha(name);
        if(name.length()%2==0){
            //使用Ribbon
            return restTemplate.getForObject("http://service-2:8764/ha?name="+name+":Ribbon:", String.class);
        }else{
            //使用Feign
            return hiFeign.ha(name+":Feign:");
        }*/
    }

    //hystrix断路方法
    public String errInfo(String name){
        return name+"!sorry, error hystrix!";
    }
}
