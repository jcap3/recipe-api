package com.jcaponong.recipeapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

public record RecipeUpdateRequest(

        @NotBlank
        String title,
        String description,

        @NotNull
        @Positive
        Integer servingsMin,

        @NotNull
        @Positive
        Integer servingsMax,

        Boolean vegetarian,

        @NotEmpty
        List<@Valid RecipeIngredientRequest> ingredients,

        @NotEmpty
        List<@Valid RecipeInstructionRequest> instructions,

        Set<String> tags
) {
    @AssertTrue(message = "servingsMax must be greater than or equal to servingsMin")
    public boolean isServingsRangeValid() {
        return servingsMin == null || servingsMax == null || servingsMax >= servingsMin;
    }
}
