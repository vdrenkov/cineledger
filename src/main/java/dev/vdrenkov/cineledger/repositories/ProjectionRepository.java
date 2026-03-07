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

    List<Projection> findProjectionsByProgramId(int id);

    List<Projection> findProjectionsByMovieId(int id);

    List<Projection> findProjectionsByStartTimeBefore(LocalTime time);

    List<Projection> findProjectionsByStartTimeAfter(LocalTime after);

    List<Projection> findProjectionsByHallIdAndStartTimeBetween(int hallId, LocalTime startTimeBefore,
        LocalTime startTimeAfter);
}


