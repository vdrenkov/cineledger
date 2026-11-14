package dev.vdrenkov.cineledger.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import dev.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import dev.vdrenkov.cineledger.exceptions.ReviewNotFoundException;
import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.models.entities.Review;
import dev.vdrenkov.cineledger.models.requests.ReviewRequest;
import dev.vdrenkov.cineledger.repositories.ReviewRepository;
import dev.vdrenkov.cineledger.testutils.constants.ReviewConstants;
import dev.vdrenkov.cineledger.testutils.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutils.factories.MovieFactory;
import dev.vdrenkov.cineledger.testutils.factories.ReviewFactory;
import dev.vdrenkov.cineledger.testutils.factories.UserFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    final ReviewRequest reviewRequest = ReviewFactory.getDefaultReviewRequest();

    @Mock private ReviewRepository reviewRepository;

    @Mock private UserService userService;

    @Mock private MovieService movieService;

    @Mock private CinemaService cinemaService;

    @InjectMocks private ReviewService reviewService;

    @Test
    void testAddMovieReview_noExceptions_success() {
        final Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(movieService.getMovieForUpdate(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.save(any())).thenReturn(ReviewFactory.getDefaultReview());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt()))
                .thenReturn(MovieFactory.getDefaultMovieDto());

        final Review review = reviewService.addMovieReview(reviewRequest, ReviewConstants.ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getMovie(), review.getMovie());
        assertEquals(expected.getReviewText(), review.getReviewText());
        Assertions.assertEquals(LocalDate.now(), review.getDateModified());
    }

    @Test
    void testAddMovieReview_reviewsSize1_success() {
        final Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(movieService.getMovieForUpdate(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.save(any())).thenReturn(new Review());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt()))
                .thenReturn(MovieFactory.getDefaultMovieDto());
        when(reviewRepository.averageRatingByMovieId(anyInt())).thenReturn(1.1);

        final Review review = reviewService.addMovieReview(reviewRequest, ReviewConstants.ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getMovie(), review.getMovie());
        assertEquals(expected.getReviewText(), review.getReviewText());
        Assertions.assertEquals(LocalDate.now(), review.getDateModified());
    }

    @Test
    void testAddCinemaReview_noExceptions_success() {
        final Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(cinemaService.getCinemaForUpdate(anyInt()))
                .thenReturn(CinemaFactory.getDefaultCinema());
        when(reviewRepository.save(any())).thenReturn(expected);
        when(cinemaService.updateCinemaAverageRating(anyDouble(), anyInt()))
                .thenReturn(CinemaFactory.getDefaultCinemaDto());

        final Review review = reviewService.addCinemaReview(reviewRequest, ReviewConstants.ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getCinema(), review.getCinema());
        assertEquals(expected.getReviewText(), review.getReviewText());
        Assertions.assertEquals(LocalDate.now(), review.getDateModified());
    }

    @Test
    void testGetReviewsByMovieId_noExceptions_success() {
        final List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.findByMovieId(anyInt()))
                .thenReturn(ReviewFactory.getDefaultReviewList());

        final List<ReviewDto> reviews = reviewService.getReviewsByMovieId(ReviewConstants.ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    void testGetReviewsByCinemaId_noExceptions_success() {
        final List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoListWithCinema();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(reviewRepository.findByCinemaId(anyInt()))
                .thenReturn(ReviewFactory.getDefaultReviewListWithCinema());

        final List<ReviewDto> reviews = reviewService.getReviewsByCinemaId(ReviewConstants.ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    void testGetMovieReviewsByUserId_userAuthorized_success() {
        final List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewRepository.findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(anyInt()))
                .thenReturn(ReviewFactory.getDefaultReviewList());

        final List<ReviewDto> reviews = reviewService.getMovieReviewsByUserId(ReviewConstants.ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    void testGetMovieReviewsByUserId_userNotAuthorized_throwsNotAuthorizedException() {
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        assertThrows(
                NotAuthorizedException.class,
                () -> reviewService.getMovieReviewsByUserId(ReviewConstants.ID));
    }

    @Test
    void testGetCinemaReviewsByUserId_userAuthorized_success() {
        final List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoListWithCinema();

        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewRepository.findAllByUserIdAndMovieIsNullAndCinemaIsNotNull(anyInt()))
                .thenReturn(ReviewFactory.getDefaultReviewListWithCinema());

        final List<ReviewDto> reviews = reviewService.getCinemaReviewsByUserId(ReviewConstants.ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    void testGetCinemaReviewsByUserId_userNotAuthorized_throwsNotAuthorizedException() {
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        assertThrows(
                NotAuthorizedException.class,
                () -> reviewService.getCinemaReviewsByUserId(ReviewConstants.ID));
    }

    @Test
    void testUpdateReview_noExceptions_success() {
        final ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt()))
                .thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(reviewRepository.save(any())).thenReturn(ReviewFactory.getDefaultReview());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt()))
                .thenReturn(MovieFactory.getDefaultMovieDto());
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);

        final ReviewDto result = reviewService.updateReview(reviewRequest, ReviewConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    void testUpdateReview_userIdDifferent_throwsNotAuthorizedException() {
        when(reviewRepository.findById(anyInt()))
                .thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        assertThrows(
                NotAuthorizedException.class,
                () -> reviewService.updateReview(reviewRequest, ReviewConstants.ID));
    }

    @Test
    void testUpdateReview_movieNull_success() {
        final Review review = ReviewFactory.getDefaultReviewWithCinema();
        final ReviewDto expected = ReviewFactory.getDefaultReviewDtoWithCinema();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(cinemaService.updateCinemaAverageRating(anyDouble(), anyInt()))
                .thenReturn(CinemaFactory.getDefaultCinemaDto());

        final ReviewDto result = reviewService.updateReview(reviewRequest, ReviewConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    void testUpdateReview_ReviewNotFoundException_fail() {
        when(reviewRepository.findById(anyInt())).thenThrow(ReviewNotFoundException.class);

        assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.updateReview(reviewRequest, ReviewConstants.ID));
    }

    @Test
    void testUpdateReview_ReviewByUserNotFoundException_fail() {
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.updateReview(reviewRequest, ReviewConstants.ID));
    }

    @Test
    void testDeleteReview_noExceptions_success() {
        final ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt()))
                .thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt()))
                .thenReturn(MovieFactory.getDefaultMovieDto());

        final ReviewDto result = reviewService.deleteReview(ReviewConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    void testDeleteReview_movieNull_success() {
        final Review review = ReviewFactory.getDefaultReviewWithCinema();
        final ReviewDto expected = ReviewFactory.getDefaultReviewDtoWithCinema();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(cinemaService.updateCinemaAverageRating(anyDouble(), anyInt()))
                .thenReturn(CinemaFactory.getDefaultCinemaDto());

        final ReviewDto result = reviewService.deleteReview(ReviewConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    void testDeleteReview_ReviewNotFoundException_fail() {
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.deleteReview(ReviewConstants.ID));
    }

    @Test
    void testDeleteReview_ReviewByUserNotFoundException_fail() {
        when(reviewRepository.findById(anyInt()))
                .thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        assertThrows(
                NotAuthorizedException.class, () -> reviewService.deleteReview(ReviewConstants.ID));
    }
}
