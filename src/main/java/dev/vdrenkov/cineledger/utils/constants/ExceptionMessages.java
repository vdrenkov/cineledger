package dev.vdrenkov.cineledger.utils.constants;

/**
 * Centralizes reusable exception and validation messages used across the application.
 */
public final class ExceptionMessages {

    /*
     * Not found messages
     */

    /** Message used when a category lookup returns no result. */
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "No such category was found in the database!";
    /** Message used when a cinema lookup returns no result. */
    public static final String CINEMA_NOT_FOUND_MESSAGE = "No such cinema was found in the database!";
    /** Message used when a program lookup returns no result. */
    public static final String PROGRAM_NOT_FOUND_MESSAGE = "No such program was found in the database!";
    /** Message used when a user lookup returns no result. */
    public static final String USER_NOT_FOUND_MESSAGE = "No such user was found in the database!";
    /** Message used when an item lookup returns no result. */
    public static final String ITEM_NOT_FOUND_MESSAGE = "No such item was found in the database!";
    /** Message used when a hall lookup returns no result. */
    public static final String HALL_NOT_FOUND_MESSAGE = "No such hall was found in the database!";
    /** Message used when a projection lookup returns no result. */
    public static final String PROJECTION_NOT_FOUND_MESSAGE = "No such projection was found in the database!";
    /** Message used when a movie lookup returns no result. */
    public static final String MOVIE_NOT_FOUND_MESSAGE = "No such movie was found in the database!";
    /** Message used when an order lookup returns no result. */
    public static final String ORDER_NOT_FOUND_MESSAGE = "No such order was found in the database!";
    /** Message used when a role lookup returns no result. */
    public static final String ROLE_NOT_FOUND_MESSAGE = "No such role was found in the database!";
    /** Message used when no roles are supplied during registration or update. */
    public static final String ROLE_NOT_CHOSEN_MESSAGE = "You haven't specified any roles!";
    /** Message used when a review lookup returns no result. */
    public static final String REVIEW_NOT_FOUND_MESSAGE = "No such review was found in the database!";
    /** Message used when a ticket lookup returns no result. */
    public static final String TICKET_NOT_FOUND_MESSAGE = "No such ticket was found in the database!";
    /** Message used when a discount lookup returns no result. */
    public static final String DISCOUNT_NOT_FOUND_MESSAGE = "No such discount was found in the database!";

    /*
     * Authorization messages
     */

    /** Message returned when the authenticated user lacks permission for the action. */
    public static final String NOT_AUTHORIZED_MESSAGE = "You are not authorized to perform this action!";
    /** Message returned when an operation requires an authenticated user. */
    public static final String NOT_LOGGED_IN_MESSAGE = "You are not logged in!";

    /*
     * Validation messages
     */

    /** Message returned when a submitted date or date interval is invalid. */
    public static final String DATE_NOT_VALID_MESSAGE = "Date not valid.";
    /** Message returned when a hall is already occupied for the requested timeslot. */
    public static final String HALL_NOT_AVAILABLE_EXCEPTION = "The hall is not available for the desired start time.";
    /** Message returned when a supplied discount code cannot be applied. */
    public static final String DISCOUNT_CODE_NOT_VALID_MESSAGE = "Discount code not valid.";
    /** Message returned when no seats remain for the requested projection. */
    public static final String NO_AVAILABLE_TICKETS_EXCEPTION = "No more tickets available for this projection.";
    /** Message returned when an item is out of stock. */
    public static final String NO_AVAILABLE_ITEMS_EXCEPTION = "No more items of this kind available.";

    /*
     * Existing entity properties messages
     */

    /** Message used when a category with the same unique data already exists. */
    public static final String CATEGORY_ALREADY_EXISTS_MESSAGE = "Category already exists!";
    /** Message used when a cinema with the same unique data already exists. */
    public static final String CINEMA_ALREADY_EXISTS_MESSAGE = "Cinema already exists!";
    /** Message used when a discount with the same unique data already exists. */
    public static final String DISCOUNT_ALREADY_EXISTS_MESSAGE = "Discount already exists!";
    /** Message used when an item with the same unique data already exists. */
    public static final String ITEM_ALREADY_EXISTS_MESSAGE = "Item already exists!";
    /** Message used when a program with the same unique data already exists. */
    public static final String PROGRAM_ALREADY_EXISTS_MESSAGE = "Program already exists!";
    /** Message used when a movie with the same unique data already exists. */
    public static final String MOVIE_ALREADY_EXISTS_MESSAGE = "Movie already exists!";
    /** Message used when a username is already taken. */
    public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username already exists!";
    /** Message used when an email is already taken. */
    public static final String USER_EMAIL_ALREADY_EXISTS_MESSAGE = "Email already exists!";
    /** Message used when a role with the same unique data already exists. */
    public static final String ROLE_ALREADY_EXISTS_MESSAGE = "Role already exists!";

    /*
     * Message for constructor in non-instantiable classes
     */

    /** Message used by private utility constructors that must never be called. */
    public static final String NON_INSTANTIABLE_CLASS_MESSAGE = "Do not instantiate this class!";

    /*
     * Global exception messages
     */

    /** Fallback message used for unexpected failures. */
    public static final String GLOBAL_EXCEPTION = "Something went wrong…";
    /** Log message prefix used when exceptions are handled centrally. */
    public static final String CAUGHT_EXCEPTION = "Exception caught";
    /** Message returned when the request payload or parameters fail validation. */
    public static final String INVALID_REQUEST_MESSAGE = "The request is invalid.";
    /** Message returned when a route does not support the attempted HTTP method. */
    public static final String METHOD_NOT_ALLOWED_MESSAGE = "The requested HTTP method is not supported.";
    /** Message returned when authentication cannot be completed successfully. */
    public static final String AUTHENTICATION_FAILED_MESSAGE = "Authentication failed.";
    /** Message returned when the request conflicts with the current persisted state. */
    public static final String DATA_CONFLICT_MESSAGE = "The request conflicts with existing data.";

    private ExceptionMessages() throws IllegalAccessException {
        throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}



