package com.example.service2.service;

import com.example.service2.mapper.HaMapper;
import io.seata.spring.annotation.GlobalLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HaService {

    //@GlobalLock
    //@Transactional(rollbackFor = Exception.class)
    public void add(String name) {

        haMapper.insertHa(name);

    }

    @Autowired
    private HaMapper haMapper;
}
