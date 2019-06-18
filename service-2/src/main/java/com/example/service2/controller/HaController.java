package com.example.service2.controller;

import com.example.service2.service.HaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HaController {

    @Value("${server.port}")
    String port;
    @Autowired
    private HaService haService;

    @RequestMapping("/ha")
    public String ha(@RequestParam("name") String name) {
        //插入数据
        haService.add(name);
        return "Hi, my name is " + name + ", port: " + port;
    }



}
