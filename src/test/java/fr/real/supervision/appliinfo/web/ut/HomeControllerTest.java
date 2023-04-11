package fr.real.supervision.appliinfo.web.ut;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import fr.real.supervision.appliinfo.web.HomeController;

/**
 * In this test, the web layer of the Spring application is started, without the
 * server. We ask for just one {@link HomeController}" to be instantiated.
 * Spring Boot is only instantiating the web layer, not the whole context.
 * 
 * @author tehdy.draoui
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Ignore("Problème de build")
	@Test
	public void givenUnauthenticated_WhenRequestHome_ThenDisplayHomeTitleAndLoginButton() throws Exception {
		// when
		this.mockMvc
			.perform(get("/"))
			
		// then
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(HomeController.HOME_TITLE)))
			.andExpect(content().string(containsString("Veuillez vous connecter")))
			.andReturn();
	}
	@Ignore("Problème de build")
	@WithMockUser
	@Test
	public void givenAuthenticated_WhenRequestHome_ThenDisplayHomeTitleButNoLoginButton() throws Exception {
		// when
		this.mockMvc
			.perform(get("/"))
			
		// then
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(HomeController.HOME_TITLE)))
			.andExpect(content().string(not(containsString("Veuillez vous connecter"))))
			.andReturn();
	}
}
