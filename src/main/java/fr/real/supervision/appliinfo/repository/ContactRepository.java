package fr.real.supervision.appliinfo.repository;

import fr.real.supervision.appliinfo.model.DiffusionGroup;
import org.springframework.data.repository.PagingAndSortingRepository;

import fr.real.supervision.appliinfo.model.Contact;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {

    List<Contact> findContactsByDiffusionGroupsIn(List<DiffusionGroup> diffusionGroupList);

}
