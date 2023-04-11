package fr.real.supervision.appliinfo.web.historique.service.imp;

import fr.real.supervision.appliinfo.model.AlertEvent;

public interface HistoryService {
    public Iterable<AlertEvent> getAllAlertEvents();
}
