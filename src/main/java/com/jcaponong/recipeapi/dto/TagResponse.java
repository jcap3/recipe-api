package com.jcaponong.recipeapi.dto;

import com.jcaponong.recipeapi.entity.TagType;
import java.util.UUID;

public record TagResponse(
        UUID id,
        String name,
        String normalizedName,
        TagType type
) {
}
