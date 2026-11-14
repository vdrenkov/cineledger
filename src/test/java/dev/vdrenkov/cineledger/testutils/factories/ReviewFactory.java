package dev.vdrenkov.cineledger.testutils.factories;

import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.models.entities.Review;
import dev.vdrenkov.cineledger.models.requests.ReviewRequest;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.DATE_MODIFIED;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.RATING;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.REVIEW_CINEMA;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.REVIEW_CINEMA_DTO;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.REVIEW_MOVIE;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.REVIEW_MOVIE_DTO;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.REVIEW_TEXT;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.REVIEW_USER;
import static dev.vdrenkov.cineledger.testutils.constants.ReviewConstants.REVIEW_USER_DTO;

public final class ReviewFactory {

    private ReviewFactory() {
        throw new IllegalStateException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static Review getDefaultReview() {
        return new Review(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, REVIEW_MOVIE, REVIEW_CINEMA, REVIEW_USER);
    }

    public static List<Review> getDefaultReviewList() {
        return Collections.singletonList(getDefaultReview());
    }

    public static ReviewDto getDefaultReviewDto() {
        return new ReviewDto(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, REVIEW_MOVIE_DTO, null, REVIEW_USER_DTO);
    }

    public static ReviewDto getDefaultReviewDtoWithCinema() {
        return new ReviewDto(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, null, REVIEW_CINEMA_DTO, REVIEW_USER_DTO);
    }

    public static Review getDefaultReviewWithCinema() {
        return new Review(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, null, REVIEW_CINEMA, REVIEW_USER);
    }

    public static List<Review> getDefaultReviewListWithCinema() {
        return Collections.singletonList(getDefaultReviewWithCinema());
    }

    public static List<ReviewDto> getDefaultReviewDtoList() {
        return Collections.singletonList(getDefaultReviewDto());
    }

    public static List<ReviewDto> getDefaultReviewDtoListWithCinema() {
        return Collections.singletonList(getDefaultReviewDtoWithCinema());
    }

    public static ReviewRequest getDefaultReviewRequest() {
        return new ReviewRequest(RATING, REVIEW_TEXT);
    }
}
