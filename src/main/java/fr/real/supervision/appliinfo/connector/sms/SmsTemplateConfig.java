package fr.real.supervision.appliinfo.connector.sms;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class SmsTemplateConfig {

	public static final String SMS_TEMPLATE_ENCODING = "UTF-8";

	@Value("${sms.template.path:src/main/resources/templates/sms/}")
	private String smsTemplatePath;

	@Bean
	public TemplateEngine smsTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(smsTemplateResolver());
		return templateEngine;
	}
	private ITemplateResolver smsTemplateResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();

		templateResolver.setOrder(Integer.valueOf(2));
		templateResolver.setResolvablePatterns(Collections.singleton("*"));
		templateResolver.setPrefix(smsTemplatePath);
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding(SMS_TEMPLATE_ENCODING);
		templateResolver.setCacheable(false);
		return templateResolver;
	}	
	
}
