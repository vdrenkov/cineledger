package dev.vdrenkov.cineledger.mappers;

import dev.vdrenkov.cineledger.models.dtos.ReviewDto;
import dev.vdrenkov.cineledger.models.entities.Review;

import java.util.List;
import java.util.Objects;

public final class ReviewMapper {

    private ReviewMapper() {

    }

    public static ReviewDto mapReviewToReviewDto(Review review) {

        if (Objects.nonNull(review.getMovie())) {
            return new ReviewDto(review.getId(), review.getRating(), review.getReviewText(), review.getDateModified(),
                MovieMapper.mapMovieToMovieDto(review.getMovie()), null, UserMapper.mapUserToUserDto(review.getUser()));
        }

        return new ReviewDto(review.getId(), review.getRating(), review.getReviewText(), review.getDateModified(), null,
            CinemaMapper.mapCinemaToCinemaDto(review.getCinema()), UserMapper.mapUserToUserDto(review.getUser()));
    }

    public static List<ReviewDto> mapReviewListToReviewDtoList(List<Review> reviews) {
        return reviews.stream().map(ReviewMapper::mapReviewToReviewDto).toList();
    }
}
