package com.javarush.reviewplatform;

import org.springframework.boot.SpringApplication;

public class TestReviewPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.from(ReviewPlatformApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
