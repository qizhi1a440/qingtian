package com.example.service2.service;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.example.service2.mapper.HaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HaService {

    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional
    public void add(String name) {

        haMapper.insertHa(name);

    }

    @Autowired
    private HaMapper haMapper;
}
