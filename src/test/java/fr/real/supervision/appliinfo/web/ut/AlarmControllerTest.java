package fr.real.supervision.appliinfo.web.ut;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.real.supervision.appliinfo.model.Alarm;
import fr.real.supervision.appliinfo.web.alarms.controller.AlarmController;
import fr.real.supervision.appliinfo.web.alarms.service.AlarmService;

/**
 * In this test, the web layer of the Spring application is started, without the
 * server. We ask for just one {@link AlarmController}" to be instantiated.
 * Spring Boot is only instantiating the web layer, not the whole context.
 * 
 * @author tehdy.draoui
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AlarmController.class)
public class AlarmControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AlarmService alarmService;
	
	private Long alarmId = 0l;

	@Before
	public void setup() {
		// given
		
		// Initializes the JacksonTester
		JacksonTester.initFields(this, new ObjectMapper());
		
		List<Alarm> alarms = Arrays.asList(createAlarm(), createAlarm(), createAlarm());
		when(alarmService.getAlarms()).thenReturn(alarms);
	}
	
	private Alarm createAlarm() {
		Alarm alarm = new Alarm();
		alarm.setId(alarmId++);
		alarm.setName("test alarm "+alarm.getId());
		alarm.setDescription("test description"+alarm.getId());
		return alarm;
	}
	@Ignore("Problème de build")
	@WithMockUser
	@Test
	public void givenExistingAlarms_WhenGetAlarms_ThenReturnExistingAlarms() throws Exception {
		// when
		this.mockMvc
			.perform(get("/alarms"))
			
		// then
		  .andExpect(model().attribute("alarms", alarmService.getAlarms()))
		  .andExpect(view().name("alarms/alarms"))
		  .andExpect(status().isOk())
		  .andReturn();
	}
	@Ignore("Problème de build")
	@WithAnonymousUser
	@Test
	public void givenUnauthenticated_WhenRequestAlarms_ThenReturnAccessError() throws Exception {
		// when
		this.mockMvc
			.perform(get("/alarms"))
			
		// then
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/access-denied"))
			.andReturn();
	}
}
