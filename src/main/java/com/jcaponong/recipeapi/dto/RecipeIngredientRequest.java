package com.jcaponong.recipeapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RecipeIngredientRequest(
        @NotBlank
        String ingredientName,

        @NotBlank
        String displayText,

        @Positive
        BigDecimal quantity,

        String unit,

        String preparationNote,

        Boolean isOptionalIngredient,

        @NotNull
        @Positive
        Integer position
) {
}
