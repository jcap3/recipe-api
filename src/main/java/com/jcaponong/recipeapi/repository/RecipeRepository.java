package com.jcaponong.recipeapi.repository;

import com.jcaponong.recipeapi.entity.Recipe;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    Optional<Recipe> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByTitleIgnoreCaseAndDeletedAtIsNull(String title);
}
