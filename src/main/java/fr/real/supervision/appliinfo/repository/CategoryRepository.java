package fr.real.supervision.appliinfo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import fr.real.supervision.appliinfo.model.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

}
