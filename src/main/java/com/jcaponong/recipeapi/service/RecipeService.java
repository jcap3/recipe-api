package com.jcaponong.recipeapi.service;

import com.jcaponong.recipeapi.dto.RecipeCreateRequest;
import com.jcaponong.recipeapi.dto.RecipeIngredientRequest;
import com.jcaponong.recipeapi.dto.RecipeInstructionRequest;
import com.jcaponong.recipeapi.dto.RecipeResponse;
import com.jcaponong.recipeapi.entity.Ingredient;
import com.jcaponong.recipeapi.entity.Recipe;
import com.jcaponong.recipeapi.entity.RecipeIngredient;
import com.jcaponong.recipeapi.entity.RecipeInstruction;
import com.jcaponong.recipeapi.entity.Tag;
import com.jcaponong.recipeapi.entity.TagType;
import com.jcaponong.recipeapi.mapper.RecipeMapper;
import com.jcaponong.recipeapi.repository.IngredientRepository;
import com.jcaponong.recipeapi.repository.RecipeRepository;
import com.jcaponong.recipeapi.repository.TagRepository;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final TagRepository tagRepository;
    private final RecipeMapper recipeMapper;

    @Transactional
    public RecipeResponse createRecipe(RecipeCreateRequest request) {
        Recipe recipe = recipeMapper.toEntity(request);
        addIngredients(recipe, request.ingredients());
        addInstructions(recipe, request.instructions());
        addTags(recipe, request.tags());

        Recipe savedRecipe = recipeRepository.save(recipe);
        return recipeMapper.toResponse(savedRecipe);
    }

    private void addIngredients(Recipe recipe, List<RecipeIngredientRequest> ingredientRequests) {
        if (ObjectUtils.isEmpty(ingredientRequests)) {
            return;
        }

        for (int index = 0; index < ingredientRequests.size(); index++) {
            RecipeIngredientRequest request = ingredientRequests.get(index);
            String normalizedName = normalizeName(request.ingredientName());
            Ingredient ingredient = ingredientRepository.findByNormalizedName(normalizedName)
                    .orElseGet(() -> createIngredient(normalizedName));

            RecipeIngredient recipeIngredient = RecipeIngredient.create();
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setDisplayText(request.displayText());
            recipeIngredient.setQuantity(request.quantity());
            recipeIngredient.setUnit(request.unit());
            recipeIngredient.setPreparationNote(request.preparationNote());
            recipeIngredient.setOptionalIngredient(Boolean.TRUE.equals(request.isOptionalIngredient()));
            recipeIngredient.setPosition(!ObjectUtils.isEmpty(request.position()) ? request.position() : index + 1);

            recipe.getRecipeIngredients().add(recipeIngredient);
        }
    }

    private Ingredient createIngredient(String normalizedName) {
        Ingredient ingredient = Ingredient.create();
        ingredient.setName(normalizedName);
        ingredient.setNormalizedName(normalizedName);
        return ingredientRepository.save(ingredient);
    }

    private void addInstructions(Recipe recipe, List<RecipeInstructionRequest> instructionRequests) {
        if (ObjectUtils.isEmpty(instructionRequests)) {
            return;
        }

        for (int index = 0; index < instructionRequests.size(); index++) {
            RecipeInstructionRequest request = instructionRequests.get(index);
            RecipeInstruction instruction = RecipeInstruction.create();
            instruction.setRecipe(recipe);
            instruction.setStepNumber(!ObjectUtils.isEmpty(request.stepNumber()) ? request.stepNumber() : index + 1);
            instruction.setInstruction(request.instruction());
            instruction.setDurationSeconds(request.durationSeconds());

            recipe.getInstructions().add(instruction);
        }
    }

    private void addTags(Recipe recipe, Set<String> tagNames) {
        if (CollectionUtils.isEmpty(tagNames)) {
            return;
        }

        for (String tagName : tagNames) {
            String normalizedName = normalizeName(tagName);
            Tag tag = tagRepository.findByNormalizedName(normalizedName)
                    .orElseGet(() -> createTag(normalizedName));
            recipe.getTags().add(tag);
        }
    }

    private Tag createTag(String normalizedName) {
        Tag tag = Tag.create();
        tag.setName(normalizedName);
        tag.setNormalizedName(normalizedName);
        tag.setType(TagType.GENERAL);
        return tagRepository.save(tag);
    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
