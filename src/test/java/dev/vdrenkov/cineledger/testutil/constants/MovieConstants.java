package dev.vdrenkov.cineledger.testutil.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Collects reusable movie constants for tests.
 */
public final class MovieConstants {

    /**
     * Provides the default title used in tests.
     */
    public static final String TITLE = "Movie title";
    /**
     * Provides the default description used in tests.
     */
    public static final String DESCRIPTION = "Movie description";
    /**
     * Provides the default release date used in tests.
     */
    public static final LocalDate RELEASE_DATE = LocalDate.now().plusDays(30);
    /**
     * Provides the default runtime used in tests.
     */
    public static final Duration RUNTIME = Duration.ofHours(24);
    /**
     * Provides the default identifier used in tests.
     */
    public static final int ID = 1;
    /**
     * Provides the default rating used in tests.
     */
    public static final double RATING = 5;

    private MovieConstants() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}


