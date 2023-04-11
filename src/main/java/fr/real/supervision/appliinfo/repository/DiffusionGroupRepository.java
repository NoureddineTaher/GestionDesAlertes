package fr.real.supervision.appliinfo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import fr.real.supervision.appliinfo.model.DiffusionGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiffusionGroupRepository extends PagingAndSortingRepository<DiffusionGroup, Long> {

    List<DiffusionGroup> findDiffusionGroupByNameIn(List<String> names);
}
