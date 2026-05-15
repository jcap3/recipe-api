package com.jcaponong.recipeapi.dto;

import java.math.BigDecimal;

public record RecipeIngredientRequest(
        String ingredientName,
        String displayText,
        BigDecimal quantity,
        String unit,
        String preparationNote,
        Boolean optionalIngredient,
        Integer position
) {
}
