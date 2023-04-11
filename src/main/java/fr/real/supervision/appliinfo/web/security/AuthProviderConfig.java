package fr.real.supervision.appliinfo.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthProviderConfig extends WebSecurityConfigurerAdapter {

	private Environment env;

	AuthProviderConfig(Environment env) {
		this.env = env;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// CSRF protection is enabled by default
		http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/css/**").permitAll()
				.antMatchers("/fonts/**").permitAll().antMatchers("/js/**").permitAll().antMatchers("/images/**")
				.permitAll().antMatchers("/favicon.ico").permitAll().antMatchers("/access-denied").permitAll()
				.antMatchers("/diagnostic").permitAll().antMatchers("/version").permitAll().antMatchers("/exploitation")
				.permitAll().antMatchers("/login").permitAll().antMatchers("/pdf/**").permitAll().antMatchers("/ping").permitAll().antMatchers("/applications/status").permitAll().antMatchers("/actuator/**").permitAll().antMatchers("/meteo/historique").permitAll()
				.antMatchers("/maintenances")
				.hasAnyAuthority(RoleConstants.ADMIN, RoleConstants.SUPER_ADMIN, RoleConstants.EXPLOITANT)
				.antMatchers("/communication")
				.hasAnyAuthority(RoleConstants.ADMIN, RoleConstants.SUPER_ADMIN, RoleConstants.EXPLOITANT)
				.antMatchers("/meteo")
				.permitAll().antMatchers("/information/status").permitAll()
				.antMatchers("/alarms").hasAuthority(RoleConstants.ADMIN).antMatchers("/alerts")
				.hasAuthority(RoleConstants.ADMIN).antMatchers("/categories").hasAuthority(RoleConstants.ADMIN)
				.antMatchers("/contacts").hasAuthority(RoleConstants.ADMIN).antMatchers("/holidays")
				.hasAuthority(RoleConstants.ADMIN).antMatchers("/profiles").hasAuthority(RoleConstants.ADMIN)
				.antMatchers("/diffusions").hasAuthority(RoleConstants.ADMIN).anyRequest().fullyAuthenticated().and()
				.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class).logout()
				.invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
				.logoutSuccessHandler(new CustomLogoutSuccessHandler()).permitAll().and().rememberMe().and()
				.exceptionHandling().accessDeniedPage("/access-denied").accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(http403ForbiddenEntryPoint())
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and().headers().frameOptions().sameOrigin();


	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new AuthAccessDeniedHandler();
	}

	@Bean
	public AuthenticationEntryPoint http403ForbiddenEntryPoint() {
		return new Http403ForbiddenEntryPoint();
	}

	public AuthenticationFilter authenticationFilter() throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(successHandler());
		filter.setAuthenticationFailureHandler(failureHandler());
		filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
		return filter;
	}

	public SimpleUrlAuthenticationFailureHandler failureHandler() {
		return new SimpleUrlAuthenticationFailureHandler("/login?error");
	}

	public SimpleUrlAuthenticationSuccessHandler successHandler() {
		return new SimpleUrlAuthenticationSuccessHandler("/");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(kerberosAuthenticationProvider());
	}

	@Bean
	public KerberosAuthenticationProvider kerberosAuthenticationProvider() {
		KerberosAuthenticationProvider provider = new KerberosAuthenticationProvider();
		SunJaasKerberosClient client = new SunJaasKerberosClient();
		client.setDebug(false); // TODO Remove debug mode
		provider.setKerberosClient(client);
		provider.setUserDetailsService(dummyUserDetailsService());
		return provider;
	}

	@Bean
	public UserDetailsService dummyUserDetailsService() {
		return env.acceptsProfiles(Profiles.of("dev")) ? new DummyDevUserDetailsService()
				: new DummyUserDetailsService();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
