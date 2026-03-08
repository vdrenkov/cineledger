package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Projection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

/**
 * Provides persistence access for projection entities.
 */
@Repository
public interface ProjectionRepository extends JpaRepository<Projection, Integer> {
    /**
     * Retrieves all projections in a program.
     *
     * @param id
     *     the program identifier
     * @return the projections that belong to the program
     */
    List<Projection> findProjectionsByProgramId(int id);

    /**
     * Retrieves all projections for a movie.
     *
     * @param id
     *     the movie identifier
     * @return the projections scheduled for the movie
     */
    List<Projection> findProjectionsByMovieId(int id);

    /**
     * Retrieves all projections that start before the given time.
     *
     * @param time
     *     the exclusive upper start-time bound
     * @return the matching projections
     */
    List<Projection> findProjectionsByStartTimeBefore(LocalTime time);

    /**
     * Retrieves all projections that start after the given time.
     *
     * @param after
     *     the exclusive lower start-time bound
     * @return the matching projections
     */
    List<Projection> findProjectionsByStartTimeAfter(LocalTime after);

    /**
     * Retrieves all projections for a hall whose start time falls within the given interval.
     *
     * @param hallId
     *     the hall identifier
     * @param startTimeBefore
     *     the lower start-time bound
     * @param startTimeAfter
     *     the upper start-time bound
     * @return the overlapping projections for the hall
     */
    List<Projection> findProjectionsByHallIdAndStartTimeBetween(int hallId, LocalTime startTimeBefore,
        LocalTime startTimeAfter);
}


