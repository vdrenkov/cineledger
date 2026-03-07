package bg.vdrenkov.cineledger.services;

import bg.vdrenkov.cineledger.exceptions.NotAuthorizedException;
import bg.vdrenkov.cineledger.exceptions.ReviewNotFoundException;
import bg.vdrenkov.cineledger.mappers.ReviewMapper;
import bg.vdrenkov.cineledger.models.dtos.ReviewDto;
import bg.vdrenkov.cineledger.models.entities.Cinema;
import bg.vdrenkov.cineledger.models.entities.Movie;
import bg.vdrenkov.cineledger.models.entities.Review;
import bg.vdrenkov.cineledger.models.entities.User;
import bg.vdrenkov.cineledger.models.requests.ReviewRequest;
import bg.vdrenkov.cineledger.repositories.ReviewRepository;
import bg.vdrenkov.cineledger.utils.constants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final MovieService movieService;
    private final CinemaService cinemaService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper, UserService userService,
        MovieService movieService, CinemaService cinemaService) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.userService = userService;
        this.movieService = movieService;
        this.cinemaService = cinemaService;
    }

    public Review addMovieReview(ReviewRequest request, int movieId) {
        User user = userService.getCurrentUser();
        Movie movie = movieService.getMovieById(movieId);
        LocalDate now = LocalDate.now();
        Review review = new Review(request.getRating(), request.getReviewText(), now, movie, user);

        log.info("An attempt to add a new review in the database");

        reviewRepository.save(review);

        double averageRating = calculateAverageRatingForMovie(movieId);
        movieService.updateMovieAverageRating(averageRating, movieId);

        return review;
    }

    public Review addCinemaReview(ReviewRequest request, int cinemaId) {
        User user = userService.getCurrentUser();
        Cinema cinema = cinemaService.getCinemaById(cinemaId);
        LocalDate now = LocalDate.now();
        Review review = new Review(request.getRating(), request.getReviewText(), now, cinema, user);

        log.info("An attempt to add a new review in the database");

        reviewRepository.save(review);

        double averageRating = calculateAverageRatingForCinema(cinemaId);
        cinemaService.updateCinemaAverageRating(averageRating, cinemaId);

        return review;
    }

    public List<ReviewDto> getReviewsByMovieId(int movieId) {
        movieService.getMovieById(movieId);

        log.info(String.format("An attempt to extract all reviews with movie id %d", movieId));

        return reviewRepository
            .findByMovieId(movieId)
            .stream()
            .map(reviewMapper::mapReviewToReviewDto)
            .collect(Collectors.toList());
    }

    public List<ReviewDto> getReviewsByCinemaId(int cinemaId) {
        cinemaService.getCinemaById(cinemaId);

        log.info(String.format("An attempt to extract all reviews with cinema id %d", cinemaId));

        return reviewRepository
            .findByCinemaId(cinemaId)
            .stream()
            .map(reviewMapper::mapReviewToReviewDto)
            .collect(Collectors.toList());
    }

    public List<ReviewDto> getMovieReviewsByUserId(int userId) {
        if (userService.isCurrentUserAuthorized(userId)) {
            return reviewMapper.mapReviewListToReviewDtoList(
                reviewRepository.findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(userId));
        } else {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }
    }

    public List<ReviewDto> getCinemaReviewsByUserId(int userId) {
        if (userService.isCurrentUserAuthorized(userId)) {
            return reviewMapper.mapReviewListToReviewDtoList(
                reviewRepository.findAllByUserIdAndMovieIsNullAndCinemaIsNotNull(userId));
        } else {
            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }
    }

    public ReviewDto updateReview(ReviewRequest request, int reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error(String.format("Exception caught: " + ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE));

            throw new ReviewNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE);
        });

        if (!userService.isCurrentUserAuthorized(review.getUser().getId())) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        ReviewDto reviewDto = reviewMapper.mapReviewToReviewDto(review);

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

    public ReviewDto deleteReview(int reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error(String.format("Exception caught: " + ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE));

            throw new ReviewNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE);
        });

        if (!userService.isCurrentUserAuthorized(review.getUser().getId())) {
            log.error(String.format("Exception caught: %s", ExceptionMessages.NOT_AUTHORIZED_MESSAGE));

            throw new NotAuthorizedException(ExceptionMessages.NOT_AUTHORIZED_MESSAGE);
        }

        ReviewDto reviewDto = reviewMapper.mapReviewToReviewDto(review);

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
        List<ReviewDto> reviews = getReviewsByCinemaId(cinemaId);

        return calculateAverageRating(reviews);
    }

    private double calculateAverageRatingForMovie(int movieId) {
        List<ReviewDto> reviews = getReviewsByMovieId(movieId);

        return calculateAverageRating(reviews);
    }

    private double calculateAverageRating(List<ReviewDto> reviews) {
        double sum = 0;
        int numberOfReviews = reviews.size();

        for (ReviewDto review : reviews) {
            sum += review.getRating();
        }

        if (numberOfReviews == 0) {
            return sum / (numberOfReviews + 1);
        }
        return sum / numberOfReviews;
    }
}


