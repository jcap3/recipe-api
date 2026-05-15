package com.jcaponong.recipeapi.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RecipeIngredientResponse(
        UUID id,
        String ingredientName,
        String displayText,
        BigDecimal quantity,
        String unit,
        String preparationNote,
        boolean optionalIngredient,
        Integer position
) {
}
