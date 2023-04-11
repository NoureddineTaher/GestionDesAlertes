package fr.real.supervision.appliinfo.web.contacts.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Contact;
import fr.real.supervision.appliinfo.model.DiffusionGroup;
import fr.real.supervision.appliinfo.repository.ContactRepository;
import fr.real.supervision.appliinfo.repository.DiffusionGroupRepository;
import fr.real.supervision.appliinfo.web.contacts.dto.ContactDto;
import fr.real.supervision.appliinfo.web.contacts.service.ContactService;
import fr.real.supervision.appliinfo.web.util.AuthenticationUser;

@Service
public class ContactServiceImpl implements ContactService {

	private static final Logger LOG = LoggerFactory.getLogger(ContactServiceImpl.class);

	@Autowired
	ContactRepository contactRepository;

	@Autowired
	DiffusionGroupRepository diffusionGroupRepository;

	@Override
	public List<Contact> getContacts() {
		Iterable<Contact> findAll = contactRepository.findAll();
		List<Contact> collect = StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
		collect.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
		return collect;
	}

	@Override
	public Contact getContactById(Long id) {
		return contactRepository.findById(id).orElse(null);
	}

	@Override
	public ContactDto getContactDtoById(Long id) {
		Contact contact = getContactById(id);
		if (contact == null) {
			return null;
		} else {
			return populateContactDtoFromContact(contact);
		}
	}

	@Override
	public void deleteContactById(Long id) {
		contactRepository.deleteById(id);
	}

	@Override
	public void forceDeleteContactById(Long id) {
		Optional<Contact> contact = contactRepository.findById(id);
		if (contact.isPresent()) {
			Set<DiffusionGroup> diffusionGroups = contact.get().getDiffusionGroups();
			diffusionGroups.forEach(x -> x.getContacts().remove(contact.get()));
			diffusionGroups.forEach(x -> diffusionGroupRepository.save(x));
			contactRepository.delete(contact.get());
		}
	}

	@Override
	public void saveContact(ContactDto contactDto) {
		Contact contact = populateContactFromContactDto(contactDto);
		contact.setCreatedBy(AuthenticationUser.getAuthenticatedUser());
		contactRepository.save(contact);
		LOG.info("Creation contact : {} par {} le {}", contact.getName(), contact.getCreatedBy(),
				contact.getCreatedDate());
	}

	@Override
	public void updateContact(ContactDto contactDto) {
		Contact contact = populateContactFromContactDto(contactDto);
		contact.setLastModifiedBy(AuthenticationUser.getAuthenticatedUser());
		contact.setLastModifiedDate(LocalDateTime.now());
		contactRepository.save(contact);
		LOG.info("Modification contact : {} par {} le {}", contact.getName(), contact.getLastModifiedBy(),
				contact.getLastModifiedDate());
	}

	private Contact populateContactFromContactDto(ContactDto contactDto) {
		Contact contact = new Contact();
		contact.setId(contactDto.getId());
		contact.setName(contactDto.getName());
		contact.setFirstname(contactDto.getFirstname());
		contact.setEmail(contactDto.getEmail());
		contact.setPhone(contactDto.getPhone());
		contact.setDiffusionGroups(contactDto.getDiffusionGroups());
		return contact;
	}

	private ContactDto populateContactDtoFromContact(Contact contact) {
		ContactDto contactDto = new ContactDto();
		contactDto.setId(contact.getId());
		contactDto.setName(contact.getName());
		contactDto.setFirstname(contact.getFirstname());
		contactDto.setEmail(contact.getEmail());
		contactDto.setPhone(contact.getPhone());
		contactDto.setDiffusionGroups(contact.getDiffusionGroups());
		return contactDto;
	}

}
