package com.xuanji;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.xuanji.mapper")
public class XuanjiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(XuanjiApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(XuanjiApplication.class);
    }

}
