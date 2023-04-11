package fr.real.supervision.appliinfo.connector.sms;

import javax.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("sms")
@Validated
public class SmsProperties {

	@Pattern(regexp = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
	private String url;

	private String user;

	private String password;

	private String apiUrl;

	private String apiToken;

	private boolean proxyEnabled;

	private String proxyHost;

	private Integer proxyPort;

	private String opsGenieUrl;

	private String opsGenieToken;

	public String getUrl() {
		return url.endsWith("/") ? url : url + "/";
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public boolean isProxyEnabled() {
		return proxyEnabled;
	}

	public void setProxyEnabled(boolean proxyEnabled) {
		this.proxyEnabled = proxyEnabled;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getOpsGenieUrl() {
		return opsGenieUrl;
	}

	public void setOpsGenieUrl(String opsGenieUrl) {
		this.opsGenieUrl = opsGenieUrl;
	}

	public String getOpsGenieToken() {
		return opsGenieToken;
	}

	public void setOpsGenieToken(String opsGenieToken) {
		this.opsGenieToken = opsGenieToken;
	}
}
