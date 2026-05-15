package com.jcaponong.recipeapi.mapper;

import com.jcaponong.recipeapi.dto.RecipeCreateRequest;
import com.jcaponong.recipeapi.dto.RecipeIngredientResponse;
import com.jcaponong.recipeapi.dto.RecipeInstructionResponse;
import com.jcaponong.recipeapi.dto.RecipeResponse;
import com.jcaponong.recipeapi.dto.TagResponse;
import com.jcaponong.recipeapi.entity.Recipe;
import com.jcaponong.recipeapi.entity.RecipeIngredient;
import com.jcaponong.recipeapi.entity.RecipeInstruction;
import com.jcaponong.recipeapi.entity.Tag;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeMapper {

    public Recipe toEntity(RecipeCreateRequest request) {
        Recipe recipe = Recipe.create();
        recipe.setTitle(request.title());
        recipe.setDescription(request.description());
        recipe.setServingsMin(request.servingsMin());
        recipe.setServingsMax(request.servingsMax());
        recipe.setVegetarian(Boolean.TRUE.equals(request.vegetarian()));
        return recipe;
    }

    public RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getServingsMin(),
                recipe.getServingsMax(),
                recipe.isVegetarian(),
                recipe.getStatus(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt(),
                mapIngredients(recipe),
                mapInstructions(recipe),
                mapTags(recipe)
        );
    }

    private List<RecipeIngredientResponse> mapIngredients(Recipe recipe) {
        return recipe.getRecipeIngredients().stream()
                .sorted(Comparator.comparing(RecipeIngredient::getPosition))
                .map(recipeIngredient -> new RecipeIngredientResponse(
                        recipeIngredient.getId(),
                        recipeIngredient.getIngredient().getName(),
                        recipeIngredient.getDisplayText(),
                        recipeIngredient.getQuantity(),
                        recipeIngredient.getUnit(),
                        recipeIngredient.getPreparationNote(),
                        recipeIngredient.isOptionalIngredient(),
                        recipeIngredient.getPosition()
                ))
                .toList();
    }

    private List<RecipeInstructionResponse> mapInstructions(Recipe recipe) {
        return recipe.getInstructions().stream()
                .sorted(Comparator.comparing(RecipeInstruction::getStepNumber))
                .map(instruction -> new RecipeInstructionResponse(
                        instruction.getId(),
                        instruction.getStepNumber(),
                        instruction.getInstruction(),
                        instruction.getDurationSeconds()
                ))
                .toList();
    }

    private Set<TagResponse> mapTags(Recipe recipe) {
        return recipe.getTags().stream()
                .sorted(Comparator.comparing(Tag::getNormalizedName))
                .map(tag -> new TagResponse(
                        tag.getId(),
                        tag.getName(),
                        tag.getNormalizedName(),
                        tag.getType()
                ))
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
    }
}
