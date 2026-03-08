package dev.vdrenkov.cineledger.utils.constants;

/**
 * Shared log message templates used across the application.
 */
public final class LogMessages {

    /**
     * Template used when logging a handled exception message.
     */
    public static final String EXCEPTION_CAUGHT_LOG = "Exception caught: {}";

    private LogMessages() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }
}
