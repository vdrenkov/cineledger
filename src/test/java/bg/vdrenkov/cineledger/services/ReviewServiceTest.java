package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import bg.vdrenkov.cineledger.exceptions.ReviewNotFoundException;
import bg.vdrenkov.cineledger.mappers.ReviewMapper;
import bg.vdrenkov.cineledger.models.dtos.ReviewDto;
import bg.vdrenkov.cineledger.models.entities.Review;
import bg.vdrenkov.cineledger.repositories.ReviewRepository;
import bg.vdrenkov.cineledger.testUtils.constants.ReviewConstants;
import bg.vdrenkov.cineledger.testUtils.factories.CinemaFactory;
import bg.vdrenkov.cineledger.testUtils.factories.MovieFactory;
import bg.vdrenkov.cineledger.testUtils.factories.ReviewFactory;
import bg.vdrenkov.cineledger.testUtils.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void testAddMovieReview_noExceptions_success() {
        Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.save(any())).thenReturn(ReviewFactory.getDefaultReview());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt())).thenReturn(
            MovieFactory.getDefaultMovieDto());

        Review review = reviewService.addMovieReview(ReviewFactory.getDefaultReviewRequest(), ReviewConstants.ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getMovie(), review.getMovie());
        assertEquals(expected.getReviewText(), review.getReviewText());
        Assertions.assertEquals(ReviewConstants.NOW, review.getDateModified());
    }

    @Test
    public void testAddMovieReview_reviewsSize1_success() {
        ReviewDto expectedDto = ReviewFactory.getDefaultReviewDto();
        Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.save(any())).thenReturn(new Review());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt())).thenReturn(
            MovieFactory.getDefaultMovieDto());
        when(reviewRepository.findByMovieId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewList());
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expectedDto);

        Review review = reviewService.addMovieReview(ReviewFactory.getDefaultReviewRequest(), ReviewConstants.ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getMovie(), review.getMovie());
        assertEquals(expected.getReviewText(), review.getReviewText());
        Assertions.assertEquals(ReviewConstants.NOW, review.getDateModified());
    }

    @Test
    public void testAddCinemaReview_noExceptions_success() {
        Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(reviewRepository.save(any())).thenReturn(expected);
        when(cinemaService.updateCinemaAverageRating(anyDouble(), anyInt())).thenReturn(
            CinemaFactory.getDefaultCinemaDto());

        Review review = reviewService.addCinemaReview(ReviewFactory.getDefaultReviewRequest(), ReviewConstants.ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getCinema(), review.getCinema());
        assertEquals(expected.getReviewText(), review.getReviewText());
        Assertions.assertEquals(ReviewConstants.NOW, review.getDateModified());
    }

    @Test
    public void testGetReviewsByMovieId_noExceptions_success() {
        List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.findByMovieId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewList());
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(ReviewFactory.getDefaultReviewDto());

        List<ReviewDto> reviews = reviewService.getReviewsByMovieId(ReviewConstants.ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    public void testGetReviewsByCinemaId_noExceptions_success() {
        List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(reviewRepository.findByCinemaId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewList());
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(ReviewFactory.getDefaultReviewDto());

        List<ReviewDto> reviews = reviewService.getReviewsByCinemaId(ReviewConstants.ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    public void testGetMovieReviewsByUserId_userAuthorized_success() {
        List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewRepository.findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(anyInt())).thenReturn(
            ReviewFactory.getDefaultReviewList());
        when(reviewMapper.mapReviewListToReviewDtoList(any())).thenReturn(expectedReviews);

        List<ReviewDto> reviews = reviewService.getMovieReviewsByUserId(ReviewConstants.ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    public void testGetMovieReviewsByUserId_userNotAuthorized_throwsNotAuthorizedException() {
        assertThrows(NotAuthorizedException.class, () -> {

            when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

            reviewService.getMovieReviewsByUserId(ReviewConstants.ID);

        });
    }

    @Test
    public void testGetCinemaReviewsByUserId_userAuthorized_success() {
        List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewMapper.mapReviewListToReviewDtoList(any())).thenReturn(expectedReviews);

        List<ReviewDto> reviews = reviewService.getCinemaReviewsByUserId(ReviewConstants.ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    public void testGetCinemaReviewsByUserId_userNotAuthorized_throwsNotAuthorizedException() {
        assertThrows(NotAuthorizedException.class, () -> {

            when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

            reviewService.getCinemaReviewsByUserId(ReviewConstants.ID);

        });
    }

    @Test
    public void testUpdateReview_noExceptions_success() {
        ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expected);
        when(reviewRepository.save(any())).thenReturn(ReviewFactory.getDefaultReview());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt())).thenReturn(
            MovieFactory.getDefaultMovieDto());
        CinemaFactory.getDefaultCinemaDto();
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);

        ReviewDto result = reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ReviewConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    public void testUpdateReview_userIdDifferent_throwsNotAuthorizedException() {
        assertThrows(NotAuthorizedException.class, () -> {

            when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
            when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

            reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ReviewConstants.ID);

        });
    }

    @Test
    public void testUpdateReview_movieNull_success() {
        Review review = ReviewFactory.getDefaultReview();
        review.setMovie(null);
        ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expected);

        ReviewDto result = reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ReviewConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    public void testUpdateReview_ReviewNotFoundException_fail() {
        assertThrows(ReviewNotFoundException.class, () -> {

            when(reviewRepository.findById(anyInt())).thenThrow(ReviewNotFoundException.class);

            reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ReviewConstants.ID);

        });
    }

    @Test
    public void testUpdateReview_ReviewByUserNotFoundException_fail() {
        assertThrows(ReviewNotFoundException.class, () -> {

            when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

            reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ReviewConstants.ID);

        });
    }

    @Test
    public void testDeleteReview_noExceptions_success() {
        ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expected);
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt())).thenReturn(
            MovieFactory.getDefaultMovieDto());
        CinemaFactory.getDefaultCinemaDto();

        ReviewDto result = reviewService.deleteReview(ReviewConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteReview_movieNull_success() {
        Review review = ReviewFactory.getDefaultReview();
        review.setMovie(null);
        ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expected);

        ReviewDto result = reviewService.deleteReview(ReviewConstants.ID);

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteReview_ReviewNotFoundException_fail() {
        assertThrows(ReviewNotFoundException.class, () -> {

            when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

            reviewService.deleteReview(ReviewConstants.ID);

        });
    }

    @Test
    public void testDeleteReview_ReviewByUserNotFoundException_fail() {
        assertThrows(NotAuthorizedException.class, () -> {

            when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
            when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

            reviewService.deleteReview(ReviewConstants.ID);

        });
    }
}




