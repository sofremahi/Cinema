package com.tiddev.cinema.service.repo;


import com.tiddev.cinema.service.model.SeatsSalon0;
import com.tiddev.cinema.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username );
}
