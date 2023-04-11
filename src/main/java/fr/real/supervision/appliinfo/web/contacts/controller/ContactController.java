package fr.real.supervision.appliinfo.web.contacts.controller;

import java.util.Set;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.real.supervision.appliinfo.model.Contact;
import fr.real.supervision.appliinfo.model.DiffusionGroup;
import fr.real.supervision.appliinfo.web.contacts.dto.ContactDto;
import fr.real.supervision.appliinfo.web.contacts.service.ContactService;

@Controller
public class ContactController {

	private static final String VIEW_UPDATE_CONTACT = "contacts/update_contact";

	private static final String VIEW_ADD_CONTACT = "contacts/add_contact";

	private static final String VIEW_CONTACTS = "contacts/contacts";

	private static final String REDIRECT_CONTACTS = "redirect:/contacts";

	private static final String ATTRIBUTE_CONTACTS = "contacts";

	private static final String ATTRIBUTE_CONTACT = "contactDto";

	private static final String VERSION = "version";

	@Value("${info.app.version}")
	String applicationVersion;

	@Autowired
	private ContactService contactService;

	@GetMapping("/contacts")
	public ModelAndView getContacts(Model model) {
		ModelAndView modelAndView =  new ModelAndView(VIEW_CONTACTS);
		modelAndView.addObject(ATTRIBUTE_CONTACTS, contactService.getContacts());
		modelAndView.addObject(VERSION, applicationVersion );
		return modelAndView;
	}

	@GetMapping("/contacts/addcontact")
	public ModelAndView addContact(ContactDto contactDto) {
		return new ModelAndView(VIEW_ADD_CONTACT).addObject(VERSION, applicationVersion);
	}

	@PostMapping("/contacts/addcontact")
	public ModelAndView addContact(@Valid ContactDto contact, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return new ModelAndView(VIEW_ADD_CONTACT);
		}
		contactService.saveContact(contact);
		return new ModelAndView(REDIRECT_CONTACTS);
	}

	@GetMapping("/contacts/edit/{id}")
	public ModelAndView updateContact(@PathVariable("id") long id, Model model, RedirectAttributes redir) {
		ModelAndView modelAndView = new ModelAndView();
		ContactDto contactDto = contactService.getContactDtoById(id);
		if (contactDto != null) {
			modelAndView.setViewName(VIEW_UPDATE_CONTACT);
			model.addAttribute(ATTRIBUTE_CONTACT, contactDto);
		} else {
			modelAndView.setViewName(REDIRECT_CONTACTS);
			populateReturnError(id, redir);
		}
		modelAndView.addObject(VERSION, applicationVersion );
		return modelAndView;
	}

	@PostMapping("/contacts/edit/{id}")
	public ModelAndView updateContact(@PathVariable("id") long id, @Valid ContactDto contactDto, BindingResult result,
			Model model, RedirectAttributes redir) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_CONTACTS);
		if (contactService.getContactById(id) == null) {
			populateReturnError(id, redir);
			return modelAndView;
		}
		if (result.hasErrors()) {
			return new ModelAndView(VIEW_UPDATE_CONTACT);
		}
		contactService.updateContact(contactDto);
		return modelAndView;
	}

	@GetMapping("/contacts/delete/{id}")
	public ModelAndView deleteContact(@PathVariable("id") long id, Model model, RedirectAttributes redir) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_CONTACTS);
		Contact contact = contactService.getContactById(id);
		if (contact == null) {
			populateReturnError(id, redir);
		} else {
			Set<DiffusionGroup> diffusionGroups = contact.getDiffusionGroups();
			if (diffusionGroups.isEmpty()) {
				contactService.deleteContactById(contact.getId());
			} else {
				populateReturnError(contact, redir, diffusionGroups);
			}
		}
		return modelAndView;
	}

	@GetMapping("/contacts/forcedelete/{id}")
	public ModelAndView forceDeleteContact(@PathVariable("id") long id, Model model, RedirectAttributes redir) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_CONTACTS);
		contactService.forceDeleteContactById(id);
		return modelAndView;
	}

	private void populateReturnError(Contact contact, RedirectAttributes redir, Set<DiffusionGroup> diffusionGroups) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorCode", true);
		redir.addFlashAttribute("errorObjectId", contact.getId());
		redir.addFlashAttribute("errorMessage", "Impossible de supprimer le contact " + contact.getName()
				+ ". Il appartient a la (aux) liste(s) de diffusion suivante(s) : " + StringUtils.join(diffusionGroups, ", ") + ".");
	}

	private void populateReturnError(Long id, RedirectAttributes redir) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Attention, Invalid Contact Id : " + id);
	}

}
