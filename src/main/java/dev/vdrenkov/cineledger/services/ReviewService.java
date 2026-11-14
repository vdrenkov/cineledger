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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final MovieService movieService;
    private final CinemaService cinemaService;

    public ReviewService(ReviewRepository reviewRepository, UserService userService, MovieService movieService,
        CinemaService cinemaService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.movieService = movieService;
        this.cinemaService = cinemaService;
    }

    public Review addMovieReview(ReviewRequest request, int movieId) {
        final User user = userService.getCurrentUser();
        final Movie movie = movieService.getMovieById(movieId);
        final LocalDate now = LocalDate.now();
        final Review review = new Review(request.rating(), request.reviewText(), now, movie, user);

        reviewRepository.save(review);

        final double averageRating = calculateAverageRatingForMovie(movieId);
        movieService.updateMovieAverageRating(averageRating, movieId);

        return review;
    }

    public Review addCinemaReview(ReviewRequest request, int cinemaId) {
        final User user = userService.getCurrentUser();
        final Cinema cinema = cinemaService.getCinemaById(cinemaId);
        final LocalDate now = LocalDate.now();
        final Review review = new Review(request.rating(), request.reviewText(), now, cinema, user);

        reviewRepository.save(review);

        final double averageRating = calculateAverageRatingForCinema(cinemaId);
        cinemaService.updateCinemaAverageRating(averageRating, cinemaId);

        return review;
    }

    public List<ReviewDto> getReviewsByMovieId(int movieId) {
        movieService.getMovieById(movieId);

        return reviewRepository.findByMovieId(movieId).stream().map(ReviewMapper::mapReviewToReviewDto).toList();
    }

    public List<ReviewDto> getReviewsByCinemaId(int cinemaId) {
        cinemaService.getCinemaById(cinemaId);

        return reviewRepository.findByCinemaId(cinemaId).stream().map(ReviewMapper::mapReviewToReviewDto).toList();
    }

    public List<ReviewDto> getMovieReviewsByUserId(int userId) {
        if (userService.isCurrentUserAuthorized(userId)) {
            return ReviewMapper.mapReviewListToReviewDtoList(
                reviewRepository.findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(userId));
        } else {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }
    }

    public List<ReviewDto> getCinemaReviewsByUserId(int userId) {
        if (userService.isCurrentUserAuthorized(userId)) {
            return ReviewMapper.mapReviewListToReviewDtoList(
                reviewRepository.findAllByUserIdAndMovieIsNullAndCinemaIsNotNull(userId));
        } else {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }
    }

    public ReviewDto updateReview(ReviewRequest request, int reviewId) {
        final Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {

            return new ReviewNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE);
        });

        if (!userService.isCurrentUserAuthorized(review.getUser().getId())) {

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        final ReviewDto reviewDto = ReviewMapper.mapReviewToReviewDto(review);

        review.setReviewText(request.reviewText());
        review.setRating(request.rating());

        reviewRepository.save(review);

        return getReviewDto(review, reviewDto);
    }

    public ReviewDto deleteReview(int reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {

            return new ReviewNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE);
        });

        if (!userService.isCurrentUserAuthorized(review.getUser().getId())) {

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        final ReviewDto reviewDto = ReviewMapper.mapReviewToReviewDto(review);

        reviewRepository.delete(review);

        return getReviewDto(review, reviewDto);
    }

    private ReviewDto getReviewDto(final Review review, final ReviewDto reviewDto) {
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

    private static double calculateAverageRating(List<ReviewDto> reviews) {
        double sum = 0;
        final int numberOfReviews = reviews.size();

        for (ReviewDto review : reviews) {
            sum += review.rating();
        }

        if (numberOfReviews == 0) {
            return sum / (numberOfReviews + 1);
        }
        return sum / numberOfReviews;
    }
}
