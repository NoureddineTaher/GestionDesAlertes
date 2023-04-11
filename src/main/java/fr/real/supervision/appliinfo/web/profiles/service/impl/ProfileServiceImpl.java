package fr.real.supervision.appliinfo.web.profiles.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.real.supervision.appliinfo.model.Profile;
import fr.real.supervision.appliinfo.model.ProfileFunctionality;
import fr.real.supervision.appliinfo.repository.ProfileRepository;
import fr.real.supervision.appliinfo.web.profiles.dto.ProfileDto;
import fr.real.supervision.appliinfo.web.profiles.service.ProfileFunctionalityService;
import fr.real.supervision.appliinfo.web.profiles.service.ProfileService;

@Component
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private ProfileFunctionalityService profileFunctionalityService;

	@Override
	public List<Profile> getProfiles() {
		Iterable<Profile> findAll = profileRepository.findAll();
		return StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public List<Profile> getProfilesByHiddenFalse() {
		Iterable<Profile> findAll = profileRepository.findAllByHiddenFalse();
		return StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public void save(ProfileDto dto) {
		Profile profile = populateProfileFromProfileDto(dto);
		profileRepository.save(profile);

		profile.getProfileFunctionalities().stream().forEach(profileFunctionality -> {
			profileFunctionality.setProfile(profile);
			profileFunctionalityService.save(profileFunctionality);
		});
	}

	@Override
	public void updateProfile(ProfileDto profileDto) {
		Profile profile = getProfileById(profileDto.getId());

		populateExistingProfileFromProfileDto(profile, profileDto);

		profileRepository.save(profile);
	}

	@Override
	public Profile getProfileById(Long id) {
		return profileRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteProfileById(Long id) {
		profileRepository.deleteById(id);
	}

	@Override
	public ProfileDto getProfileDtoById(Long id) {
		Profile profile = getProfileById(id);
		if (profile == null) {
			return null;
		} else {
			return populateProfileDtoFromProfile(profile);
		}
	}

	private Profile populateProfileFromProfileDto(ProfileDto dto) {
		Profile profile = new Profile();
		return populateExistingProfileFromProfileDto(profile, dto);
	}

	private Profile populateExistingProfileFromProfileDto(Profile profile, ProfileDto dto) {
		profile.setName(dto.getName());
		profile.setDescription(dto.getDescription());
		profile.setProfileFunctionalities(dto.getProfileFunctionalitiesDto().stream()
				.map(profileFunctionalityDto -> profileFunctionalityService
						.populateProfileFunctionalityFromProfileFunctionalityDto(profileFunctionalityDto))
				.collect(Collectors.toList()));
		return profile;
	}

	private ProfileDto populateProfileDtoFromProfile(Profile profile) {
		List<ProfileFunctionality> profileFunctionalities = profileFunctionalityService
				.getProfileFunctionalitiesByProfileId(profile.getId());

		ProfileDto profileDto = new ProfileDto();
		profileDto.setId(profile.getId());
		profileDto.setName(profile.getName());
		profileDto.setDescription(profile.getDescription());
		profileDto.setProfileFunctionalitiesDto(profileFunctionalities.stream().map(profileFunctionality ->

		profileFunctionalityService.populateProfileFunctionalityDtoFromProfileFunctionality(profileFunctionality)

		).collect(Collectors.toList()));

		return profileDto;
	}

}
