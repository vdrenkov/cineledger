package dev.vdrenkov.cineledger.utils.constants;

/**
 * Centralizes the URI templates exposed by the REST API.
 */
public final class URIConstants {

    /**
     * Categories endpoints
     */

    /** Collection endpoint for categories. */
    public static final String CATEGORIES_PATH = "/categories";
    /** Item endpoint for a single category. */
    public static final String CATEGORIES_ID_PATH = "/categories/{id}";

    /**
     * Cinemas endpoints
     */

    /** Collection endpoint for cinemas. */
    public static final String CINEMAS_PATH = "/cinemas";
    /** Item endpoint for a single cinema. */
    public static final String CINEMAS_ID_PATH = "/cinemas/{id}";

    /**
     * Discounts endpoints
     */

    /** Collection endpoint for discounts. */
    public static final String DISCOUNTS_PATH = "/discounts";
    /** Item endpoint for a single discount. */
    public static final String DISCOUNTS_ID_PATH = "/discounts/{id}";

    /**
     * Halls endpoints
     */

    /** Collection endpoint for halls. */
    public static final String HALLS_PATH = "/halls";
    /** Item endpoint for a single hall. */
    public static final String HALLS_ID_PATH = "/halls/{id}";
    /** Nested endpoint for halls that belong to a specific cinema. */
    public static final String CINEMAS_ID_HALLS_PATH = "/cinemas/{id}/halls";

    /**
     * Reports endpoints
     */

    /** Income report endpoint scoped to a cinema. */
    public static final String REPORTS_CINEMAS_ID_INCOMES_PATH = "/reports/cinemas/{id}/incomes";
    /** Income report endpoint scoped to a hall. */
    public static final String REPORTS_HALLS_ID_INCOMES_PATH = "/reports/halls/{id}/incomes";
    /** Income report endpoint scoped to an item. */
    public static final String REPORTS_ITEMS_ID_INCOMES_PATH = "/reports/items/{id}/incomes";
    /** Income report endpoint scoped to a movie. */
    public static final String REPORTS_MOVIES_ID_INCOMES_PATH = "/reports/movies/{id}/incomes";
    /** Income report endpoint scoped to a user. */
    public static final String REPORTS_USERS_ID_INCOMES_PATH = "/reports/users/{id}/incomes";

    /**
     * Items endpoints
     */

    /** Collection endpoint for concession items. */
    public static final String ITEMS_PATH = "/items";
    /** Item endpoint for a single concession item. */
    public static final String ITEMS_ID_PATH = "/items/{id}";

    /**
     * Movies endpoints
     */

    /** Collection endpoint for movies. */
    public static final String MOVIES_PATH = "/movies";
    /** Item endpoint for a single movie. */
    public static final String MOVIES_ID_PATH = "/movies/{id}";
    /** Nested endpoint for movies that belong to a specific category. */
    public static final String CATEGORIES_ID_MOVIES_PATH = "/categories/{id}/movies";

    /**
     * Orders endpoints
     */

    /** Collection endpoint for orders. */
    public static final String ORDERS_PATH = "/orders";
    /** Item endpoint for a single order. */
    public static final String ORDERS_ID_PATH = "/orders/{id}";
    /** Nested endpoint for orders created by a specific user. */
    public static final String USERS_ID_ORDERS_PATH = "/users/{id}/orders";

    /**
     * Programs endpoints
     */

    /** Collection endpoint for cinema programs. */
    public static final String PROGRAMS_PATH = "/programs";
    /** Item endpoint for a single program. */
    public static final String PROGRAMS_ID_PATH = "/programs/{id}";
    /** Nested endpoint for programs that belong to a specific cinema. */
    public static final String CINEMAS_ID_PROGRAMS_PATH = "/cinemas/{id}/programs";

    /**
     * Projections endpoints
     */

    /** Collection endpoint for movie projections. */
    public static final String PROJECTIONS_PATH = "/projections";
    /** Item endpoint for a single projection. */
    public static final String PROJECTIONS_ID_PATH = "/projections/{id}";
    /** Nested endpoint for projections that belong to a specific program. */
    public static final String PROGRAMS_ID_PROJECTIONS_PATH = "/programs/{id}/projections";
    /** Nested endpoint for projections that belong to a specific movie. */
    public static final String MOVIES_ID_PROJECTIONS_PATH = "/movies/{id}/projections";

    /**
     * Reviews endpoints
     */

    /** Item endpoint for a single review. */
    public static final String REVIEWS_ID_PATH = "/reviews/{id}";
    /** Nested endpoint for reviews written for a cinema. */
    public static final String CINEMAS_ID_REVIEWS_PATH = "/cinemas/{id}/reviews";
    /** Nested endpoint for reviews written for a movie. */
    public static final String MOVIES_ID_REVIEWS_PATH = "/movies/{id}/reviews";
    /** Nested endpoint for movie reviews created by a specific user. */
    public static final String USERS_ID_MOVIES_REVIEWS_PATH = "/users/{id}/movies/reviews";
    /** Nested endpoint for cinema reviews created by a specific user. */
    public static final String USERS_ID_CINEMAS_REVIEWS_PATH = "/users/{id}/cinemas/reviews";

    /**
     * Roles endpoints
     */

    /** Collection endpoint for roles. */
    public static final String ROLES_PATH = "/roles";
    /** Item endpoint for a single role. */
    public static final String ROLES_ID_PATH = "/roles/{id}";

    /**
     * Reports endpoints
     */

    /** Tickets-count report endpoint scoped to a movie category. */
    public static final String REPORTS_MOVIES_CATEGORIES_ID_TICKETS_COUNT_PATH = "/reports/movies/categories/{id}/tickets-count";
    /** Tickets-count report endpoint across all movies. */
    public static final String REPORTS_MOVIES_TICKETS_COUNT_PATH = "/reports/movies/tickets-count";
    /** Items-count report endpoint across all concession items. */
    public static final String REPORTS_ITEMS_ITEMS_COUNT_PATH = "/reports/items/items-count";

    /**
     * Tickets endpoints
     */

    /** Collection endpoint for tickets. */
    public static final String TICKETS_PATH = "/tickets";
    /** Item endpoint for a single ticket. */
    public static final String TICKETS_ID_PATH = "/tickets/{id}";
    /** Nested endpoint for tickets that belong to a specific projection. */
    public static final String PROJECTIONS_ID_TICKETS_PATH = "/projections/{id}/tickets";

    /**
     * Users endpoints
     */

    /** Collection endpoint for users. */
    public static final String USERS_PATH = "/users";
    /** Item endpoint for a single user. */
    public static final String USERS_ID_PATH = "/users/{id}";
    /** Collection endpoint for administrator-focused operations. */
    public static final String ADMINS_PATH = "/admins";
    /** Item endpoint for a single administrator-focused resource. */
    public static final String ADMINS_ID_PATH = "/admins/{id}";
    /** Endpoint used to authenticate a user session. */
    public static final String LOGIN_PATH = "/login";
    /** Endpoint used to register a new user. */
    public static final String REGISTRATION_PATH = "/registration";

    private URIConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



