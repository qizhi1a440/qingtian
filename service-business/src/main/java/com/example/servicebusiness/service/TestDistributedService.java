package com.example.servicebusiness.service;


import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDistributedService {

    @Autowired
    private HiService hiService;

    @Autowired
    private HaService haService;

    @GlobalTransactional()
    public String test(String name) {
        String ha=null;
        boolean flag=false;
        try {
            ha = haService.ha(name);
        }catch (Exception e){
            System.out.println("haService异常了");
            flag=true;
            //throw new RuntimeException("haService异常了");
        }
        System.out.println("hahahahahahahahahahahahahahahaha");
        try {
            String hi = hiService.add(name);
        }catch (Exception e){
            System.out.println("hiService异常了");
            flag=true;
            //throw new RuntimeException("hiService异常了");
        }
        System.out.println("hihihihihihihihihihihihihiihihi");
        if (flag){
            throw new RuntimeException("插入数据异常了");
        }
        return ha;
    }
}
