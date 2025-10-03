package com.iconset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class IconSetGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(IconSetGeneratorApplication.class, args);
    }
}
