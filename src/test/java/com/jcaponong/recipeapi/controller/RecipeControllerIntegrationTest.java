package com.jcaponong.recipeapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaponong.recipeapi.repository.IngredientRepository;
import com.jcaponong.recipeapi.repository.RecipeRepository;
import com.jcaponong.recipeapi.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void shouldCreateRecipe() throws Exception {
        String requestBody = """
                {
                  "title": "Tomato Pasta",
                  "description": "Simple pasta",
                  "servingsMin": 2,
                  "servingsMax": 4,
                  "vegetarian": true,
                  "ingredients": [
                    {
                      "ingredientName": "  Cherry   Tomatoes  ",
                      "displayText": "2 cups cherry tomatoes",
                      "quantity": 2,
                      "unit": "cups",
                      "preparationNote": "halved",
                      "optionalIngredient": false,
                      "position": 1
                    }
                  ],
                  "instructions": [
                    {
                      "stepNumber": 1,
                      "instruction": "Cook the pasta.",
                      "durationSeconds": 600
                    }
                  ],
                  "tags": ["  Quick   Dinner  "]
                }
                """;

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Tomato Pasta"))
                .andExpect(jsonPath("$.vegetarian").value(true))
                .andExpect(jsonPath("$.ingredients[0].ingredientName").value("cherry tomatoes"))
                .andExpect(jsonPath("$.instructions[0].instruction").value("Cook the pasta."))
                .andExpect(jsonPath("$.tags[0].name").value("quick dinner"))
                .andExpect(jsonPath("$.tags[0].normalizedName").value("quick dinner"))
                .andExpect(jsonPath("$.tags[0].type").value("GENERAL"));

        assertThat(recipeRepository.count()).isEqualTo(1);
        assertThat(ingredientRepository.findByNormalizedName("cherry tomatoes")).isPresent();
        assertThat(tagRepository.findByNormalizedName("quick dinner")).isPresent();
    }
}
