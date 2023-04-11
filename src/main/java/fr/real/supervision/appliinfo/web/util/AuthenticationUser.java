package fr.real.supervision.appliinfo.web.util;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUser {

	private AuthenticationUser() {
	}

	/**
	 * Récupère l'utilisateur connecté
	 * 
	 * @return retourne l'utilisateur connecté
	 */
	public static String getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return Pattern.compile("@").splitAsStream(auth.getName()).collect(Collectors.toList()).get(0);
	}
}
