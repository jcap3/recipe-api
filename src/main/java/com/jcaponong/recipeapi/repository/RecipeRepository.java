package com.jcaponong.recipeapi.repository;

import com.jcaponong.recipeapi.entity.Recipe;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeRepository extends JpaRepository<Recipe, UUID>, JpaSpecificationExecutor<Recipe> {

    Page<Recipe> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Recipe> findByIdAndDeletedAtIsNull(UUID id);
}
