package com.javarush.reviewplatform.util;

public final class Constant {
    private Constant() {
        throw new IllegalStateException("Utility class");
    }
    // ============ URL PATHS =============
    public static final class Path {
        private Path() {}
        public static final String CATEGORIES = "/categories";
        public static final String PRODUCTS = "/products";
        public static final String REVIEWS = "/reviews";
        public static final String API_CATEGORIES = "/api/v1/categories";
    }

    // ============ VIEW NAMES =============
    public static final class View {
        private View() {}
        public static final String MAIN = "main";
    }
}
