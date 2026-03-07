package bg.vdrenkov.cineledger.repositories;

import bg.vdrenkov.cineledger.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findRoleByName(String name);

    boolean existsByName(String name);
}


