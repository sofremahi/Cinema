package com.tiddev.cinema.service.repo;

import com.tiddev.cinema.service.model.SeatsSalon0;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Salon0Repo extends JpaRepository<SeatsSalon0, String> {
    @Query("select s from SeatsSalon0 s ")
    Page<SeatsSalon0> pageOfSeats(Pageable page);
    Optional<SeatsSalon0> findByRowAndColumn(Long row, Long column);
}
