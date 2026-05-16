package com.jcaponong.recipeapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcaponong.recipeapi.repository.IngredientRepository;
import com.jcaponong.recipeapi.repository.RecipeRepository;
import com.jcaponong.recipeapi.repository.TagRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    void shouldCreateRecipe() throws Exception {
        mockMvc.perform(post("/v0/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tomatoPastaRequest()))
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

    @Test
    void shouldGetRecipeById() throws Exception {
        String recipeId = createRecipe(tomatoPastaRequest());

        mockMvc.perform(get("/v0/recipes/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId))
                .andExpect(jsonPath("$.title").value("Tomato Pasta"))
                .andExpect(jsonPath("$.ingredients[0].ingredientName").value("cherry tomatoes"))
                .andExpect(jsonPath("$.instructions[0].instruction").value("Cook the pasta."))
                .andExpect(jsonPath("$.tags[0].name").value("quick dinner"));
    }

    @Test
    void shouldListRecipes() throws Exception {
        String recipeId = createRecipe(tomatoPastaRequest());

        mockMvc.perform(get("/v0/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recipeId))
                .andExpect(jsonPath("$[0].title").value("Tomato Pasta"))
                .andExpect(jsonPath("$[0].ingredients[0].ingredientName").value("cherry tomatoes"))
                .andExpect(jsonPath("$[0].instructions[0].instruction").value("Cook the pasta."))
                .andExpect(jsonPath("$[0].tags[0].name").value("quick dinner"));
    }

    @Test
    void shouldUpdateRecipe() throws Exception {
        String recipeId = createRecipe(tomatoPastaRequest());

        mockMvc.perform(put("/v0/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vegetableSoupRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(recipeId))
                .andExpect(jsonPath("$.title").value("Vegetable Soup"))
                .andExpect(jsonPath("$.description").value("Updated soup"))
                .andExpect(jsonPath("$.servingsMin").value(4))
                .andExpect(jsonPath("$.servingsMax").value(6))
                .andExpect(jsonPath("$.vegetarian").value(true))
                .andExpect(jsonPath("$.ingredients.length()").value(1))
                .andExpect(jsonPath("$.ingredients[0].ingredientName").value("carrots"))
                .andExpect(jsonPath("$.instructions.length()").value(1))
                .andExpect(jsonPath("$.instructions[0].instruction").value("Simmer until tender."))
                .andExpect(jsonPath("$.tags.length()").value(1))
                .andExpect(jsonPath("$.tags[0].name").value("comfort food"));

        assertThat(ingredientRepository.findByNormalizedName("carrots")).isPresent();
        assertThat(tagRepository.findByNormalizedName("comfort food")).isPresent();
    }

    @Test
    void shouldDeleteRecipe() throws Exception {
        String recipeId = createRecipe(tomatoPastaRequest());

        mockMvc.perform(delete("/v0/recipes/{id}", recipeId))
                .andExpect(status().isNoContent());

        assertThat(recipeRepository.findByIdAndDeletedAtIsNull(UUID.fromString(recipeId))).isEmpty();
    }

    @Test
    void shouldNotReturnDeletedRecipeById() throws Exception {
        String recipeId = createRecipe(tomatoPastaRequest());

        mockMvc.perform(delete("/v0/recipes/{id}", recipeId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v0/recipes/{id}", recipeId))
                .andExpect(status().isNotFound());
    }

    private String createRecipe(String requestBody) throws Exception {
        String responseBody = mockMvc.perform(post("/v0/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode response = objectMapper.readTree(responseBody);
        return response.get("id").asText();
    }

    private String tomatoPastaRequest() {
        return """
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
    }

    private String vegetableSoupRequest() {
        return """
                {
                  "title": "Vegetable Soup",
                  "description": "Updated soup",
                  "servingsMin": 4,
                  "servingsMax": 6,
                  "vegetarian": true,
                  "ingredients": [
                    {
                      "ingredientName": "  Carrots  ",
                      "displayText": "3 carrots",
                      "quantity": 3,
                      "unit": "pieces",
                      "preparationNote": "sliced",
                      "optionalIngredient": false,
                      "position": 1
                    }
                  ],
                  "instructions": [
                    {
                      "stepNumber": 1,
                      "instruction": "Simmer until tender.",
                      "durationSeconds": 1200
                    }
                  ],
                  "tags": [" Comfort   Food "]
                }
                """;
    }
}
