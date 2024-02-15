package com.example.blog.web.common.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventListeners {
    @EventListener
    public void handleAuthenticationEvent(AbstractAuthenticationEvent event) {
        log.info("handleAuthenticationEvent " + event);
    }

    @EventListener
    public void handleBadCredentials(AuthenticationFailureBadCredentialsEvent event) {
        log.info("handleBadCredentials");
    }

    @EventListener
    public void handleAuthencaitonSuccess(AuthenticationSuccessEvent event) {
        log.info("handleAuthencaitonSuccess");
    }
}
