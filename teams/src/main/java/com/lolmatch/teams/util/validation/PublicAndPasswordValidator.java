package com.lolmatch.teams.util.validation;

import com.lolmatch.teams.team.dto.AddTeamRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PublicAndPasswordValidator implements ConstraintValidator<PublicAndPasswordValid, AddTeamRequest> {
	@Override
	public void initialize(PublicAndPasswordValid constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
	
	@Override
	public boolean isValid(AddTeamRequest input, ConstraintValidatorContext constraintValidatorContext) {
		if (input.isPublic() && !input.password().isBlank()) {
			return false;
		}
		return input.isPublic() || !input.password().isBlank();
	}
}
