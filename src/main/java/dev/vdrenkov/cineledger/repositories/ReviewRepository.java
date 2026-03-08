package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides persistence access for review entities.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    /**
     * Retrieves all reviews written for a cinema.
     *
     * @param cinemaId
     *     the cinema identifier
     * @return the reviews associated with the cinema
     */
    List<Review> findByCinemaId(int cinemaId);

    /**
     * Retrieves all reviews written for a movie.
     *
     * @param movieId
     *     the movie identifier
     * @return the reviews associated with the movie
     */
    List<Review> findByMovieId(int movieId);

    /**
     * Retrieves all movie reviews authored by a user.
     *
     * @param userId
     *     the authoring user identifier
     * @return the user's movie reviews
     */
    List<Review> findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(int userId);

    /**
     * Retrieves all cinema reviews authored by a user.
     *
     * @param userId
     *     the authoring user identifier
     * @return the user's cinema reviews
     */
    List<Review> findAllByUserIdAndMovieIsNullAndCinemaIsNotNull(int userId);
}


