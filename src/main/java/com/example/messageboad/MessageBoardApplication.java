package com.example.messageboad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MessageBoardApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // WAR展開時に呼び出される
        return application.sources(MessageBoardApplication.class);
    }

    public static void main(String[] args) {
        // ローカル起動時はこちらが使われる
        SpringApplication.run(MessageBoardApplication.class, args);
    }
}
