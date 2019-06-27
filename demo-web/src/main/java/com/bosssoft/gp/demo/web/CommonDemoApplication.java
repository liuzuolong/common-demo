package com.bosssoft.gp.demo.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author smile7up
 * @createDate 2019-06-27
 * @Description 启动类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.bosssoft.gp.demo.dao.api")
public class CommonDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonDemoApplication.class,args);
    }
}
