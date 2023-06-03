package com.fitmate.system.security.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private final String AUTHORITIES_KEY = "auth";

}
