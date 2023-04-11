package fr.real.supervision.appliinfo.web.profiles.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.ProfileFunctionality;
import fr.real.supervision.appliinfo.web.profiles.dto.ProfileFunctionalityDto;

@Service
public interface ProfileFunctionalityService {

	List<ProfileFunctionality> getProfileFunctionalitiesByProfileId(Long profileId);

	void save(ProfileFunctionality profileFunctionality);

	ProfileFunctionalityDto createProfileFunctionalityDto(Long id);

	ProfileFunctionality populateProfileFunctionalityFromProfileFunctionalityDto(
			ProfileFunctionalityDto profileFunctionalityDto);

	ProfileFunctionalityDto populateProfileFunctionalityDtoFromProfileFunctionality(
			ProfileFunctionality profileFunctionality);

}
