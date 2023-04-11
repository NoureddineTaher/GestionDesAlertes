package fr.real.supervision.appliinfo.connector.mail;

import fr.real.supervision.appliinfo.exception.PlatformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class MailClient {

	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	public MailClient(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}


	public void prepareAndSend(String from, String[] recipients, String subject, String message) throws PlatformException {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			 MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(from);
			messageHelper.setTo(recipients);
			messageHelper.setSubject(subject);
			messageHelper.setText(message, true);
		};
        try {
		mailSender.send(messagePreparator);
        } catch (MailException e) {
            // runtime exception; compiler will not force you to handle it
        	throw new PlatformException("Erreur lors de l'envoi du mail", e);
        }
	}



}