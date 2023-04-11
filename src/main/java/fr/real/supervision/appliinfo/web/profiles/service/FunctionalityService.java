package fr.real.supervision.appliinfo.web.profiles.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Functionality;

@Service
public interface FunctionalityService {

	Functionality getFunctionalityById(Long id);

	List<Functionality> findFunctionalities();

	String findTypeById(Long id);

	String findNameById(Long id);

}
