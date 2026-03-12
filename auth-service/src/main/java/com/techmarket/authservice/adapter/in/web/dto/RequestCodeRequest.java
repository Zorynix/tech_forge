package com.techmarket.authservice.adapter.in.web.dto;

import jakarta.validation.constraints.AssertTrue;

public record RequestCodeRequest(
        String phone,
        String email
) {
    @AssertTrue(message = "Either phone or email must be provided")
    public boolean isValid() {
        return (phone != null && !phone.isBlank()) || (email != null && !email.isBlank());
    }
}
