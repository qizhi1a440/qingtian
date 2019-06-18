package com.example.servicebusiness.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-1")
public interface HiService {
    @GetMapping("/hi")
    String add(@RequestParam("name") String name);
}
