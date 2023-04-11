package fr.real.supervision.appliinfo.web.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.unboundid.ldap.sdk.DereferencePolicy;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;

import fr.real.supervision.appliinfo.exception.NotAuthorizeException;
import fr.real.supervision.appliinfo.web.ldap.InformationConnectLdap;

public class DummyUserDetailsService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(DummyUserDetailsService.class);

	@Autowired
	private InformationConnectLdap informationConnectLdap;

	@Value("${ldap.baseDN}")
	String baseDN;

	@Value("${hai.admin}")
	String cnAdmin;

	@Value("${hai.exploitant}")
	String cnExploitant;

	@Value("${hai.communiquant}")
	String cnCommuniquant;

	private static final String NOT_USED = "notUsed";

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String passwordUser = AuthenticationFilter.getUser().getPassword();
		String bindDn = null;
		UserDetails user = null;
		Set<String> roles = new HashSet<>();
		try {
			LDAPConnection connection = informationConnectLdap.connect(username, passwordUser);
			LOG.debug("Deuxieme authentification LDAP reussie");
			Filter filter = Filter.createEqualityFilter("userPrincipalName", username);
			SearchRequest searchRequest = new SearchRequest(baseDN, SearchScope.SUB, DereferencePolicy.NEVER, 0, 0,
					false, filter);
			SearchResult searchResult = connection.search(searchRequest);

			if (!searchResult.getSearchEntries().isEmpty()) {
				bindDn = searchResult.getSearchEntries().get(0).getDN();
				Entry userEntry = connection.getEntry(bindDn);
				String[] memberValues = userEntry.getAttributeValues("memberOf");

				LOG.debug("Recuperation de  groupes");

				Set<String> admin = toList(cnAdmin);
				Set<String> exploitant = toList(cnExploitant);
				Set<String> communiquant = toList(cnCommuniquant);

				for (String arrElement : memberValues) {
					if (admin.contains(arrElement)) {
						roles.add(RoleConstants.ADMIN);
					} else if (exploitant.contains(arrElement)) {
						roles.add(RoleConstants.EXPLOITANT);
					} else if (communiquant.contains(arrElement)) {
						roles.add(RoleConstants.COMMUNIQUANT);
					}
				}
				user = new User(username, NOT_USED, true, true, true, true,
						AuthorityUtils.createAuthorityList(roles.stream().toArray(String[]::new)));
			} else {
				LOG.debug("Utilisateur non trouve dans l'AD");
			}

			if (roles.isEmpty()) {
				LOG.debug("Vous n'avez pas une autorisation d'acceder");
				throw new NotAuthorizeException(username);
			}
		} catch (LDAPSearchException e) {
			LOG.error("Erreur lors du recherche dans l'active directory", e);
			throw new UsernameNotFoundException(username);
		} catch (LDAPException e) {
			LOG.error("Mot de passe ou nom d'utilisateur incorrect");
			throw new UsernameNotFoundException(username);
		}

		return user;

	}

	public Set<String> toList(String dN) {
		String[] res = dN.split(";");
		return new HashSet<>(Arrays.asList(res));
	}
}
