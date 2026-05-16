package com.jcaponong.recipeapi.util;

import java.util.Locale;

public final class NameNormalizer {

    private NameNormalizer() {
    }

    public static String normalizeName(String name) {
        return name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
