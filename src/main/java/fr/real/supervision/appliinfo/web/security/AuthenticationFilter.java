package fr.real.supervision.appliinfo.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    public static final User user = new User() ;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
        throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Méthode d'authentication non supportee: "
              + request.getMethod());
        }
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        if(StringUtils.isNotBlank(password)){
            user.setPassword(password);
        }
        if(StringUtils.isBlank(username))
        	throw new UsernameNotFoundException(username);

        LOGGER.info("Connexion utilisateur.");
        
        return super.attemptAuthentication(request, response);
    }

    public static User getUser(){
         return user;
    }

}
