package me.yeop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathChecker {

    @GetMapping("/health")
    public String healthCheck() {
        return "Healthy!";
    }
}
