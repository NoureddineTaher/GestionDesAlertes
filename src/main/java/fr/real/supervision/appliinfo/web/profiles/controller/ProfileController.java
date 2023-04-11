package fr.real.supervision.appliinfo.web.profiles.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.real.supervision.appliinfo.model.Profile;
import fr.real.supervision.appliinfo.web.profiles.dto.ProfileDto;
import fr.real.supervision.appliinfo.web.profiles.service.FunctionalityService;
import fr.real.supervision.appliinfo.web.profiles.service.ProfileFunctionalityService;
import fr.real.supervision.appliinfo.web.profiles.service.ProfileService;

@Controller
@RequestMapping("/profiles")
public class ProfileController {

	private static final String REDIRECT_PROFILES = "redirect:/profiles";

	private static final String VIEW_PROFILES = "profiles/profiles";

	private static final String VIEW_ADD_PROFILE = "profiles/add_profile";

	private static final String VIEW_UPDATE_PROFILE = "profiles/update_profile";

	private static final String ATTRIBUTE_PROFILES = "profiles";
	private static final String ATTRIBUTE_PROFILE_DTO = "profileDto";
	private static final String ATTRIBUTE_FUNCTIONALITY_SERVICE = "functionalityService";

	private static final String FIELD_NAME = "name";

	private static final String FIELD_DESCRIPTION = "description";

	private static final String ERROR_NAME_LENGTH = "Le nom ne doit pas dépasser 15 caracteres";
	private static final String ERROR_DESCRIPTION_LENGTH = "La description ne doit pas dépasser 50 caracteres";

	private static final String ERROR_DURING_PROFILE_SAVING_MSG = "Erreur lors de l'enregistrement - Champ incorrect (le nom doit être unique)";

	private static final String VERSION = "version";

	@Autowired
	private ProfileService profileService;

	@Autowired
	private ProfileFunctionalityService profileFunctionalityService;

	@Autowired
	private FunctionalityService functionalityService;

	@Value("${info.app.version}")
	String applicationVersion;

	@GetMapping("")
	public ModelAndView getProfiles() {
		ModelAndView modelAndView = new ModelAndView(VIEW_PROFILES);
		modelAndView.addObject(ATTRIBUTE_PROFILES, profileService.getProfilesByHiddenFalse());
		modelAndView.addObject(VERSION, applicationVersion);
		return modelAndView;
	}

	@GetMapping("/addprofile")
	public ModelAndView addProfile() {
		ModelAndView modelAndView = new ModelAndView(VIEW_ADD_PROFILE);
		ProfileDto attributeValue = new ProfileDto();

		attributeValue.setProfileFunctionalitiesDto(functionalityService.findFunctionalities().stream()
				.map(functionality -> profileFunctionalityService.createProfileFunctionalityDto(functionality.getId()))
				.collect(Collectors.toList()));

		modelAndView.addObject(ATTRIBUTE_PROFILE_DTO, attributeValue);
		modelAndView.addObject(ATTRIBUTE_FUNCTIONALITY_SERVICE, functionalityService);
		modelAndView.addObject(VERSION, applicationVersion);
		return modelAndView;
	}

	@PostMapping("/addprofile")
	public ModelAndView addProfile(@Valid @ModelAttribute(value = ATTRIBUTE_PROFILE_DTO) ProfileDto profileDto,
			BindingResult bindingResult) {

		verifyPropertiesDto(profileDto, bindingResult);

		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_ADD_PROFILE);
			modelAndView.addObject(ATTRIBUTE_PROFILE_DTO, profileDto);
			modelAndView.addObject(ATTRIBUTE_FUNCTIONALITY_SERVICE, functionalityService);
			return modelAndView;
		}

		try {
			profileService.save(profileDto);
		} catch (Exception e) {
			bindingResult.addError(new FieldError(ATTRIBUTE_PROFILE_DTO, FIELD_NAME, ERROR_DURING_PROFILE_SAVING_MSG));
			return new ModelAndView(VIEW_ADD_PROFILE);
		}
		return new ModelAndView(REDIRECT_PROFILES);
	}

	@GetMapping("/update/{id}")
	public ModelAndView updateProfile(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();

		ProfileDto profileDto = profileService.getProfileDtoById(id);

		if (profileDto != null) {
			modelAndView.setViewName(VIEW_UPDATE_PROFILE);
			modelAndView.addObject(ATTRIBUTE_PROFILE_DTO, profileDto);
			modelAndView.addObject(ATTRIBUTE_FUNCTIONALITY_SERVICE, functionalityService);

		} else {
			modelAndView.setViewName(REDIRECT_PROFILES);
			populateReturnError(id, redirectAttributes);
		}
		modelAndView.addObject(VERSION, applicationVersion);
		return modelAndView;
	}

	@PostMapping("/update/{id}")
	public ModelAndView updateProfile(@PathVariable("id") long id,
			@Valid @ModelAttribute(value = ATTRIBUTE_PROFILE_DTO) ProfileDto profileDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		Profile profile = profileService.getProfileById(id);

		if (profile == null) {
			populateReturnError(id, redirectAttributes);
			return new ModelAndView(REDIRECT_PROFILES);
		}

		verifyPropertiesDto(profileDto, bindingResult);

		if (bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_UPDATE_PROFILE);
			modelAndView.addObject(ATTRIBUTE_PROFILE_DTO, profileDto);
			modelAndView.addObject(ATTRIBUTE_FUNCTIONALITY_SERVICE, functionalityService);
			return modelAndView;
		}

		try {

			profileService.updateProfile(profileDto);
		} catch (Exception e) {
			bindingResult.addError(new FieldError(ATTRIBUTE_PROFILE_DTO, FIELD_NAME, ERROR_DURING_PROFILE_SAVING_MSG));

			return new ModelAndView(VIEW_ADD_PROFILE);
		}

		return new ModelAndView(REDIRECT_PROFILES);

	}

	@GetMapping("/delete/{id}")
	public ModelAndView deleteProfile(@PathVariable("id") long id, RedirectAttributes redirectAttribute) {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_PROFILES);
		Profile profile = profileService.getProfileById(id);

		if (profile != null) {
			profileService.deleteProfileById(id);
		} else {
			populateReturnError(id, redirectAttribute);
		}

		return modelAndView;

	}

	private void populateReturnError(Long id, RedirectAttributes redir) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Attention, Id Profil invalide : " + id);
	}

	private void verifyPropertiesDto(ProfileDto profileDto, BindingResult bindingResult) {
		if (profileDto.getName().length() > 15) {
			bindingResult.addError(new FieldError(ATTRIBUTE_PROFILE_DTO, FIELD_NAME, ERROR_NAME_LENGTH));
		}

		if (profileDto.getDescription().length() > 50) {
			bindingResult.addError(new FieldError(ATTRIBUTE_PROFILE_DTO, FIELD_DESCRIPTION, ERROR_DESCRIPTION_LENGTH));
		}
	}

}
