package bg.vdrenkov.cineledger.repositories;

import bg.vdrenkov.cineledger.models.entities.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Integer> {

  List<Hall> findAllByCinemaId(int cinemaId);
}


