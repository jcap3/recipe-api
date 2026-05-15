package com.jcaponong.recipeapi.dto;

public record RecipeInstructionRequest(
        Integer stepNumber,
        String instruction,
        Integer durationSeconds
) {
}
