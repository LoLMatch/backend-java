package com.lolmatch.calendar.config;

import com.lolmatch.calendar.model.notification.StompPrincipal;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.List;

@Slf4j
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    private JwtDecoder jwtDecoder;

    @PostConstruct
    public void init() {
        jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        log.info("preSend");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer" + " ")) {
                throw new AccessDeniedException("Bad auth header: " + authHeader);
            }
            String token = authHeader.substring("Bearer".length() + 1);
            log.info("Websocket token: " + token);
            Jwt jwt;
            JwtAuthenticationToken authenticationToken;
            try {
                jwt = jwtDecoder.decode(token);
                String uuid = jwt.getSubject();
                List<SimpleGrantedAuthority> authorities = Collections.emptyList();

                authenticationToken = new JwtAuthenticationToken(jwt, authorities);
                authenticationToken.setAuthenticated(true);

                accessor.setUser(new StompPrincipal(uuid));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (JwtException e) {
                throw new AccessDeniedException("Invalid JWT token");
            }
        }
        return message;
    }
}
