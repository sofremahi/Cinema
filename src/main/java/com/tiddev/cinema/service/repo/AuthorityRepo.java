package com.tiddev.cinema.service.repo;

import com.tiddev.cinema.service.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepo extends JpaRepository<Authority , Long> {
    List<Authority> findByAuthorityIn(List<String> authorities);
}
