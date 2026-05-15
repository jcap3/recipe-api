package com.jcaponong.recipeapi.controller;

import com.jcaponong.recipeapi.dto.RecipeCreateRequest;
import com.jcaponong.recipeapi.dto.RecipeResponse;
import com.jcaponong.recipeapi.service.RecipeService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public RecipeResponse getRecipe(@PathVariable UUID id) {
        return recipeService.getRecipe(id);
    }
}
