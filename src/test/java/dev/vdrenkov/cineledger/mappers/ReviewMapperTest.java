package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.testutils.constants.ReviewConstants;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutils.factories.MovieFactory;
import dev.vdrenkov.cineledger.testutils.factories.ReviewFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests review mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class ReviewMapperTest {
    /**
     * Verifies that map Review To Review DTO cinema Null success.
     */
    @Test
    void testMapReviewToReviewDto_cinemaNull_success() {
        final ReviewDto reviewDto = ReviewMapper.mapReviewToReviewDto(ReviewFactory.getDefaultReview());

        assertEquals(ReviewConstants.ID, reviewDto.getId());
        assertEquals(ReviewConstants.RATING, reviewDto.getRating(), 0.0);
        assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.getReviewText());
        assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.getDateModified());
        assertEquals(MovieFactory.getDefaultMovieDto(), reviewDto.getMovie());
        assertNull(reviewDto.getCinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    /**
     * Verifies that map Review To Review DTO movie Null success.
     */
    @Test
    void testMapReviewToReviewDto_movieNull_success() {
        final ReviewDto reviewDto = ReviewMapper.mapReviewToReviewDto(ReviewFactory.getDefaultReviewWithCinema());

        assertEquals(ReviewConstants.ID, reviewDto.getId());
        assertEquals(ReviewConstants.RATING, reviewDto.getRating(), 0.0);
        assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.getReviewText());
        assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.getDateModified());
        assertNull(reviewDto.getMovie());
        assertEquals(CinemaFactory.getDefaultCinemaDto(), reviewDto.getCinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    /**
     * Verifies that map Review List To Review DTO List cinema Null success.
     */
    @Test
    void testMapReviewListToReviewDtoList_cinemaNull_success() {
        final List<ReviewDto> reviewDtos = ReviewMapper.mapReviewListToReviewDtoList(
            ReviewFactory.getDefaultReviewList());
        final ReviewDto reviewDto = reviewDtos.getFirst();

        assertEquals(ReviewConstants.ID, reviewDto.getId());
        assertEquals(ReviewConstants.RATING, reviewDto.getRating(), 0.0);
        assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.getReviewText());
        assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.getDateModified());
        assertEquals(MovieFactory.getDefaultMovieDto(), reviewDto.getMovie());
        assertNull(reviewDto.getCinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    /**
     * Verifies that map Review List To Review DTO List movie Null success.
     */
    @Test
    void testMapReviewListToReviewDtoList_movieNull_success() {
        final List<ReviewDto> reviewDtos = ReviewMapper.mapReviewListToReviewDtoList(
            ReviewFactory.getDefaultReviewListWithCinema());
        final ReviewDto reviewDto = reviewDtos.getFirst();

        assertEquals(ReviewConstants.ID, reviewDto.getId());
        assertEquals(ReviewConstants.RATING, reviewDto.getRating(), 0.0);
        assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.getReviewText());
        assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.getDateModified());
        assertNull(reviewDto.getMovie());
        assertEquals(CinemaFactory.getDefaultCinemaDto(), reviewDto.getCinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }
}




