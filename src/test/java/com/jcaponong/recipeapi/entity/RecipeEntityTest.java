package com.jcaponong.recipeapi.entity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RecipeEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldPersistRecipe() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Tomato Pasta");
        recipe.setDescription("Simple pasta");
        recipe.setServingsMin(2);
        recipe.setServingsMax(4);
        recipe.setVegetarian(true);
        recipe.setStatus(RecipeStatus.DRAFT);

        Recipe saved = entityManager.persistAndFlush(recipe);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }
}