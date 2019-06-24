package com.example.txmanager1;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableTransactionManagerServer
@EnableEurekaClient
public class TxManager1Application {

    public static void main(String[] args) {
        SpringApplication.run(TxManager1Application.class, args);
    }

}
