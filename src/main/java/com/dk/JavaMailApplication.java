package com.dk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @program: springboot_javamail
 * @description: TODO
 * @author: Mr.XYH
 * @create: 2020-12-08 17:54
 **/
@SpringBootApplication
public class JavaMailApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaMailApplication.class,args);
    }
}
