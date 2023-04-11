package fr.real.supervision.appliinfo.web.meteo.service;


import fr.real.supervision.appliinfo.model.Contact;
import fr.real.supervision.appliinfo.model.Mail;
import fr.real.supervision.appliinfo.repository.ContactRepository;
import fr.real.supervision.appliinfo.repository.DiffusionGroupRepository;
import fr.real.supervision.appliinfo.web.historiques.Historisation;
import fr.real.supervision.appliinfo.web.historiques.HistorisationRepository;
import fr.real.supervision.appliinfo.web.historiques.HistorisationService;
import fr.real.supervision.appliinfo.web.meteo.dto.MailDto;
import fr.real.supervision.appliinfo.web.meteo.dto.MeteoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

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
public class EmailSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);
    private final JavaMailSender mailSender;

    private ContactRepository contactRepository;

    private final DiffusionGroupRepository diffusionGroupRepository;
    private final SpringTemplateEngine templateEngine;
    private final MeteoService meteoService;
    private final HistorisationRepository historisationRepository;
    private final HistorisationService historisationService;

    public EmailSenderService(ContactRepository contactRepository, DiffusionGroupRepository diffusionGroupRepository, JavaMailSender mailSender, SpringTemplateEngine templateEngine, MeteoService meteoService, HistorisationRepository historisationRepository, HistorisationService historisationService) {

        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.meteoService = meteoService;
        this.contactRepository = contactRepository;
        this.diffusionGroupRepository = diffusionGroupRepository;

        this.historisationRepository = historisationRepository;
        this.historisationService = historisationService;
    }


    public void sendEmail(MailDto mailDto) throws MessagingException,MailSendException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Map<String, Object> properties = new HashMap<>();


        Mail mail = Mail.builder().htmlTemplate(new Mail.HtmlTemplate("mailMeteo", properties)).build();

        MeteoDto meteodto = new MeteoDto();


        meteodto.setSection1(meteoService.getMeteoBySection(1L));
        meteodto.setSection2(meteoService.getMeteoBySection(2L));
        meteodto.setSection3(meteoService.getMeteoBySection(3L));
        meteodto.setSection4(meteoService.getMeteoBySection(4L));
        properties.put("meteodto", meteodto);


        helper.setTo(prepareDestinationList(mailDto.getTo()));
        helper.setFrom("communication-gds@groupeadsn.fr");
        helper.setSubject(mailDto.getSubjectMail());

        String html = getHtmlContent(mail);
        helper.setText(html, true);


        addImageToEmail(helper, "Picto-Rouge.png");
        addImageToEmail(helper, "Picto-Jaune.png");
        addImageToEmail(helper, "Picto-Bleu.png");

        addImageToEmail(helper, "Picto-vert.png");
        addImageToEmail(helper, "Picto-Gris.png");
        addImageToEmail(helper, "Picto-violet.png");
        addImageToEmail(helper, "Picto-Bleu-resized.png");
        addImageToEmail(helper, "Picto-Rouge-resized.png");
        addImageToEmail(helper, "Picto-Jaune-resized.png");

        addImageToEmail(helper, "Picto-vert-resized.png");
        addImageToEmail(helper, "Picto-Gris-resized.png");
        addImageToEmail(helper, "Picto-violet-resized.png");
        addImageToEmail(helper, "legende_agenda.png");
        addImageToEmail(helper, "legende_meteo.png");
        addImageToEmail(helper, "meteo.png");
        addImageToEmail(helper, "img_1.png");

        try {
            LOGGER.info("mail from : {}", mailDto.getFrom());
            LOGGER.info("Sujet du mail : {}", mailDto.getSubjectMail());
            LOGGER.info("mail to {}", mailDto.getTo());
            mailSender.send(message);
            LOGGER.info("mail envoyé avec succès à:  {}", (Object) prepareDestinationList(mailDto.getTo()));

            //enregisterer le mailDto dans la table historisation et recuperer l'id
            Historisation historisation = historisationService.saveHisto(mailDto);
            //enregistrement des sections du mail avec l'id dans Historisation
            if(historisation!=null) {
                historisationService.saveHistoSections(meteodto, historisation.getId());
            }
        } catch (MailSendException e) {
            LOGGER.error("Erreur lors de l'envoie du mail : {}", e.getMessage());
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
