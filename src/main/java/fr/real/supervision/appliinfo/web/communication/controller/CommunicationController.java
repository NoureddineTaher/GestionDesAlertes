package fr.real.supervision.appliinfo.web.communication.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.real.supervision.appliinfo.connector.mail.MailClient;
import fr.real.supervision.appliinfo.connector.sms.SmsClient;
import fr.real.supervision.appliinfo.connector.sms.SmsException;
import fr.real.supervision.appliinfo.exception.PlatformException;
import fr.real.supervision.appliinfo.model.Contact;
import fr.real.supervision.appliinfo.web.communication.dto.CommunicationDto;
import fr.real.supervision.appliinfo.web.diffusiongroup.service.DiffusionGroupService;

@Controller
@RequestMapping("/communication")
public class CommunicationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationController.class);

	private static final String VIEW_COMMUNICATION = "communication/communication";

	private static final String ATTRIBUTE_COMMUNICATION = "communicationDto";
	private static final String ATTRIBUTE_LIST_DIFFUSIONGROUPS = "listDiffusionGroups";
	private static final String ATTRIBUTE_PROCESS_DONE = "processDone";
	private static final String ATTRIBUTE_MESSAGE_MAIL = "messageMail";
	private static final String ATTRIBUTE_MESSAGE_SMS = "messageSMS";
	private static final String ATTRIBUTE_SUCCESS_MAIL = "successMail";
	private static final String ATTRIBUTE_SUCCESS_SMS = "successSMS";

	private static final String REDIRECT_COMMUNICATION = "redirect:/communication";

	@Autowired
	private DiffusionGroupService diffusionGroupService;

	@Autowired
	private SmsClient smsClient;

	@Autowired
	private MailClient mailClient;

	@Value("${info.app.version}")
	String applicationVersion;

	private ModelAndView createModelAndView(CommunicationDto communicationDto) {
		ModelAndView modelAndView = new ModelAndView(VIEW_COMMUNICATION);

		modelAndView.addObject(ATTRIBUTE_LIST_DIFFUSIONGROUPS, diffusionGroupService.getDiffusionGroups());
		modelAndView.addObject(ATTRIBUTE_COMMUNICATION, communicationDto);
		modelAndView.addObject("version", applicationVersion);

		return modelAndView;
	}

	@GetMapping("")
	public ModelAndView index() {
		CommunicationDto communicationDto = new CommunicationDto();
		communicationDto.setDiffusionGroups(new ArrayList<>());

		return createModelAndView(communicationDto);
	}

	@PostMapping("/send")
	public ModelAndView postSms(
			@Valid @ModelAttribute(value = ATTRIBUTE_COMMUNICATION) CommunicationDto communicationDto,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return createModelAndView(communicationDto);
		}

		List<Contact> contacts = communicationDto.getDiffusionGroups().stream()
				.flatMap(diffusionGroup -> diffusionGroup.getContacts().stream()).distinct()
				.collect(Collectors.toList());

		if (contacts.isEmpty()) {
			result.rejectValue("diffusionGroups", "diffusionGroups.notvalid",
					"La liste de diffusion ne peut pas être vide");
		} else if (Boolean.FALSE.equals(communicationDto.getSendSMS()) && !communicationDto.getSendMail()) {
			result.rejectValue("sendSMS", "sendSMS.notvalid", "Merci de sélectionner au moins une option d'envoi");
			result.rejectValue("sendMail", "sendMail.notvalid", "Merci de sélectionner au moins une option d'envoi");
		} else if (Boolean.TRUE.equals(communicationDto.getSendSMS()) && communicationDto.getSmsContent().isEmpty()) {
			result.rejectValue("smsContent", "smsContent.notvalid", "Le contenu du sms ne peut pas être vide");
		} else if (Boolean.TRUE.equals(communicationDto.getSendMail()) && communicationDto.getEmailObject().isEmpty()) {
			result.rejectValue("emailObject", "emailObject.notvalid", "Le titre du mail ne peut pas être vide");
		} else if (Boolean.TRUE.equals(communicationDto.getSendMail())
				&& communicationDto.getEmailContent().isEmpty()) {
			result.rejectValue("emailContent", "emailContent.notvalid", "Le contenu du mail ne peut pas être vide");
		} else {
			boolean successSMS = false;
			boolean successMail = false;

			if (Boolean.TRUE.equals(communicationDto.getSendSMS())) {
				String[] phones = contacts.stream().map(Contact::getPhone).toArray(String[]::new);

				try {
					smsClient.send(phones, communicationDto.getSmsContent());
					successSMS = true;
				} catch (SmsException e) {
					LOGGER.error("Erreur lors de la communication d'un sms : {}", e.getMessage());
					successSMS = false;
				}
			}

			if (Boolean.TRUE.equals(communicationDto.getSendMail())) {
				String[] emailAdresses = contacts.stream().map(Contact::getEmail).toArray(String[]::new);

				try {
					mailClient.prepareAndSend("appliinfo@intranet-adsn.fr", emailAdresses,
							communicationDto.getEmailObject(), communicationDto.getEmailContent());
					successMail = true;
				} catch (PlatformException e) {
					LOGGER.error("Erreur lors de la communication d'un mail : {}", e.getMessage());
					successMail = false;
				}
			}

			String messageSMS = "";
			String messageMail = "";

			if (Boolean.TRUE.equals(communicationDto.getSendSMS())) {
				messageSMS = (successSMS ? "L'envoi du SMS a fonctionné" : "L'envoi du SMS n'a pas fonctionné");
			}

			if (Boolean.TRUE.equals(communicationDto.getSendMail())) {
				messageMail = (successMail ? "L'envoi du mail a fonctionné" : "L'envoi du mail n'a pas fonctionné");
			}

			ModelAndView modelAndView;

			if (communicationDto.getSendSMS() == successSMS && communicationDto.getSendMail() == successMail) {
				modelAndView = new ModelAndView(REDIRECT_COMMUNICATION);

				redirectAttributes.addFlashAttribute(ATTRIBUTE_PROCESS_DONE, true);
				redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGE_SMS, messageSMS);
				redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGE_MAIL, messageMail);
				redirectAttributes.addFlashAttribute(ATTRIBUTE_SUCCESS_SMS, successSMS);
				redirectAttributes.addFlashAttribute(ATTRIBUTE_SUCCESS_MAIL, successMail);

				redirectAttributes.addFlashAttribute("error", false);

			} else {
				modelAndView = new ModelAndView(VIEW_COMMUNICATION);

				modelAndView.addObject(ATTRIBUTE_LIST_DIFFUSIONGROUPS, diffusionGroupService.getDiffusionGroups());
				modelAndView.addObject(ATTRIBUTE_PROCESS_DONE, "true");
				modelAndView.addObject(ATTRIBUTE_MESSAGE_SMS, messageSMS);
				modelAndView.addObject(ATTRIBUTE_MESSAGE_MAIL, messageMail);
				modelAndView.addObject(ATTRIBUTE_SUCCESS_SMS, successSMS);
				modelAndView.addObject(ATTRIBUTE_SUCCESS_MAIL, successMail);
				modelAndView.addObject("error", true);

			}
			return modelAndView;
		}

		ModelAndView modelAndView = new ModelAndView(VIEW_COMMUNICATION);

		modelAndView.addObject(ATTRIBUTE_LIST_DIFFUSIONGROUPS, diffusionGroupService.getDiffusionGroups());
		modelAndView.addObject(ATTRIBUTE_PROCESS_DONE, false);

		return modelAndView;

	}

}
