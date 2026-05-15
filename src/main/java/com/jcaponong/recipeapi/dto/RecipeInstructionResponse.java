package com.jcaponong.recipeapi.dto;

import java.util.UUID;

public record RecipeInstructionResponse(
        UUID id,
        Integer stepNumber,
        String instruction,
        Integer durationSeconds
) {
}
