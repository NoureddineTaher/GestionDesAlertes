package fr.real.supervision.appliinfo.web.alarms.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ServerRegexpValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerRegexpConstraint {
	String message() default "Invalid server regexp";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}