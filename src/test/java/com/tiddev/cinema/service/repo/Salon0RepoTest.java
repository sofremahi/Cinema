package com.tiddev.cinema.service.repo;

import com.tiddev.cinema.service.model.SeatsSalon0;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
public class Salon0RepoTest {

    @Autowired
    private Salon0Repo salon0Repo;

    //Junit test for
    @Test
    public void givenRowAndColumn_whenFind_thenReturnSalon0() {
        //given : setup
        SeatsSalon0 seat = SeatsSalon0.builder().column(2L).row(2L).build();
 Long row = 2L;
 Long column = 2L;
 salon0Repo.save(seat);
        //when : action we are going to test
Optional<SeatsSalon0> check = salon0Repo.findByRowAndColumn(row , column);
        // verify the output
        Assertions.assertTrue(check.isPresent());
    }
}