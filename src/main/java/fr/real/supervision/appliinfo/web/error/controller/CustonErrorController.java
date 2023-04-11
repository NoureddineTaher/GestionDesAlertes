package fr.real.supervision.appliinfo.web.error.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustonErrorController implements ErrorController {

	private static final String ERROR_ENDPOINT = "/error";

	@GetMapping(value = ERROR_ENDPOINT)
	public String handleError() {
		return "error";
	}

	public String getErrorPath() {
		return ERROR_ENDPOINT;
	}

}
