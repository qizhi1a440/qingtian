package com.example.service1.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@FeignClient(value = "service-2")
public interface HiFeign {
    @GetMapping("/ha")
    String ha(@RequestParam("name") String name);

}
