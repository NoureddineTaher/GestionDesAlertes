package fr.real.supervision.appliinfo.web.category.controller;

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
import fr.real.supervision.appliinfo.web.category.dto.CategoryDto;
import fr.real.supervision.appliinfo.web.category.service.CategoryService;

@Controller
public class CategoryController {

	private static final String ATTRIBUTE_CATEGORYDTO = "categoryDto";

	private static final String VIEW_UPDATECATEGORY = "categories/update_category.html";

	private static final String VIEW_CATEGORIES = "categories/categories";

	private static final String VIEW_ADDCATEGORY = "categories/add_category.html";

	private static final String REDIRECT_CATEGORIES = "redirect:/categories";

	private static final String VERSION= "version";

	@Autowired
	private CategoryService categoryService;

	@Value("${info.app.version}")
	String applicationVersion;

	@GetMapping("/categories")
	public ModelAndView getCategories(Model model) {
		ModelAndView modelAndView = new ModelAndView(VIEW_CATEGORIES);
		modelAndView.addObject("categories", categoryService.getCategories());
		modelAndView.addObject(VERSION,applicationVersion);
		return modelAndView ;
	}

	@GetMapping("/categories/addcategory")
	public ModelAndView addCategory(CategoryDto categoryDto) {
		return new ModelAndView(VIEW_ADDCATEGORY).addObject(VERSION,applicationVersion);
	}

	@PostMapping("/categories/addcategory")
	public ModelAndView addCategory(@Valid CategoryDto categoryDto, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return new ModelAndView(VIEW_ADDCATEGORY);
		}
		categoryService.save(categoryDto);
		return new ModelAndView(REDIRECT_CATEGORIES);
	}

	@GetMapping("/categories/edit/{id}")
	public ModelAndView updateCategory(@PathVariable("id") long id, Model model, RedirectAttributes redir) {
		ModelAndView modelAndView = new ModelAndView();
		CategoryDto categoryDto = categoryService.getCategoryDtoById(id);
		if (categoryDto != null) {
			modelAndView.setViewName(VIEW_UPDATECATEGORY);
			model.addAttribute(ATTRIBUTE_CATEGORYDTO, categoryDto);
		} else {
			modelAndView.setViewName(REDIRECT_CATEGORIES);
			populateReturnError(id, redir);
		}
		modelAndView.addObject(VERSION,applicationVersion);
		return modelAndView;
	}

	@PostMapping("/categories/edit/{id}")
	public ModelAndView updateCategory(@PathVariable("id") long id, @Valid CategoryDto categoryDto,
			BindingResult result, Model model, RedirectAttributes redir) {

		ModelAndView modelAndView = new ModelAndView(REDIRECT_CATEGORIES);
		if (categoryService.getCategoryDtoById(id) == null) {
			populateReturnError(id, redir);
			return modelAndView;
		}
		if (result.hasErrors()) {
			modelAndView.setViewName(VIEW_UPDATECATEGORY);
			model.addAttribute(ATTRIBUTE_CATEGORYDTO, categoryDto);
			return modelAndView;
		}
		categoryService.update(categoryDto);
		return modelAndView;
	}

	@GetMapping("/categories/delete/{id}")
	public ModelAndView deleteCategory(@PathVariable("id") long id, Model model, RedirectAttributes redir) {
		ModelAndView modelAndView = new ModelAndView(REDIRECT_CATEGORIES);
		CategoryDto categoryDto = categoryService.getCategoryDtoById(id);
		if (categoryDto != null) {
			List<Alert> alerts = categoryService.getAlertsForThisCategory(id);
			if (alerts.isEmpty()) {
				categoryService.deleteCategoryById(id);
			} else {
				populateReturnError(categoryDto, redir, alerts);
			}
		} else {
			populateReturnError(id, redir);
		}
		return modelAndView;
	}

	private void populateReturnError(Long id, RedirectAttributes redir) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage", "Attention, Invalid Category Id : " + id);
	}

	private void populateReturnError(CategoryDto categoryDto, RedirectAttributes redir, List<Alert> alerts) {
		redir.addFlashAttribute("error", true);
		redir.addFlashAttribute("errorMessage","Impossible de supprimer la catégorie " + categoryDto.getName()
				+ ". Elle est utilisée dans l'alerte(s) suivante(s) : " + StringUtils.join(alerts, ", ") + ".");
	}

}
