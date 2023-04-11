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
public class AstreinteTemplateConfig {

	public static final String SMS_TEMPLATE_ENCODING = "UTF-8";

	@Value("${astreinte.template.path:src/main/resources/templates/astreinte/}")
	private String astreinteTemplatePath;

	@Bean
	public TemplateEngine astreinteTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(astreinteTemplateResolver());
		return templateEngine;
	}
	
	private ITemplateResolver astreinteTemplateResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();

		templateResolver.setOrder(Integer.valueOf(2));
		templateResolver.setResolvablePatterns(Collections.singleton("*"));
		templateResolver.setPrefix(astreinteTemplatePath);
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding(SMS_TEMPLATE_ENCODING);
		templateResolver.setCacheable(false);
		return templateResolver;
	}	
	
}
