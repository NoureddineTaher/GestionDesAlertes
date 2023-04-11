package fr.real.supervision.appliinfo.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Custom {@link AuthenticationEntryPoint}, which is invoked when an unauthenticated 
 * user attempts to access a protected resource
 * 
 * @author tehdy.draoui
 *
 */
public class Http403ForbiddenEntryPoint implements AuthenticationEntryPoint {
	
	private static Logger log = LoggerFactory.getLogger(Http403ForbiddenEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
    	
    	// TODO Log anonymous client IP for unauthorized access        
    	 log.info("Un utilisateur anonyme a tente d'acceder a une ressource protegee: {}",
                 request.getRequestURI());
         
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.sendRedirect(request.getContextPath() + "/access-denied");
    }

}