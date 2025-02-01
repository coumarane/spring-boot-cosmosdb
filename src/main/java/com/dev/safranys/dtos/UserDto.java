package com.dev.safranys.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UserDto(
        @NotBlank
        String email,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @PositiveOrZero
        int age,

        @NotBlank
        String city
) {}

