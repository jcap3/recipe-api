package com.jcaponong.recipeapi.repository;

import com.jcaponong.recipeapi.entity.Recipe;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeRepository extends JpaRepository<Recipe, UUID>, JpaSpecificationExecutor<Recipe> {

    List<Recipe> findAllByDeletedAtIsNull();

    Optional<Recipe> findByIdAndDeletedAtIsNull(UUID id);
}
