package fr.real.supervision.appliinfo.job.maintenance;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import fr.real.supervision.appliinfo.model.Alert;
import fr.real.supervision.appliinfo.model.Maintenance;
import fr.real.supervision.appliinfo.web.alerts.service.AlertService;
import fr.real.supervision.appliinfo.web.maintenance.service.MaintenanceService;

public class MaintenanceJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(MaintenanceJob.class);

	@Autowired
	MaintenanceService maintenanceService;

	@Autowired
	AlertService alertService;

	@Scheduled(fixedDelayString = "${jobs.maintenance.alertActivationStatus.update.delay}")
	public void process() {

		LOGGER.info("Maintenance: Début Job Update Statut d'Activation des Alertes");

		try {
			List<Alert> alerts = alertService.getAlerts();

			// Pour chaque alerte, évaluation si une plage de maintenance planifiée est en
			// cours :
			// 1) Pas de maintenance : activation/réactivation de l'alerte
			// 2) Maintenance en cours: désactivation de l'alerte
			for (Alert alert : alerts) {

				List<Maintenance> onGoingMaintenances = maintenanceService.getOnGoingMaintenancesByAlert(alert);

				if (onGoingMaintenances.isEmpty()) {
					alertService.updateActiveAlert(alert.getId(), true);
				} else {
					alertService.updateActiveAlert(alert.getId(), false);

					StringBuilder maintenanceInfosStringBuilder = new StringBuilder(200);

					for (Maintenance maintenance : onGoingMaintenances) {
						maintenanceInfosStringBuilder.append(" Id: " + maintenance.getId() + "; StartTime: "
								+ maintenance.getStartTime() + "; EndTime: " + maintenance.getEndTime());
					}

					LOGGER.info(
							"Plage de maintenance(s) en cours sur l'alerte {} - Alerte désactivée - Id(s) et plage(s) de maintenance: {}",
							alert.getName(), maintenanceInfosStringBuilder);
				}

			}

		} catch (Exception e) {
			LOGGER.error("Une erreur est survenue pendant le Job Update Statut d'Activation des Alertes", e);
		} finally {
			LOGGER.info("Maintenance: Fin du Job Update Statut d'Activation des Alertes");
		}

	}
}
