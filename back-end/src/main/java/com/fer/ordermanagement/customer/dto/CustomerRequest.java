package com.fer.ordermanagement.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CustomerRequest {
    @NotBlank(message = "Full name must not be blank!")
    @Size(max = 120)
    String fullName;

    @Pattern(regexp = "^(0[0-9]{9})$", message = "Phone number is invalid")
    String phone;

    @NotBlank(message = "Email must not be blank!")
    @Email
    @Size(max = 120)
    String email;

    @NotBlank(message = "Address must not be blank!")
    @Size(max = 255)
    String address;
}
