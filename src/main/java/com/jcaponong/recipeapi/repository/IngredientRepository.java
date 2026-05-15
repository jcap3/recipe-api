package com.jcaponong.recipeapi.repository;

import com.jcaponong.recipeapi.entity.Ingredient;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    Optional<Ingredient> findByNormalizedName(String normalizedName);

    boolean existsByNormalizedName(String normalizedName);
}
