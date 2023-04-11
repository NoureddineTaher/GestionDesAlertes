package fr.real.supervision.appliinfo.web.category.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.Category;
import fr.real.supervision.appliinfo.repository.AlertRepository;
import fr.real.supervision.appliinfo.repository.CategoryRepository;
import fr.real.supervision.appliinfo.web.category.dto.CategoryDto;
import fr.real.supervision.appliinfo.web.category.service.CategoryService;
import fr.real.supervision.appliinfo.web.util.AuthenticationUser;

@Service
public class CategoryServiceImpl implements CategoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Override
	public List<Category> getCategories() {
		Iterable<Category> findAll = categoryRepository.findAll();
		List<Category> collect = StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
		collect.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
		return collect;
	}

	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public CategoryDto getCategoryDtoById(Long id) {
		Category category = getCategoryById(id);
		if (category == null) {
			return null;
		} else {
			return populateCategoryDtoFromCategory(category);
		}
	}

	@Override
	public void save(CategoryDto categoryDto) {
		Category category = populateCategoryFromCategoryDto(categoryDto);
		category.setCreatedBy(AuthenticationUser.getAuthenticatedUser());
		categoryRepository.save(category);
		LOGGER.info("Creation categorie : {} par {} le {} ", category.getName(), category.getCreatedBy(),
				category.getCreatedDate());
	}

	@Override
	public void update(CategoryDto categoryDto) {
		Category category = populateCategoryFromCategoryDto(categoryDto);
		category.setLastModifiedBy(AuthenticationUser.getAuthenticatedUser());
		category.setLastModifiedDate(LocalDateTime.now());
		categoryRepository.save(category);
		LOGGER.info("Modification categorie : {} par {} le {} ", category.getName(), category.getLastModifiedBy(),
				category.getLastModifiedDate());
	}

	@Override
	public void deleteCategoryById(long id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public List<Alert> getAlertsForThisCategory(Long id) {
		return alertRepository.findByCategoryId(id);
	}

	private Category populateCategoryFromCategoryDto(CategoryDto categoryDto) {
		Category category = new Category();
		category.setId(categoryDto.getId());
		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());
		return category;
	}

	private CategoryDto populateCategoryDtoFromCategory(Category category) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(category.getId());
		categoryDto.setName(category.getName());
		categoryDto.setDescription(category.getDescription());
		return categoryDto;
	}

}
