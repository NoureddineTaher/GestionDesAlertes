package fr.real.supervision.appliinfo.repository;

import fr.real.supervision.appliinfo.model.Profile;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProfileRepository  extends PagingAndSortingRepository<Profile, Long> {

    Iterable<Profile> findAllByHiddenFalse();
}
