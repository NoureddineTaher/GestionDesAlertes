package fr.real.supervision.appliinfo.web.incidents.service;

import fr.real.supervision.appliinfo.model.Contact;
import fr.real.supervision.appliinfo.model.Mail;
import fr.real.supervision.appliinfo.repository.ContactRepository;
import fr.real.supervision.appliinfo.repository.DiffusionGroupRepository;
import fr.real.supervision.appliinfo.web.incidents.models.Incident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmailSenderIncidentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderIncidentService.class);
    private final JavaMailSender mailSender;
    private final ContactRepository contactRepository;
    private final DiffusionGroupRepository diffusionGroupRepository;
    private final SpringTemplateEngine templateEngine;

    public EmailSenderIncidentService(ContactRepository contactRepository, DiffusionGroupRepository diffusionGroupRepository, JavaMailSender mailSender, SpringTemplateEngine templateEngine) {

        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.contactRepository = contactRepository;
        this.diffusionGroupRepository = diffusionGroupRepository;

    }

    public void sendEmail(Incident mailIncidentDto) throws MessagingException, MailSendException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Map<String, Object> properties = new HashMap<>();

        Mail mail = Mail.builder().htmlTemplate(new Mail.HtmlTemplate("mailIncident", properties)).build();
        // Recuperer incidentDto
        properties.put("incidentDto", mailIncidentDto);
        // Recuperer description
        if (mailIncidentDto.getDescription() != null) {
            properties.put("descriptions", mailIncidentDto.getDescription().split("\r?\n"));
        } else {
            properties.put("descriptions", new ArrayList<>());
        }

        String[] destinations = mailIncidentDto.getIncidentDestination().split(" ");

        for (String destination : destinations) {

            helper.setTo(destination.trim());

            helper.setFrom("communication-gds@groupeadsn.fr");
            helper.setSubject(mailIncidentDto.getIncidentObjet());
            helper.setCc(prepareDestinationList("@DEX.GDS"));

            String html = getHtmlContent(mail);
            helper.setText(html, true);
            addImageToEmail(helper, "cloture_incident.png");
            addImageToEmail(helper, "declaration-inc.png");
            addImageToEmail(helper, "suivi-inc.png");

            try {
                LOGGER.info("mail from : communication-gds@groupeadsn.fr");
                LOGGER.info("Sujet du mail : {}", mailIncidentDto.getIncidentObjet());
                LOGGER.info("mail to {}", destination.trim());
                mailSender.send(message);
            } catch (MailSendException e) {
                LOGGER.error("Erreur lors de l'envoie du mail : {}", e.getMessage());
            }
        }


    }

    private void addImageToEmail(MimeMessageHelper helper, String image) throws MessagingException {
        helper.addInline(image, new ClassPathResource("static/images/" + image));
    }

    private String[] prepareDestinationList(String toList) {
        List<String> convertedCountriesList = Stream.of(toList.replaceAll("[\\[\\]\\p{Blank}]", "").split(",", -1)).toList();
        Map<Boolean, List<String>> filteredList = convertedCountriesList.stream().collect(Collectors.partitioningBy(ipAddress -> ipAddress.startsWith("@")));
        List<Contact> contactList = contactRepository.findContactsByDiffusionGroupsIn(diffusionGroupRepository.findDiffusionGroupByNameIn(filteredList.get(true)));
        List<String> emailAdresse = new ArrayList<>(contactList.stream().map(Contact::getEmail).toList());
        emailAdresse.addAll(filteredList.get(false));
        return emailAdresse.toArray(new String[0]);
    }

    private String getHtmlContent(Mail mail) {
        Context context = new Context();
        context.setVariables(mail.getHtmlTemplate().getProps());
        return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
    }
}
