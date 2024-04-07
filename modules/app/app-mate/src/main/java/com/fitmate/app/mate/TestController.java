package com.fitmate.app.mate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/mate/api/check")
    public String test() {
        return "check";
    }
}
