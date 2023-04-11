package fr.real.supervision.appliinfo.web.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessController {

	@Value("${info.app.version}")
     String applicationVersion;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("version",applicationVersion);
		return "login";
	}

	@GetMapping("/access-denied")
	public String accessDenied(Model model) {
		model.addAttribute("version",applicationVersion);
		return "access-denied";
	}
}