package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByCinemaId(int cinemaId);

    List<Review> findByMovieId(int movieId);

    List<Review> findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(int userId);

    List<Review> findAllByUserIdAndMovieIsNullAndCinemaIsNotNull(int userId);

    @Query(
            "select coalesce(avg(review.rating), 0.0) from Review review where review.movie.id ="
                    + " :id")
    double averageRatingByMovieId(int id);

    @Query(
            "select coalesce(avg(review.rating), 0.0) from Review review where review.cinema.id ="
                    + " :id")
    double averageRatingByCinemaId(int id);
}
