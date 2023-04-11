package fr.real.supervision.appliinfo.web.ut;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import fr.real.supervision.appliinfo.web.security.AccessController;

/**
 * In this test, the web layer of the Spring application is started, without the
 * server. We ask for just one {@link AccessController}" to be instantiated.
 * Spring Boot is only instantiating the web layer, not the whole context.
 * To test mock kerberos authentification, we use a custom context configuration
 * with in memory authentification. This is only interesting for unit testing.
 * 
 * @author tehdy.draoui
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AccessController.class)
@ContextConfiguration(classes = WebSecurityConfigurer.class)
public class AccesControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void givenUnauthenticated_whenRequestLogin_ThenReturnLoginForm() throws Exception {
		// when
		this.mockMvc
			.perform(get("/login"))
			
		// then
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("submit")))
			.andReturn();
	}
	
	@Test
	public void givenUnauthenticated_whenFormLoginWithValidCredentials_ThenUserIsAuthenticated() throws Exception {
	  // when
	  this.mockMvc
            .perform(formLogin().user("username").password("password"))
            
      // then
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/"))
            .andExpect(authenticated().withUsername("username"))
            .andReturn();
	}
	
	@Test
	public void givenAuthenticated_WhenLogout_ThenRedirectToLoginWithMessage() throws Exception {
	  // when
	  this.mockMvc
	  		.perform(formLogin().user("username").password("password"))
	  		.andExpect(authenticated().withUsername("username"));
	  this.mockMvc.perform(logout())
            
      // then
            .andExpect(status().isFound())
            .andExpect(status().is3xxRedirection())
            .andExpect(unauthenticated())
            .andExpect(redirectedUrl("/login?logout"))
            .andReturn();
	}
	
	@Test
	public void givenUnauthenticated_WhenFormLoginWithInvalidCredentials_ThenFailWith401() throws Exception {
		// when
		this.mockMvc
			.perform(formLogin("/login").user("random").password("random"))
			
		// then
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?error"))
			.andExpect(unauthenticated())
			.andReturn();
	}
}