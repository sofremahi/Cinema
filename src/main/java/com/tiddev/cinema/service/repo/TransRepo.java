package com.tiddev.cinema.service.repo;
import com.tiddev.cinema.service.model.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransRepo extends JpaRepository<Transactions , String> {
    @Query("select t from Transactions t join" +
            " User u on u.id = t.user.id " +
            "where t.user.username = :username " +
            "ORDER BY t.createdDate DESC")
    Page<Transactions> findByUserUsername(@Param("username") String username, Pageable pageable);
}
