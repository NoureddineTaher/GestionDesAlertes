package fr.real.supervision.appliinfo.connector.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Configuration
public class MailTemplateConfig {

	public static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

	@Value("${email.template.path:src/main/resources/templates/mail/}")
	private String emailTemplatePath;

	@Bean
	public TemplateEngine emailTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(bodyTemplateResolver());
		templateEngine.addTemplateResolver(subjectTemplateResolver());
		templateEngine.addTemplateResolver(htmlTemplateResolver());

		return templateEngine;
	}
	@Bean
	public ClassLoaderTemplateResolver htmlTemplateResolver() {

		ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
		emailTemplateResolver.setPrefix("/templates/");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
		emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		return emailTemplateResolver;
	}

	@Bean
	public IDialect conditionalCommentDialect() {
		return new Java8TimeDialect();
	}

	private ITemplateResolver bodyTemplateResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();

		templateResolver.setOrder(Integer.valueOf(2));
		templateResolver.setResolvablePatterns(Collections.singleton("*Body*"));
		templateResolver.setPrefix(emailTemplatePath + "body/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	private ITemplateResolver subjectTemplateResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();

		templateResolver.setOrder(Integer.valueOf(2));
		templateResolver.setResolvablePatterns(Collections.singleton("*Subject*"));
		templateResolver.setPrefix(emailTemplatePath + "subject/");
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

}
