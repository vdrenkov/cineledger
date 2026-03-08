package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Provides persistence access for movie entities.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    /**
     * Finds a movie by its exact title.
     *
     * @param title
     *     the exact title to match
     * @return the matching movie when present
     */
    Optional<Movie> findByTitle(String title);

    /**
     * Searches for movies whose title contains the given text.
     *
     * @param title
     *     the title fragment to search for
     * @return the matching movies
     */
    List<Movie> findByTitleContaining(String title);

    /**
     * Searches for movies whose title contains the given text and whose rating meets the minimum threshold.
     *
     * @param title
     *     the title fragment to search for
     * @param minRating
     *     the minimum average rating
     * @return the matching movies
     */
    List<Movie> findByTitleContainingAndAverageRatingGreaterThanEqual(String title, double minRating);

    /**
     * Retrieves all movies whose average rating is at least the given threshold.
     *
     * @param averageRating
     *     the minimum average rating
     * @return the matching movies
     */
    List<Movie> findByAverageRatingGreaterThanEqual(double averageRating);

    /**
     * Retrieves all movies that belong to the given category.
     *
     * @param categoryId
     *     the category identifier
     * @return the movies in the category
     */
    List<Movie> findByCategoryId(int categoryId);

    /**
     * Retrieves all movies in the given category that also satisfy the minimum rating.
     *
     * @param categoryId
     *     the category identifier
     * @param rating
     *     the minimum average rating
     * @return the matching movies
     */
    List<Movie> findByCategoryIdAndAverageRatingGreaterThanEqual(int categoryId, double rating);

    /**
     * Retrieves all movies released after the given date.
     *
     * @param date
     *     the lower release-date bound
     * @return the matching movies
     */
    List<Movie> findByReleaseDateAfter(LocalDate date);

    /**
     * Retrieves all movies released before the given date.
     *
     * @param date
     *     the upper release-date bound
     * @return the matching movies
     */
    List<Movie> findByReleaseDateBefore(LocalDate date);

    /**
     * Retrieves all movies released after the given date whose rating meets the threshold.
     *
     * @param date
     *     the lower release-date bound
     * @param rating
     *     the minimum average rating
     * @return the matching movies
     */
    List<Movie> findByReleaseDateAfterAndAverageRatingGreaterThanEqual(LocalDate date, double rating);

    /**
     * Retrieves all movies released before the given date whose rating meets the threshold.
     *
     * @param date
     *     the upper release-date bound
     * @param rating
     *     the minimum average rating
     * @return the matching movies
     */
    List<Movie> findByReleaseDateBeforeAndAverageRatingGreaterThanEqual(LocalDate date, double rating);
}


