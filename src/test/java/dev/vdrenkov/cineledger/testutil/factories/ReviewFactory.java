package dev.vdrenkov.cineledger.testutil.factories;

import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.models.entities.Review;
import dev.vdrenkov.cineledger.models.requests.ReviewRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.DATE_MODIFIED;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.ID;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.RATING;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.REVIEW_CINEMA;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.REVIEW_CINEMA_DTO;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.REVIEW_MOVIE;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.REVIEW_MOVIE_DTO;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.REVIEW_TEXT;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.REVIEW_USER;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.REVIEW_USER_DTO;

/**
 * Provides reusable review fixtures for tests.
 */
public final class ReviewFactory {

    private ReviewFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    /**
     * Returns the default review fixture used in tests.
     *
     * @return test review value
     */
    public static Review getDefaultReview() {
        return new Review(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, REVIEW_MOVIE, REVIEW_CINEMA, REVIEW_USER);
    }

    /**
     * Returns the default review list fixture used in tests.
     *
     * @return test review values
     */
    public static List<Review> getDefaultReviewList() {
        return Collections.singletonList(getDefaultReview());
    }

    /**
     * Returns the default review dto fixture used in tests.
     *
     * @return test review dto value
     */
    public static ReviewDto getDefaultReviewDto() {
        return new ReviewDto(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, REVIEW_MOVIE_DTO, REVIEW_CINEMA_DTO,
            REVIEW_USER_DTO);
    }

    /**
     * Returns the default review with cinema fixture used in tests.
     *
     * @return test review value
     */
    public static Review getDefaultReviewWithCinema() {
        return new Review(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, null, REVIEW_CINEMA, REVIEW_USER);
    }

    /**
     * Returns the default review list with cinema fixture used in tests.
     *
     * @return test review values
     */
    public static List<Review> getDefaultReviewListWithCinema() {
        return Collections.singletonList(getDefaultReviewWithCinema());
    }

    /**
     * Returns the default review dto list fixture used in tests.
     *
     * @return test review dto values
     */
    public static List<ReviewDto> getDefaultReviewDtoList() {
        return Collections.singletonList(getDefaultReviewDto());
    }

    /**
     * Returns the default review request fixture used in tests.
     *
     * @return test review request value
     */
    public static ReviewRequest getDefaultReviewRequest() {
        return new ReviewRequest(RATING, REVIEW_TEXT);
    }
}


