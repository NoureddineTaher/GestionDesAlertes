package fr.real.supervision.appliinfo.web.historiques;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorisationRepository extends PagingAndSortingRepository<Historisation, Long> {


    public Historisation findTop1ByIdMailOrderByDateEnvoiDesc(long idMail);

    public List<Historisation> findByIdMail(long id);
}
