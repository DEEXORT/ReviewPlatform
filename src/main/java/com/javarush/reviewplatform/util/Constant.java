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
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String EDIT = "/edit";
        public static final String DELETE = "/delete";
        public static final String UPDATE = "/update";
        public static final String API_CATEGORIES = "/api/v1/categories";
        public static final String API_PRODUCTS = "/api/v1/products";
        public static final String API_REVIEWS = "/api/v1/reviews";
        public static final String API_USERS = "/api/v1/users";
        public static final String API_AUTH = "/api/v1/auth";
    }

    // ============ VIEW NAMES =============
    public static final class View {
        private View() {}
        public static final String MAIN = "main";
        public static final String LOGIN = "login";
    }
}
