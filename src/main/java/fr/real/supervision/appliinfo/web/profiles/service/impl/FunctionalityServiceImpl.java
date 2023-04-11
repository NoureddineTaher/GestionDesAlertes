package fr.real.supervision.appliinfo.web.profiles.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.real.supervision.appliinfo.model.Functionality;
import fr.real.supervision.appliinfo.repository.FunctionalityRepository;
import fr.real.supervision.appliinfo.web.profiles.dto.FunctionalityDto;
import fr.real.supervision.appliinfo.web.profiles.service.FunctionalityService;

@Component
public class FunctionalityServiceImpl implements FunctionalityService {

	@Autowired
	private FunctionalityRepository functionalityRepository;

	public FunctionalityDto populateFunctionalityDtoFromFunctionality(Functionality functionality) {

		FunctionalityDto functionalityDto = new FunctionalityDto();

		functionalityDto.setId(functionality.getId());
		functionalityDto.setName(functionality.getName());
		functionalityDto.setType(functionality.getType().name());

		return functionalityDto;
	}

	@Override
	public Functionality getFunctionalityById(Long id) {
		return functionalityRepository.findById(id).orElse(null);
	}

	@Override
	public List<Functionality> findFunctionalities() {
		Iterable<Functionality> findAll = functionalityRepository.findAll();
		return StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public String findTypeById(Long id) {
		Functionality functionality = getFunctionalityById(id);
		return functionality.getType().name();
	}

	@Override
	public String findNameById(Long id) {
		Functionality functionality = getFunctionalityById(id);
		return functionality.getName();
	}
}
