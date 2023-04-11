package fr.real.supervision.appliinfo.web.profiles.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.real.supervision.appliinfo.model.ProfileFunctionality;
import fr.real.supervision.appliinfo.repository.ProfileFunctionalityRepository;
import fr.real.supervision.appliinfo.web.profiles.dto.ProfileFunctionalityDto;
import fr.real.supervision.appliinfo.web.profiles.service.FunctionalityService;
import fr.real.supervision.appliinfo.web.profiles.service.ProfileFunctionalityService;
import fr.real.supervision.appliinfo.web.profiles.service.ProfileService;

@Component
public class ProfileFunctionalityServiceImpl implements ProfileFunctionalityService {

	@Autowired
	private ProfileFunctionalityRepository profileFunctionalityRepository;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private FunctionalityService functionalityService;

	@Override
	public List<ProfileFunctionality> getProfileFunctionalitiesByProfileId(Long profileId) {
		Iterable<ProfileFunctionality> findAll = profileFunctionalityRepository.findAllByProfileId(profileId);
		return StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public void save(ProfileFunctionality profileFunctionality) {
		profileFunctionalityRepository.save(profileFunctionality);
	}

	public ProfileFunctionalityDto createProfileFunctionalityDto(Long functionalityId) {
		ProfileFunctionalityDto profileFunctionalityDto = new ProfileFunctionalityDto();

		profileFunctionalityDto.setFunctionalityDtoId(functionalityId);
		profileFunctionalityDto.setRead(false);
		profileFunctionalityDto.setWrite(false);

		return profileFunctionalityDto;
	}

	public ProfileFunctionalityDto populateProfileFunctionalityDtoFromProfileFunctionality(
			ProfileFunctionality profileFunctionality) {

		ProfileFunctionalityDto profileFunctionalityDto = new ProfileFunctionalityDto();

		profileFunctionalityDto.setId(profileFunctionality.getId());
		profileFunctionalityDto.setProfileDtoId(profileFunctionality.getProfile().getId());
		profileFunctionalityDto.setFunctionalityDtoId(profileFunctionality.getFunctionality().getId());
		profileFunctionalityDto.setRead(profileFunctionality.isRead());
		profileFunctionalityDto.setWrite(profileFunctionality.isWrite());

		return profileFunctionalityDto;
	}

	public ProfileFunctionality populateProfileFunctionalityFromProfileFunctionalityDto(
			ProfileFunctionalityDto profileFunctionalityDto) {

		ProfileFunctionality profileFunctionality = new ProfileFunctionality();

		if (profileFunctionalityDto.getId() != null) {
			profileFunctionality.setId(profileFunctionalityDto.getId());
		}

		profileFunctionality.setWrite(profileFunctionalityDto.getWrite());
		profileFunctionality.setRead(profileFunctionalityDto.getRead());

		if (profileFunctionalityDto.getProfileDtoId() != null) {
			profileFunctionality.setProfile(profileService.getProfileById(profileFunctionalityDto.getProfileDtoId()));
		}

		profileFunctionality.setFunctionality(
				functionalityService.getFunctionalityById(profileFunctionalityDto.getFunctionalityDtoId()));

		return profileFunctionality;

	}

}
