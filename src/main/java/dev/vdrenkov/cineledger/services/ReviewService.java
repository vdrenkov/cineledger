package dev.vdrenkov.cineledger.services;

import dev.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import dev.vdrenkov.cineledger.exceptions.ReviewNotFoundException;
import dev.vdrenkov.cineledger.mappers.ReviewMapper;
import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.models.entities.Cinema;
import dev.vdrenkov.cineledger.models.entities.Movie;
import dev.vdrenkov.cineledger.models.entities.Review;
import dev.vdrenkov.cineledger.models.entities.User;
import dev.vdrenkov.cineledger.models.requests.ReviewRequest;
import dev.vdrenkov.cineledger.repositories.ReviewRepository;
import dev.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains business logic for review operations.
 */
@Service
public class ReviewService {
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final MovieService movieService;
    private final CinemaService cinemaService;

    /**
     * Creates a new review service with its required collaborators.
     *
     * @param reviewRepository
     *     review repository used by the operation
     * @param reviewMapper
     *     review mapper used by the operation
     * @param userService
     *     user service used by the operation
     * @param movieService
     *     movie service used by the operation
     * @param cinemaService
     *     cinema service used by the operation
     */
    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper, UserService userService,
        MovieService movieService, CinemaService cinemaService) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.userService = userService;
        this.movieService = movieService;
        this.cinemaService = cinemaService;
    }

    /**
     * Creates and persists movie review.
     *
     * @param request
     *     request payload containing the submitted data
     * @param movieId
     *     identifier of the target movie
     * @return requested review value
     */
    public Review addMovieReview(ReviewRequest request, int movieId) {
        final User user = userService.getCurrentUser();
        final Movie movie = movieService.getMovieById(movieId);
        final LocalDate now = LocalDate.now();
        final Review review = new Review(request.getRating(), request.getReviewText(), now, movie, user);

        log.info("An attempt to add a new review in the database");

        reviewRepository.save(review);

        final double averageRating = calculateAverageRatingForMovie(movieId);
        movieService.updateMovieAverageRating(averageRating, movieId);

        return review;
    }

    /**
     * Creates and persists cinema review.
     *
     * @param request
     *     request payload containing the submitted data
     * @param cinemaId
     *     identifier of the target cinema
     * @return requested review value
     */
    public Review addCinemaReview(ReviewRequest request, int cinemaId) {
        final User user = userService.getCurrentUser();
        final Cinema cinema = cinemaService.getCinemaById(cinemaId);
        final LocalDate now = LocalDate.now();
        final Review review = new Review(request.getRating(), request.getReviewText(), now, cinema, user);

        log.info("An attempt to add a new review in the database");

        reviewRepository.save(review);

        final double averageRating = calculateAverageRatingForCinema(cinemaId);
        cinemaService.updateCinemaAverageRating(averageRating, cinemaId);

        return review;
    }

    /**
     * Returns reviews matching the supplied criteria.
     *
     * @param movieId
     *     identifier of the target movie
     * @return matching review dto values
     */
    public List<ReviewDto> getReviewsByMovieId(int movieId) {
        movieService.getMovieById(movieId);

        log.info(String.format("An attempt to extract all reviews with movie id %d", movieId));

        return reviewRepository
            .findByMovieId(movieId)
            .stream()
            .map(reviewMapper::mapReviewToReviewDto)
            .collect(Collectors.toList());
    }

    /**
     * Returns reviews matching the supplied criteria.
     *
     * @param cinemaId
     *     identifier of the target cinema
     * @return matching review dto values
     */
    public List<ReviewDto> getReviewsByCinemaId(int cinemaId) {
        cinemaService.getCinemaById(cinemaId);

        log.info(String.format("An attempt to extract all reviews with cinema id %d", cinemaId));

        return reviewRepository
            .findByCinemaId(cinemaId)
            .stream()
            .map(reviewMapper::mapReviewToReviewDto)
            .collect(Collectors.toList());
    }

    /**
     * Returns movie reviews matching the supplied criteria.
     *
     * @param userId
     *     identifier of the target user
     * @return matching review dto values
     */
    public List<ReviewDto> getMovieReviewsByUserId(int userId) {
        if (userService.isCurrentUserAuthorized(userId)) {
            return reviewMapper.mapReviewListToReviewDtoList(
                reviewRepository.findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(userId));
        } else {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }
    }

    /**
     * Returns cinema reviews matching the supplied criteria.
     *
     * @param userId
     *     identifier of the target user
     * @return matching review dto values
     */
    public List<ReviewDto> getCinemaReviewsByUserId(int userId) {
        if (userService.isCurrentUserAuthorized(userId)) {
            return reviewMapper.mapReviewListToReviewDtoList(
                reviewRepository.findAllByUserIdAndMovieIsNullAndCinemaIsNotNull(userId));
        } else {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }
    }

    /**
     * Updates review and returns the previous state when needed.
     *
     * @param request
     *     request payload containing the submitted data
     * @param reviewId
     *     identifier of the target review
     * @return review dto result
     */
    public ReviewDto updateReview(ReviewRequest request, int reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error(String.format("Exception caught: " + ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE));

            throw new ReviewNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE);
        });

        if (!userService.isCurrentUserAuthorized(review.getUser().getId())) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        final ReviewDto reviewDto = reviewMapper.mapReviewToReviewDto(review);

        review.setReviewText(request.getReviewText());
        review.setRating(request.getRating());

        reviewRepository.save(review);

        log.info(String.format("Review with id %d was updated", reviewId));

        double averageRating;
        if (review.getMovie() != null) {

            averageRating = calculateAverageRatingForMovie(review.getMovie().getId());
            movieService.updateMovieAverageRating(averageRating, review.getMovie().getId());
        } else {

            averageRating = calculateAverageRatingForCinema(review.getCinema().getId());
            cinemaService.updateCinemaAverageRating(averageRating, review.getCinema().getId());
        }

        return reviewDto;
    }

    /**
     * Deletes review and returns the removed state when needed.
     *
     * @param reviewId
     *     identifier of the target review
     * @return review dto result
     */
    public ReviewDto deleteReview(int reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error(String.format("Exception caught: " + ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE));

            throw new ReviewNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE);
        });

        if (!userService.isCurrentUserAuthorized(review.getUser().getId())) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        final ReviewDto reviewDto = reviewMapper.mapReviewToReviewDto(review);

        reviewRepository.delete(review);

        log.info(String.format("Review with id %d was deleted", reviewId));

        double averageRating;
        if (review.getMovie() != null) {
            averageRating = calculateAverageRatingForMovie(review.getMovie().getId());
            movieService.updateMovieAverageRating(averageRating, review.getMovie().getId());
        } else {
            averageRating = calculateAverageRatingForCinema(review.getCinema().getId());
            cinemaService.updateCinemaAverageRating(averageRating, review.getCinema().getId());
        }

        return reviewDto;
    }

    private double calculateAverageRatingForCinema(int cinemaId) {
        final List<ReviewDto> reviews = getReviewsByCinemaId(cinemaId);

        return calculateAverageRating(reviews);
    }

    private double calculateAverageRatingForMovie(int movieId) {
        final List<ReviewDto> reviews = getReviewsByMovieId(movieId);

        return calculateAverageRating(reviews);
    }

    private double calculateAverageRating(List<ReviewDto> reviews) {
        double sum = 0;
        final int numberOfReviews = reviews.size();

        for (ReviewDto review : reviews) {
            sum += review.getRating();
        }

        if (numberOfReviews == 0) {
            return sum / (numberOfReviews + 1);
        }
        return sum / numberOfReviews;
    }
}


