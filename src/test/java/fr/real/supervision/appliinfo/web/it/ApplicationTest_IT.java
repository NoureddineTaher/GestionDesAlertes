package fr.real.supervision.appliinfo.web.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import fr.real.supervision.appliinfo.web.HomeController;

/**
 * In this test, the full Spring application context is started, but without the
 * server. That way, almost the full stack is used, and our code will be called
 * exactly the same way as if it was processing a real HTTP request, but without
 * the cost of starting the server.
 * 
 * @author tehdy.draoui
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest_IT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void whenRootThenShowHomePage() throws Exception {
		// when
		this.mockMvc
			.perform(get("/"))
			.andDo(print())
		
		// then
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(HomeController.HOME_TITLE)))
			.andReturn();
	}
}
