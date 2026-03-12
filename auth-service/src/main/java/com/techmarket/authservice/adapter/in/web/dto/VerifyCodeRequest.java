package com.techmarket.authservice.adapter.in.web.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyCodeRequest(
        String phone,
        String email,

        @NotBlank(message = "Verification code is required")
        @Pattern(regexp = "^\\d{6}$", message = "Verification code must be 6 digits")
        String code
) {
    @AssertTrue(message = "Either phone or email must be provided")
    public boolean isValid() {
        return (phone != null && !phone.isBlank()) || (email != null && !email.isBlank());
    }

    public String identifier() {
        return (phone != null && !phone.isBlank()) ? phone : email;
    }
}
