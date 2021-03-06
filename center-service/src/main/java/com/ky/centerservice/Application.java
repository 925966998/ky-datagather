package com.ky.centerservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = {"com.ky.centerservice.mapper"})
@Configuration
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {

        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(Application.class, args);
//        SocketServerD server = new SocketServerD();
//        server.startSocketServer(8088);
    }

}

