package com.fitmate.adapter.in.web.security.filter;

import com.fitmate.adapter.in.web.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String rawToken = accessor.getFirstNativeHeader("Authorization");
            String token = tokenProvider.resolveToken(rawToken);

            if (token != null && tokenProvider.validateToken(token, "access") == TokenProvider.JwtCode.ACCESS) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                accessor.setUser(authentication);
            }
        }

        return message;
    }
}
