package me.yeop.apple;

import lombok.extern.slf4j.Slf4j;
import me.yeop.domain.apple.AppleIdTokenPayload;
import me.yeop.domain.apple.GetMemberInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
public class AppleAuthClientTest {

    @Autowired
    private GetMemberInfoService getMemberInfoService;

    @Test
    void getToken() {
        String authorizationCode = "c89d558139f714c03a6d15bc511165e56.0.suvq.G5fZX3VqLrTDsXLWSmXB9w";

        AppleIdTokenPayload payload = getMemberInfoService.get(authorizationCode);
        log.info("payload: " + payload.toString());
    }
}
