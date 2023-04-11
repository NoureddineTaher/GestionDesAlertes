package fr.real.supervision.appliinfo.web.status.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import fr.real.supervision.appliinfo.web.status.config.Ticket;
import fr.real.supervision.appliinfo.web.status.config.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import fr.real.supervision.appliinfo.connector.itm.ItmAlarm;
import fr.real.supervision.appliinfo.connector.itm.ItmAlarmTicket;
import fr.real.supervision.appliinfo.connector.itm.ItmClient;
import fr.real.supervision.appliinfo.connector.itm.ItmException;
import fr.real.supervision.appliinfo.web.status.config.MeteoStatusConfigProps;
import fr.real.supervision.appliinfo.web.status.config.MeteoStatusDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static fr.real.supervision.appliinfo.web.util.ToolStatusAndSeverity.toolStatusAndSeverity;

@Service
@RequiredArgsConstructor
@Getter
public class MeteoStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeteoStatusService.class);
    private final String[] adsnDomains = {"Applications Métier", "Infrastructure de confiance", "Applications Réseau"};
    private final MeteoStatusConfigProps config;
    private final ItmClient itmClient;

    public List<MeteoStatusDto> getData() {
        Map<String, MeteoStatusDto> statuses = this.config.getFlattenedDatas();
        try {
            Map<String, ItmAlarm> alarms = itmClient.fetchAlarms().getAlarms();
            Set<String> alarmsKeys = alarms.keySet();

            alarmsKeys.forEach(key -> {
                if (statuses.containsKey(key)) {
                    LOGGER.info(String.format("Update ticket %s with severity %s", key, alarms.get(key).getSeverite()));
                    statuses.get(key).getTicket().setSeverity(alarms.get(key).getSeverite());
                    statuses.get(key).getTicket().setStatus(alarms.get(key).getStatus());
                }
            });

			Map<String, ItmAlarmTicket> tickets = itmClient.fetchTickets().getAlarmItmAlarmTickets();
			Set<String> ticketsKeys = tickets.keySet();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
			ticketsKeys.forEach(key -> {
                if (statuses.containsKey(key)) {
					LOGGER.info(String.format("Update ticket %s with status %s and reference %s", key,
							tickets.get(key).getStatus(), tickets.get(key).getValue()));
                    statuses.get(key).getTicket().setStatus(tickets.get(key).getStatus());
					statuses.get(key).getTicket().setReference(tickets.get(key).getValue());

					if (tickets.get(key).getDate()!= null) {
						statuses.get(key).getTicket().setDatePssoft(LocalDateTime.parse((tickets.get(key).getDate()), format));
                        }

                    LOGGER.info(statuses.get(key).toString());
                }

            });

        } catch (DOMException | ItmException | TransformerException | ParserConfigurationException | SAXException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new ArrayList<>(statuses.values());
    }

    public List<HashMap<HashMap<String, ItmAlarmTicket>, List<MeteoStatusDto>>> getMeteoByThree(String domain) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        List<HashMap<HashMap<String, ItmAlarmTicket>, List<MeteoStatusDto>>> statusByToolAndSeverityListByThree = new ArrayList<>();


        String[] domains;
        if (domain.equals("ADSN")) {
            domains = this.adsnDomains;
        } else {
            domains = new String[]{domain};
        }

        for (String domainItem : domains) {
            LinkedHashMap<HashMap<String, ItmAlarmTicket>, List<MeteoStatusDto>> statusByToolAndSeverity = new LinkedHashMap<>();
            LinkedHashMap<String, LinkedList<MeteoStatusDto>> statusByTool = setStatusByTool(domainItem);


            int i = 0;
            int groupSize = statusByTool.size() % 3 == 0 ? statusByTool.size() / 3 : statusByTool.size() / 3 + 1;
            int LastGroupSize = statusByTool.size() - (2 * groupSize);
            int numberOfGroups = 0;
            for (Map.Entry<String, LinkedList<MeteoStatusDto>> set : statusByTool.entrySet()) {

                //alimenter les severities de chaue service parent
                HashMap<String, ItmAlarmTicket> toolsByStatusAndSeverity = new HashMap<>();
                ItmAlarmTicket itmAlarmTicket = new ItmAlarmTicket();
                Ticket ticket = new Ticket();
                String toolStatus = toolStatusAndSeverity(set.getValue());
                itmAlarmTicket.setStatus(toolStatus);
                LocalDateTime now = LocalDateTime.now();
                String AlarmReference="";
                //Alimenter le code pssoft le plus recent dans le service parent
                for (MeteoStatusDto meteoStatusDto : set.getValue()) {
                    if (meteoStatusDto.getTicket().getDatePssoft() != null && meteoStatusDto.getTicket().getReference() != null) {
                        if (ticket.getDatePssoft() == null ) {
                            itmAlarmTicket.setValue(meteoStatusDto.getTicket().getReference());
                            ticket.setReference(meteoStatusDto.getTicket().getReference());
                            ticket.setDatePssoft(meteoStatusDto.getTicket().getDatePssoft());
                        } else {
                            String status = meteoStatusDto.getTicket().getStatus();
                            String severity = meteoStatusDto.getTicket().getSeverity();
                            if ( status!= null && severity != null && checkStatusAndseverity(severity,status,itmAlarmTicket.getStatus())) {
                                if(now.isAfter(meteoStatusDto.getTicket().getDatePssoft())){
                                    now=meteoStatusDto.getTicket().getDatePssoft();
                                    AlarmReference=meteoStatusDto.getTicket().getReference();
                                    itmAlarmTicket.setValue(AlarmReference);
                                    itmAlarmTicket.setDate(" Début  "+now.format(dateFormatter));

                                }
                                ticket.setReference(AlarmReference);
                                ticket.setDatePssoft(meteoStatusDto.getTicket().getDatePssoft());




                            }
                        }
                    }
                }



                toolsByStatusAndSeverity.put(set.getKey(), itmAlarmTicket);
                statusByToolAndSeverity.put(toolsByStatusAndSeverity, set.getValue());

                if (!this.check(this.adsnDomains, domainItem)) {
                    i += 1;
                    if (numberOfGroups == 2 && i == LastGroupSize) {
                        statusByToolAndSeverityListByThree.add(statusByToolAndSeverity);
                        break;
                    }

                    if (i == groupSize) {
                        statusByToolAndSeverityListByThree.add(statusByToolAndSeverity);
                        numberOfGroups += 1;
                        statusByToolAndSeverity = new LinkedHashMap<>();
                        i = 0;
                    }
                }

            }
            if (this.check(this.adsnDomains, domainItem)) {
                statusByToolAndSeverityListByThree.add(statusByToolAndSeverity);

            }


        }
        return statusByToolAndSeverityListByThree;


    }

    private boolean check(String[] arr, String toCheckValue) {
        // check if the specified element
        // is present in the array or not
        for (String element : arr) {
            if (Objects.equals(element, toCheckValue)) {
                return true;
            }

        }
        return false;
    }

    private MeteoStatusDto findTicket(Tool tool, Ticket ticket, List<MeteoStatusDto> statuses) {
        for (MeteoStatusDto meteoStatusDto : statuses) {
            if (Objects.equals(meteoStatusDto.getTool(), tool.getName()) && Objects.equals(ticket.getName(), meteoStatusDto.getTicket().getName())) {
                return meteoStatusDto;
            }
        }
        return null;
    }

    private Boolean checkStatusAndseverity(String severity , String status,String applicationSeverity ){
        if ((severity + "_" + status).equals(applicationSeverity)){
            return true;
        }
        return false;
    }

    private LinkedHashMap<String, LinkedList<MeteoStatusDto>> setStatusByTool(String domain) {
        List<MeteoStatusDto> statuses = getData().stream().filter(status -> status.getDomain().equals(domain)).toList();


        List<Tool> test = getConfig().getDomains().stream().filter(a -> a.getName().equals(domain)).toList().get(0).getTools();
        List<String> toBeSorted = new ArrayList(test.size());

        for (Tool value : test) {
            toBeSorted.add(value.getName());
        }

        LinkedHashMap<String, LinkedList<MeteoStatusDto>> statusByTool = new LinkedHashMap<>();

        for (Tool tool : test) {
            LinkedList<MeteoStatusDto> correspondingStauses = new LinkedList<>();


            for (Ticket ticket : tool.getTickets()) {
                MeteoStatusDto meteoStatusDto = findTicket(tool, ticket, statuses);
                if (Objects.nonNull(meteoStatusDto)) {
                    correspondingStauses.add(meteoStatusDto);
                }

            }

            statusByTool.put(tool.getName(), correspondingStauses);
        }
        return statusByTool;
    }

}
