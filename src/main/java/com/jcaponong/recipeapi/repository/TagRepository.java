package com.jcaponong.recipeapi.repository;

import com.jcaponong.recipeapi.entity.Tag;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    Optional<Tag> findByNormalizedName(String normalizedName);

    boolean existsByNormalizedName(String normalizedName);
}
