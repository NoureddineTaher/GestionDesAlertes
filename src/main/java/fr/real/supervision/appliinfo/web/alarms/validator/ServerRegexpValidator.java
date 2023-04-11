package fr.real.supervision.appliinfo.web.alarms.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServerRegexpValidator implements ConstraintValidator<ServerRegexpConstraint, String> {

	@Override
	public void initialize(ServerRegexpConstraint contactNumber) {
		// Rien à initialiser
	}

	@Override
	public boolean isValid(String serverRegexp, ConstraintValidatorContext cxt) {
		boolean result = false;
		try {
			Pattern.compile(serverRegexp);
			result = true;
		} catch (Exception e) {
			result = false;
		}

		return result;
	}

}