package dev.vdrenkov.cineledger.testutils.constants;

import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.time.Duration;
import java.time.LocalDate;

public final class MovieConstants {

    public static final String TITLE = "Movie title";

    public static final String DESCRIPTION = "Movie description";

    public static final LocalDate RELEASE_DATE = LocalDate.of(2100, 1, 1);

    public static final Duration RUNTIME = Duration.ofHours(24);

    public static final int ID = 1;

    public static final double RATING = 5;

    private MovieConstants() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
