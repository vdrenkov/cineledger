package bg.vdrenkov.cineledger.testUtils.factories;

import bg.vdrenkov.cineledger.models.dtos.ReviewDto;
import bg.vdrenkov.cineledger.models.entities.Review;
import bg.vdrenkov.cineledger.models.requests.ReviewRequest;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;

import java.util.Collections;
import java.util.List;

import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.DATE_MODIFIED;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.ID;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.RATING;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.REVIEW_CINEMA;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.REVIEW_CINEMA_DTO;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.REVIEW_MOVIE;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.REVIEW_MOVIE_DTO;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.REVIEW_TEXT;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.REVIEW_USER;
import static bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants.REVIEW_USER_DTO;

public final class ReviewFactory {

    private ReviewFactory() throws IllegalAccessException {
        throw new IllegalAccessException(ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE);
    }

    public static Review getDefaultReview() {
        return new Review(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, REVIEW_MOVIE, REVIEW_CINEMA, REVIEW_USER);
    }

    public static List<Review> getDefaultReviewList() {
        return Collections.singletonList(getDefaultReview());
    }

    public static ReviewDto getDefaultReviewDto() {
        return new ReviewDto(ID, RATING, REVIEW_TEXT, DATE_MODIFIED, REVIEW_MOVIE_DTO, REVIEW_CINEMA_DTO,
            REVIEW_USER_DTO);
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

    public static ReviewRequest getDefaultReviewRequest() {
        return new ReviewRequest(RATING, REVIEW_TEXT);
    }
}


