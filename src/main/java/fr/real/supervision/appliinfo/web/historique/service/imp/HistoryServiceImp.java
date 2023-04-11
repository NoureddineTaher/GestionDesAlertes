package fr.real.supervision.appliinfo.web.historique.service.imp;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.AlertEvent;
import fr.real.supervision.appliinfo.repository.AlertEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class HistoryServiceImp implements HistoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(fr.real.supervision.appliinfo.web.alerts.service.impl.AlertServiceImpl.class);

    @Autowired
    private AlertEventRepository alertEventRepository;

    public List<AlertEvent> getAllAlertEvents(){
        Iterable<AlertEvent> alertEvents=alertEventRepository.findAll();
        List<AlertEvent> collect = StreamSupport.stream(alertEvents.spliterator(), false).collect(Collectors.toList());
        return collect;
    };


}