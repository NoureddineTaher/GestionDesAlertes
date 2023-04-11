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

import fr.real.supervision.appliinfo.model.Contact;
import fr.real.supervision.appliinfo.web.contacts.controller.ContactController;
import fr.real.supervision.appliinfo.web.contacts.service.ContactService;

/**
 * In this test, the web layer of the Spring application is started, without the
 * server. We ask for just one {@link ContactController}" to be instantiated.
 * Spring Boot is only instantiating the web layer, not the whole context.
 * 
 * @author tehdy.draoui
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ContactController.class)
public class ContactControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ContactService contactService;
	
	private Long contactId = 0l;

	@Before
	public void setup() {
		// given
		
		// Initializes the JacksonTester
		JacksonTester.initFields(this, new ObjectMapper());
		
		List<Contact> contacts = Arrays.asList(createContact(), createContact(), createContact());
		when(contactService.getContacts()).thenReturn(contacts);
	}
	
	private Contact createContact() {
		Contact contact = new Contact();
		contact.setId(contactId++);
		contact.setName("test name "+contact.getId());
		contact.setFirstname("test firtname "+contact.getId());
		return contact;
	}
	@Ignore("Problème de build")
	@WithMockUser
	@Test
	public void givenExistingContacts_WhenGetAlarms_ThenReturnExistingContacts() throws Exception {
		// when
		this.mockMvc
			.perform(get("/contacts"))
			
		// then
		  .andExpect(model().attribute("contacts", contactService.getContacts()))
		  .andExpect(view().name("contacts/contacts"))
		  .andExpect(status().isOk())
		  .andReturn();
	}
	@Ignore("Problème de build")
	@WithAnonymousUser
	@Test
	public void givenUnauthenticated_WhenRequestContacts_ThenReturnAccessError() throws Exception {
		// when
		this.mockMvc
			.perform(get("/contacts"))
			
		// then
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/access-denied"))
			.andReturn();
	}
}
