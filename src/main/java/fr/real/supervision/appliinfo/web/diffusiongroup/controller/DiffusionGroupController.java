package fr.real.supervision.appliinfo.web.diffusiongroup.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.DiffusionGroup;
import fr.real.supervision.appliinfo.web.contacts.service.ContactService;
import fr.real.supervision.appliinfo.web.diffusiongroup.dto.DiffusionGroupDto;
import fr.real.supervision.appliinfo.web.diffusiongroup.service.DiffusionGroupService;

@Controller
public class DiffusionGroupController {

	private static final String VIEW_DIFFUSIONGROUPS = "diffusionGroup/diffusionGroups";

	private static final String VIEW_ADD_DIFFUSIONGROUP = "diffusionGroup/add_diffusiongroup";

	private static final String VIEW_UPDATE_DIFFUSIONGROUP = "diffusionGroup/update_diffustiongroup";

	private static final String REDIRECT_DIFFUSIONS = "redirect:/diffusions";

	private static final String ATTRIBUTE_LISTCONTACTS = "listcontacts";

	private static final String ATTRIBUTE_DIFFUSIONGROUPS = "diffusionGroups";

	private static final String ATTRIBUTE_DIFFUSION_GROUP = "diffusionGroupDto";

	private static final String VERSION = "version" ;

	@Value("${info.app.version}")
	String applicationVersion;

	@Autowired
	private DiffusionGroupService diffusionGroupService;

	@Autowired
	private ContactService contactService;

	@GetMapping("/diffusions")
	public ModelAndView getDiffusionGroups(Model model) {
		ModelAndView modelAndView = new ModelAndView(VIEW_DIFFUSIONGROUPS);
		modelAndView.addObject(ATTRIBUTE_DIFFUSIONGROUPS,diffusionGroupService.getDiffusionGroups());
		modelAndView.addObject(VERSION,applicationVersion);
		return modelAndView;
	}

	@GetMapping("/diffusions/addDiffusionGroup")
	public ModelAndView addDiffusionGroup(DiffusionGroupDto diffusionGroupDto) {
		ModelAndView modelAndView = new ModelAndView(VIEW_ADD_DIFFUSIONGROUP);
		modelAndView.addObject(ATTRIBUTE_LISTCONTACTS, contactService.getContacts());
		modelAndView.addObject(VERSION,applicationVersion);
		return modelAndView;
	}

	@PostMapping("/diffusions/addDiffusionGroup")
	public ModelAndView addDiffusionGroup(@Valid DiffusionGroupDto diffusionGroupDto, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView(VIEW_ADD_DIFFUSIONGROUP);
			modelAndView.addObject(ATTRIBUTE_LISTCONTACTS, contactService.getContacts());
			return modelAndView;
		}
		diffusionGroupService.saveDiffusionGroup(diffusionGroupDto);
		return new ModelAndView(REDIRECT_DIFFUSIONS);
	}

	@GetMapping("/diffusions/edit/{id}")
	public ModelAndView updateDiffusionGroup(@PathVariable("id") long id, Model model, RedirectAttributes redir) {

		ModelAndView modelAndView = new ModelAndView();
		DiffusionGroupDto diffusionGroupDto = diffusionGroupService.getDiffusionGroupDtoById(id);

		if (diffusionGroupDto != null) {
			model.addAttribute(ATTRIBUTE_DIFFUSION_GROUP, diffusionGroupDto);
			model.addAttribute(ATTRIBUTE_LISTCONTACTS, contactService.getContacts());
			modelAndView.setViewName(VIEW_UPDATE_DIFFUSIONGROUP);
		} else {
			populateReturnError(id, redir);
			modelAndView.setViewName(REDIRECT_DIFFUSIONS);
		}
		modelAndView.addObject(VERSION,applicationVersion);
		return modelAndView;
	}

	@PostMapping("/diffusions/edit/{id}")
	public ModelAndView updateDiffusionGroup(@PathVariable("id") long id, @Valid DiffusionGroupDto diffGroupDto,
			BindingResult result, Model model, RedirectAttributes redir) {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_DIFFUSIONS);
		if (diffusionGroupService.getDiffusionGroupById(id) == null) {
			populateReturnError(id, redir);
			return modelAndView;
		}
		if (result.hasErrors()) {
			model.addAttribute(ATTRIBUTE_DIFFUSION_GROUP, diffGroupDto);
			model.addAttribute(ATTRIBUTE_LISTCONTACTS, contactService.getContacts());
			return new ModelAndView(VIEW_UPDATE_DIFFUSIONGROUP);
		}
		diffusionGroupService.updateDiffusionGroup(diffGroupDto);
		return modelAndView;
	}

	@GetMapping("/diffusions/delete/{id}")
	public ModelAndView deleteDiffusionGroup(@PathVariable("id") long id, Model model, RedirectAttributes redir) {
		DiffusionGroup diffusionGroup = diffusionGroupService.getDiffusionGroupById(id);
		ModelAndView modelAndView = new ModelAndView(REDIRECT_DIFFUSIONS);
		if (diffusionGroup != null) {
			List<Alert> alertsForThisDiffusionGroup = diffusionGroupService.getAlertsForThisDiffusionGroup(id);
			if (alertsForThisDiffusionGroup.isEmpty()) {
				diffusionGroupService.deleteDiffusionGroupById(id);
			} else {
				populateReturnError(diffusionGroup, redir, alertsForThisDiffusionGroup);
			}
		} else {
			populateReturnError(id, redir);
		}
		return modelAndView;
	}

	private void populateReturnError(long id, RedirectAttributes redir) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Attention, Invalid DiffGroup Id : " + id);
	}

	private void populateReturnError(DiffusionGroup diffusionGroup, RedirectAttributes redir, List<Alert> alerts) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Impossible de supprimer la liste de diffusion " + diffusionGroup.getName()
						+ ". Elle est utilisee dans l'alerte(s) suivante(s) : " + StringUtils.join(alerts, ", ") + ".");
	}

}
