package com.tiddev.cinema.service.repo;

import com.tiddev.cinema.service.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void givenUserName_whenCheckingForUser_then() {
        User newUser = User.builder().username("ali").password("123456").build();
        userRepo.save(newUser);
        Assertions.assertTrue(userRepo.findByUsername("ali").isPresent());
    }
}
