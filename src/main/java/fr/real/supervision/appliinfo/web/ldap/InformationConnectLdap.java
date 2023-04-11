package fr.real.supervision.appliinfo.web.ldap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;

@Service
public class InformationConnectLdap {

	private static final Logger LOGGER = LoggerFactory.getLogger(InformationConnectLdap.class);

	@Value("${ldap.server}")
	String ldapHost;
	@Value("${ldap.port}")
	int ldapPort;

	public LDAPConnection connect(String login, String password) throws LDAPException {
		LDAPConnection connection = new LDAPConnection();
		try {
			connection.connect(ldapHost, ldapPort);

			if (StringUtils.isNotBlank(login) && StringUtils.isNotBlank(password)) {
				connection.bind(login, password);
			}
		} catch (LDAPException lee) {
			LOGGER.error(lee.getMessage() != null ? lee.getMessage() : "Unknow Exception");
		}
		return connection;
	}
}
