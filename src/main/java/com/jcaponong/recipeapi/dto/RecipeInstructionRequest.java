package com.jcaponong.recipeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecipeInstructionRequest(
        @NotNull
        @Positive
        Integer stepNumber,

        @NotBlank
        String instruction,

        @Positive
        Integer durationSeconds
) {
}
