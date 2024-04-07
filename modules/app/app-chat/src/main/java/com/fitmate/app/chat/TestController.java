package com.fitmate.app.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/chat/api/check")
    public String test() {
        return "check";
    }
}
