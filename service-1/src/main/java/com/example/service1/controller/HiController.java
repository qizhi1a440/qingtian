package com.example.service1.controller;

import com.example.service1.service.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class HiController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private HiService hiService;

    @RequestMapping("/hi")
    public String hi(@RequestParam String name){
        return hiService.hi(name);
    }

    @RequestMapping("/port")
    public String port(){
        return port;
    }
}
