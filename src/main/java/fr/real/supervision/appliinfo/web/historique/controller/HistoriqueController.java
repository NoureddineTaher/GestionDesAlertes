package fr.real.supervision.appliinfo.web.historique.controller;

import fr.real.supervision.appliinfo.model.AlertEvent;


import fr.real.supervision.appliinfo.repository.AlertEventRepository;

import fr.real.supervision.appliinfo.web.historique.HistotyProperties;
import fr.real.supervision.appliinfo.web.historique.service.imp.HistoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Controller
@RequestMapping("/historique")
@EnableConfigurationProperties(HistotyProperties.class)

public class HistoriqueController {

    private static final String VIEW_HISTORIQUE = "historique/historique";
    private static final String ONE_DAY = "24h";
    private static final String ONE_MONTH = "30 jours";
    private static final String ONE_WEEK = "7 jours";


    @Autowired
    HistotyProperties properties;
    @Autowired
    private HistoryServiceImp historyService ;
    @Autowired
    private AlertEventRepository alertEventRepository;


    @GetMapping("")
    public ModelAndView historique() {

        ModelAndView modelAndView = new ModelAndView(VIEW_HISTORIQUE);
        List<AlertEvent> alertEvents= filterAlerts(historyService.getAllAlertEvents(),"");
        modelAndView.addObject("alertEvents",alertEvents);
        return modelAndView;
    }


    @GetMapping("/{period}")
    public ModelAndView historiqueByPeriod(@PathVariable("period") String period) {


        ModelAndView modelAndView = new ModelAndView(VIEW_HISTORIQUE);
        List<AlertEvent> alertEvents= filterAlerts(historyService.getAllAlertEvents(),period);
        modelAndView.addObject("alertEvents",alertEvents);
        modelAndView.addObject("period",period);
        return modelAndView;
    }

    private  final List<AlertEvent> filterAlerts(List<AlertEvent> alerts , String period){



        LocalDateTime now = LocalDateTime.now().withHour(0);
        Long periodParam=(long)1;
        List<AlertEvent> alertEventsFiltered;
        LocalDateTime finalLimitPeriod;



            switch (period) {
                case ONE_WEEK:
                    finalLimitPeriod = now.minusWeeks(periodParam);
                    break;
                case ONE_DAY:
                    finalLimitPeriod = now.minusDays(periodParam);;
                    break;
                case ONE_MONTH:
                    finalLimitPeriod = now.minusMonths(periodParam);
                    break;
                default:

                    finalLimitPeriod = now.minusMonths(parseInt(properties.getPeriod()));

            }

        alertEventsFiltered = alerts.stream().filter(a -> a.getStart().isAfter(finalLimitPeriod)).collect(Collectors.toList());
        alertEventsFiltered.sort((c1, c2) -> c2.getStart().compareTo(c1.getStart()));

        return alertEventsFiltered;

    }


}
