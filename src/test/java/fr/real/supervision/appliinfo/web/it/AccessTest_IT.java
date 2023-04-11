package fr.real.supervision.appliinfo.web.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessTest_IT {
  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Before
  public void setup() {
	  // given
	  this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .alwaysDo(print())
            .build();
  }

  @Test
  public void givenLoginShouldBeAvailableForAll() throws Exception {
	  // when
	  this.mockMvc
            .perform(get("/login"))
      
      // then
            .andExpect(status().isOk());
  }

  @Test
  public void whenLoginWithInvalidCredentialsThenRedirectedToLoginWithError() throws Exception {
	  	String loginErrorUrl = "/login?error";
	  	
	  	// when
    	this.mockMvc
            .perform(formLogin().password("invalid"))
            
        //then
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(loginErrorUrl))
            .andExpect(unauthenticated());
  }
  
  @Test
  public void whenLoginWithInvalidCredentialsThenDisplayErrorMessage() throws Exception {
	  	// when
	  	this.mockMvc
            .perform(get("/login?error"))
            
        //then
            .andExpect(content().string(containsString("Nom d'utilisateur ou mot de passe invalide")));
  }
}