package fr.real.supervision.appliinfo.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginFailedListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLoginFailedListener.class);

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        Object username = event.getAuthentication().getPrincipal();

        LOGGER.info("Connexion du user {} échouée : {}", username, event.getException().getMessage());

    }

}
