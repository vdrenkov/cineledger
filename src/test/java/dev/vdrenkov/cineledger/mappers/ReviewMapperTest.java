package dev.vdrenkov.cineledger.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

@ExtendWith(MockitoExtension.class)
class ReviewMapperTest {

    @Test
    void testMapReviewToReviewDto_cinemaNull_success() {
        final ReviewDto reviewDto =
                ReviewMapper.mapReviewToReviewDto(ReviewFactory.getDefaultReview());

        assertEquals(ReviewConstants.ID, reviewDto.id());
        assertEquals(ReviewConstants.RATING, reviewDto.rating(), 0.0);
        assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.reviewText());
        assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.dateModified());
        assertEquals(MovieFactory.getDefaultMovieDto(), reviewDto.movie());
        assertNull(reviewDto.cinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.user());
    }

    @Test
    void testMapReviewToReviewDto_movieNull_success() {
        final ReviewDto reviewDto =
                ReviewMapper.mapReviewToReviewDto(ReviewFactory.getDefaultReviewWithCinema());

        assertEquals(ReviewConstants.ID, reviewDto.id());
        assertEquals(ReviewConstants.RATING, reviewDto.rating(), 0.0);
        assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.reviewText());
        assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.dateModified());
        assertNull(reviewDto.movie());
        assertEquals(CinemaFactory.getDefaultCinemaDto(), reviewDto.cinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.user());
    }

    @Test
    void testMapReviewListToReviewDtoList_cinemaNull_success() {
        final List<ReviewDto> reviewDtos =
                ReviewMapper.mapReviewListToReviewDtoList(ReviewFactory.getDefaultReviewList());
        final ReviewDto reviewDto = reviewDtos.getFirst();

        assertEquals(ReviewConstants.ID, reviewDto.id());
        assertEquals(ReviewConstants.RATING, reviewDto.rating(), 0.0);
        assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.reviewText());
        assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.dateModified());
        assertEquals(MovieFactory.getDefaultMovieDto(), reviewDto.movie());
        assertNull(reviewDto.cinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.user());
    }

    @Test
    void testMapReviewListToReviewDtoList_movieNull_success() {
        final List<ReviewDto> reviewDtos =
                ReviewMapper.mapReviewListToReviewDtoList(
                        ReviewFactory.getDefaultReviewListWithCinema());
        final ReviewDto reviewDto = reviewDtos.getFirst();

        assertEquals(ReviewConstants.ID, reviewDto.id());
        assertEquals(ReviewConstants.RATING, reviewDto.rating(), 0.0);
        assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.reviewText());
        assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.dateModified());
        assertNull(reviewDto.movie());
        assertEquals(CinemaFactory.getDefaultCinemaDto(), reviewDto.cinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.user());
    }
}
