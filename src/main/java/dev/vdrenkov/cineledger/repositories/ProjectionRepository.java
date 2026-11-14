package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Projection;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select entity from Projection entity where entity.id = :id")
    Optional<Projection> findByIdForUpdate(int id);

    @Query(
            value =
                    "SELECT EXISTS (SELECT 1 FROM projections projection JOIN programs program ON"
                        + " program.id = projection.program_id WHERE projection.hall_id = :hallId"
                        + " AND projection.id <> :excludedId AND program.program_date +"
                        + " projection.start_time < :endsAt AND program.program_date +"
                        + " projection.start_time + (projection.runtime / 1000000000.0) * interval"
                        + " '1 second' > :startsAt)",
            nativeQuery = true)
    boolean overlaps(
            int hallId,
            int excludedId,
            java.time.LocalDateTime startsAt,
            java.time.LocalDateTime endsAt);

    List<Projection> findProjectionsByProgramId(int id);

    List<Projection> findProjectionsByMovieId(int id);

    List<Projection> findProjectionsByStartTimeBefore(LocalTime time);

    List<Projection> findProjectionsByStartTimeAfter(LocalTime after);

    List<Projection> findProjectionsByHallIdAndStartTimeBetween(
            int hallId, LocalTime startTimeBefore, LocalTime startTimeAfter);
}
