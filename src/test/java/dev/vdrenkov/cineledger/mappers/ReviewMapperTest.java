package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.CinemaDto;
import dev.vdrenkov.cineledger.models.dtos.MovieDto;
import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.testutil.constants.ReviewConstants;
import dev.vdrenkov.cineledger.testutil.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutil.factories.MovieFactory;
import dev.vdrenkov.cineledger.testutil.factories.ReviewFactory;
import dev.vdrenkov.cineledger.testutil.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests review mapper behavior.
 */
@ExtendWith(MockitoExtension.class)
class ReviewMapperTest {

    @Mock
    private MovieMapper movieMapper;
    @Mock
    private CinemaMapper cinemaMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private ReviewMapper reviewMapper;

    /**
     * Verifies that map Review To Review DTO cinema Null success.
     */
    @Test
    void testMapReviewToReviewDto_cinemaNull_success() {
        final MovieDto movie = MovieFactory.getDefaultMovieDto();

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(movie);
        when(userMapper.mapUserToUserDto(any())).thenReturn(UserFactory.getDefaultUserDto());

        final ReviewDto reviewDto = reviewMapper.mapReviewToReviewDto(ReviewFactory.getDefaultReview());

        Assertions.assertEquals(ReviewConstants.ID, reviewDto.getId());
        Assertions.assertEquals(ReviewConstants.RATING, reviewDto.getRating(), 0.0);
        Assertions.assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.getReviewText());
        Assertions.assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.getDateModified());
        assertEquals(movie, reviewDto.getMovie());
        assertNull(reviewDto.getCinema());
        Assertions.assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    /**
     * Verifies that map Review To Review DTO movie Null success.
     */
    @Test
    void testMapReviewToReviewDto_movieNull_success() {
        final CinemaDto cinema = CinemaFactory.getDefaultCinemaDto();

        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(cinema);
        when(userMapper.mapUserToUserDto(any())).thenReturn(UserFactory.getDefaultUserDto());

        final ReviewDto reviewDto = reviewMapper.mapReviewToReviewDto(ReviewFactory.getDefaultReviewWithCinema());

        Assertions.assertEquals(ReviewConstants.ID, reviewDto.getId());
        Assertions.assertEquals(ReviewConstants.RATING, reviewDto.getRating(), 0.0);
        Assertions.assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.getReviewText());
        Assertions.assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.getDateModified());
        assertNull(reviewDto.getMovie());
        assertEquals(cinema, reviewDto.getCinema());
        Assertions.assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    /**
     * Verifies that map Review List To Review DTO List cinema Null success.
     */
    @Test
    void testMapReviewListToReviewDtoList_cinemaNull_success() {
        final MovieDto movie = MovieFactory.getDefaultMovieDto();
        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(movie);
        when(userMapper.mapUserToUserDto(any())).thenReturn(UserFactory.getDefaultUserDto());

        final List<ReviewDto> reviewDtos = reviewMapper.mapReviewListToReviewDtoList(
            ReviewFactory.getDefaultReviewList());
        final ReviewDto reviewDto = reviewDtos.getFirst();

        Assertions.assertEquals(ReviewConstants.ID, reviewDto.getId());
        Assertions.assertEquals(ReviewConstants.RATING, reviewDto.getRating(), 0.0);
        Assertions.assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.getReviewText());
        Assertions.assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.getDateModified());
        assertEquals(movie, reviewDto.getMovie());
        assertNull(reviewDto.getCinema());
        Assertions.assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    /**
     * Verifies that map Review List To Review DTO List movie Null success.
     */
    @Test
    void testMapReviewListToReviewDtoList_movieNull_success() {
        final CinemaDto cinema = CinemaFactory.getDefaultCinemaDto();

        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(cinema);
        when(userMapper.mapUserToUserDto(any())).thenReturn(UserFactory.getDefaultUserDto());

        List<ReviewDto> reviewDtos = reviewMapper.mapReviewListToReviewDtoList(
            ReviewFactory.getDefaultReviewListWithCinema());
        final ReviewDto reviewDto = reviewDtos.getFirst();

        Assertions.assertEquals(ReviewConstants.ID, reviewDto.getId());
        Assertions.assertEquals(ReviewConstants.RATING, reviewDto.getRating(), 0.0);
        Assertions.assertEquals(ReviewConstants.REVIEW_TEXT, reviewDto.getReviewText());
        Assertions.assertEquals(ReviewConstants.DATE_MODIFIED, reviewDto.getDateModified());
        assertNull(reviewDto.getMovie());
        assertEquals(cinema, reviewDto.getCinema());
        Assertions.assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }
}




