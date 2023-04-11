package fr.real.supervision.appliinfo.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLoginSuccessListener.class);

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        UserDetails ud = (UserDetails) authenticationSuccessEvent.getAuthentication().getPrincipal();
        LOGGER.info("Connexion du user {} réussie", ud.getUsername());
    }
}
