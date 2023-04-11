package fr.real.supervision.appliinfo.web.contacts.service;

import java.util.List;

import fr.real.supervision.appliinfo.model.Contact;
import fr.real.supervision.appliinfo.web.contacts.dto.ContactDto;

public interface ContactService {

	public List<Contact> getContacts();

	public Contact getContactById(Long id);

	public void deleteContactById(Long id);

	public void forceDeleteContactById(Long id);

	public void saveContact(ContactDto contactDto);

	public void updateContact(ContactDto contactDto);

	public ContactDto getContactDtoById(Long id);

}
