package me.yeop.global.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt") // 프로퍼티값을 가져와서 사용하는 애너테이션
public class JwtProperties {

    private String issuer;

    private String secretKey;

}
