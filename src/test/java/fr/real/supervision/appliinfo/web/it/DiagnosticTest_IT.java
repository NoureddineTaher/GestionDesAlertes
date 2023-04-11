package fr.real.supervision.appliinfo.web.it;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiagnosticTest_IT {
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
    private FilterChainProxy springSecurityFilterChain;
	
	private MockMvc mockMvc;

	 @Before
    public void setup() {
		 // given
		 this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .alwaysDo(print())
            .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
            .build();
    }
	
	@Test
	public void dignosticTest() throws Exception {
		// when
		this.mockMvc
			.perform(get("/diagnostic").with(anonymous()))
		
		// then
			.andExpect(status().is2xxSuccessful())
			.andReturn();
	}
	
}


