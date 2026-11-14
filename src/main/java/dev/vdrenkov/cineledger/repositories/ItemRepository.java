package dev.vdrenkov.cineledger.repositories;

import dev.vdrenkov.cineledger.models.entities.Item;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select entity from Item entity where entity.id = :id")
    Optional<Item> findByIdForUpdate(int id);

    Optional<Item> findByName(String itemName);
}
