package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.models.entities.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Maps review domain models to DTO representations used by the API.
 */
public final class ReviewMapper {
    private static final Logger log = LoggerFactory.getLogger(ReviewMapper.class);

    private ReviewMapper() {
        /* This utility class should not be instantiated */
    }

    /**
     * Maps review values to review dto values.
     *
     * @param review
     *     review entity to transform
     * @return review dto result
     */
    public static ReviewDto mapReviewToReviewDto(Review review) {
        log.info("Mapping the review {} to a review DTO", review.getId());

        if (Objects.nonNull(review.getMovie())) {
            return new ReviewDto(review.getId(), review.getRating(), review.getReviewText(), review.getDateModified(),
                MovieMapper.mapMovieToMovieDto(review.getMovie()), null, UserMapper.mapUserToUserDto(review.getUser()));
        }

        return new ReviewDto(review.getId(), review.getRating(), review.getReviewText(), review.getDateModified(), null,
            CinemaMapper.mapCinemaToCinemaDto(review.getCinema()), UserMapper.mapUserToUserDto(review.getUser()));
    }

    /**
     * Maps review list values to review dto list values.
     *
     * @param reviews
     *     review entities to transform
     * @return matching review dto values
     */
    public static List<ReviewDto> mapReviewListToReviewDtoList(List<Review> reviews) {
        return reviews.stream().map(ReviewMapper::mapReviewToReviewDto).toList();
    }
}


