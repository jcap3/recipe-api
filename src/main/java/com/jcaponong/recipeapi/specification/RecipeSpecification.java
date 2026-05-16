package com.jcaponong.recipeapi.specification;

import com.jcaponong.recipeapi.entity.Ingredient;
import com.jcaponong.recipeapi.entity.Recipe;
import com.jcaponong.recipeapi.entity.RecipeIngredient;
import com.jcaponong.recipeapi.entity.RecipeInstruction;
import com.jcaponong.recipeapi.util.NameNormalizer;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.List;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public final class RecipeSpecification {

    private RecipeSpecification() {
    }

    public static Specification<Recipe> notDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<Recipe> hasVegetarian(Boolean vegetarian) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("vegetarian"), vegetarian);
    }

    public static Specification<Recipe> hasServings(Integer servings) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.lessThanOrEqualTo(root.get("servingsMin"), servings),
                criteriaBuilder.greaterThanOrEqualTo(root.get("servingsMax"), servings)
        );
    }

    public static Specification<Recipe> includesIngredients(List<String> ingredientNames) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            List<String> normalizedNames = ingredientNames.stream()
                    .filter(StringUtils::hasText)
                    .map(NameNormalizer::normalizeName)
                    .distinct()
                    .toList();

            if (ObjectUtils.isEmpty(normalizedNames)) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(normalizedNames.stream()
                    .map(normalizedName -> {
                        Join<Recipe, RecipeIngredient> recipeIngredient = root.join("recipeIngredients");
                        Join<RecipeIngredient, Ingredient> ingredient = recipeIngredient.join("ingredient");
                        return criteriaBuilder.equal(ingredient.get("normalizedName"), normalizedName);
                    })
                    .toArray(Predicate[]::new));
        };
    }

    public static Specification<Recipe> excludesIngredients(List<String> ingredientNames) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            List<String> normalizedNames = ingredientNames.stream()
                    .filter(StringUtils::hasText)
                    .map(NameNormalizer::normalizeName)
                    .distinct()
                    .toList();

            if (ObjectUtils.isEmpty(normalizedNames)) {
                return criteriaBuilder.conjunction();
            }

            Subquery<RecipeIngredient> subquery = query.subquery(RecipeIngredient.class);
            Root<RecipeIngredient> recipeIngredient = subquery.from(RecipeIngredient.class);
            Join<RecipeIngredient, Ingredient> ingredient = recipeIngredient.join("ingredient");

            subquery.select(recipeIngredient)
                    .where(
                            criteriaBuilder.equal(recipeIngredient.get("recipe"), root),
                            ingredient.get("normalizedName").in(normalizedNames)
                    );

            return criteriaBuilder.not(criteriaBuilder.exists(subquery));
        };
    }

    public static Specification<Recipe> instructionContains(String text) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            HibernateCriteriaBuilder hibernateCriteriaBuilder = (HibernateCriteriaBuilder) criteriaBuilder;
            Join<Recipe, RecipeInstruction> instruction = root.join("instructions");
            return hibernateCriteriaBuilder.ilike(
                    instruction.get("instruction"),
                    "%" + text.trim() + "%"
            );
        };
    }
}
