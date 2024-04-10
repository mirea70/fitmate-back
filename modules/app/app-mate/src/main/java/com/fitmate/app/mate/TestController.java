package com.fitmate.app.mate;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/mate/check")
    @Operation(hidden = true)
    public String test() {
        return "check";
    }
}
