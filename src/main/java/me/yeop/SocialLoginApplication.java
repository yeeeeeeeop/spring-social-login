package me.yeop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
@EnableJpaAuditing
public class SocialLoginApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialLoginApplication.class, args);
    }
}
