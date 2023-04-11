package fr.real.supervision.appliinfo.web.category.service;

import java.util.List;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.Category;
import fr.real.supervision.appliinfo.web.category.dto.CategoryDto;

public interface CategoryService {

	public List<Category> getCategories();

	CategoryDto getCategoryDtoById(Long id);

	Category getCategoryById(Long id);

	void deleteCategoryById(long id);

	void save(CategoryDto categoryDto);

	void update(CategoryDto categoryDto);

	List<Alert> getAlertsForThisCategory(Long id);

}
