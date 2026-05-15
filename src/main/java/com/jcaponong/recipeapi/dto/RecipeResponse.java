package com.jcaponong.recipeapi.dto;

import com.jcaponong.recipeapi.entity.RecipeStatus;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record RecipeResponse(
        UUID id,
        String title,
        String description,
        Integer servingsMin,
        Integer servingsMax,
        boolean vegetarian,
        RecipeStatus status,
        Instant createdAt,
        Instant updatedAt,
        List<RecipeIngredientResponse> ingredients,
        List<RecipeInstructionResponse> instructions,
        Set<TagResponse> tags
) {
}
