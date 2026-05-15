package com.jcaponong.recipeapi.dto;

import java.util.List;
import java.util.Set;

public record RecipeCreateRequest(
        String title,
        String description,
        Integer servingsMin,
        Integer servingsMax,
        Boolean vegetarian,
        List<RecipeIngredientRequest> ingredients,
        List<RecipeInstructionRequest> instructions,
        Set<String> tags
) {
}
