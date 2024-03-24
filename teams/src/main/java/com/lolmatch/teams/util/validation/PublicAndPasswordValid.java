package com.lolmatch.teams.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PublicAndPasswordValidator.class)
@Documented
public @interface PublicAndPasswordValid {
	
	String message() default "Podane hasła są różne";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
