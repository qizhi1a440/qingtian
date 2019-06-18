package com.example.servicebusiness.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-2")
public interface HaService {
    @GetMapping("/ha")
    String ha(@RequestParam("name") String name);
}
