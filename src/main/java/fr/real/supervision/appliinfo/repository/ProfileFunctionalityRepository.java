package fr.real.supervision.appliinfo.repository;

import fr.real.supervision.appliinfo.model.ProfileFunctionality;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProfileFunctionalityRepository extends PagingAndSortingRepository<ProfileFunctionality, Long> {

    Iterable<ProfileFunctionality> findAllByProfileId(Long profileId);

}
