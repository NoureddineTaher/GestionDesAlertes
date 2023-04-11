package fr.real.supervision.appliinfo.web;

import fr.real.supervision.appliinfo.web.BandeauAlerte.service.BandeauTextService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

@ControllerAdvice
@Controller
public class HomeController {

	public static final String HOME_TITLE = "Appli INFO";
	@Value("${info.app.version}")
	String applicationVersion;

	@GetMapping("/")
	public String root(Model model) {
		model.addAttribute("title", HOME_TITLE);
		model.addAttribute("version",applicationVersion);
		return "index";
	}
}