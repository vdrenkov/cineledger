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
    List<Review> findAll();

    List<Review> findByCinemaId(int cinemaId);

    List<Review> findByMovieId(int movieId);

    List<Review> findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(int userId);

    List<Review> findAllByUserIdAndMovieIsNullAndCinemaIsNotNull(int userId);
}


