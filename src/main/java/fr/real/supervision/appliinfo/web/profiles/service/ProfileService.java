package fr.real.supervision.appliinfo.web.profiles.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Profile;
import fr.real.supervision.appliinfo.web.profiles.dto.ProfileDto;

@Service
public interface ProfileService {

	List<Profile> getProfiles();

	List<Profile> getProfilesByHiddenFalse();

	void save(ProfileDto dto);

	void updateProfile(ProfileDto profileDto);

	Profile getProfileById(Long id);

	void deleteProfileById(Long id);

	ProfileDto getProfileDtoById(Long id);

}
