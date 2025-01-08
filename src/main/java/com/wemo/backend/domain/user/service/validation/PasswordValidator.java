package com.wemo.backend.domain.user.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < 8 || password.length() > 20) {
            return false;
        }

        int count = 0;
        if (password.matches(".*[a-zA-Z].*")) count++;
        if (password.matches(".*[0-9].*")) count++;
        if (password.matches(".*[!@#$%^&*].*")) count++;

        return count >= 2;
    }
}

