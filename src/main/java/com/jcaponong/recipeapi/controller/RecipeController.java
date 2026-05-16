package com.jcaponong.recipeapi.controller;

import com.jcaponong.recipeapi.dto.RecipeCreateRequest;
import com.jcaponong.recipeapi.dto.RecipeResponse;
import com.jcaponong.recipeapi.dto.RecipeUpdateRequest;
import com.jcaponong.recipeapi.service.RecipeService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;

@RestController
@RequestMapping("/v0/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@RequestBody RecipeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.createRecipe(request));
    }

    @GetMapping
    public List<RecipeResponse> getRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/search")
    public List<RecipeResponse> searchRecipes(
            @RequestParam(required = false) Boolean vegetarian,
            @RequestParam(required = false) Integer servings,
            @RequestParam(required = false) String includeIngredients,
            @RequestParam(required = false) String excludeIngredients,
            @RequestParam(required = false) String instructionContains
    ) {
        return recipeService.searchRecipes(
                vegetarian,
                servings,
                parseCommaSeparated(includeIngredients),
                parseCommaSeparated(excludeIngredients),
                instructionContains
        );
    }

    @GetMapping("/{id}")
    public RecipeResponse getRecipe(@PathVariable UUID id) {
        return recipeService.getRecipe(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable UUID id,
            @RequestBody RecipeUpdateRequest request
    ) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable UUID id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    private List<String> parseCommaSeparated(String value) {
        if (!StringUtils.hasText(value)) {
            return List.of();
        }

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
    }
}
