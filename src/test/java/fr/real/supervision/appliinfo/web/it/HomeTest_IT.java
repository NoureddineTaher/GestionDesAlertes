package fr.real.supervision.appliinfo.web.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.real.supervision.appliinfo.web.HomeController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeTest_IT {
  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Before
  public void setup() {
	  // given
	  this.mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .alwaysDo(print())
        .build();
  }

  @Test
  public void givenNoAccessWhenHomeThenShowHomePage() throws Exception {
	  // when
	  this.mockMvc
        .perform(get("/"))
       
      // then
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(HomeController.HOME_TITLE)))
        .andReturn();
  }
}