package com.tiddev.cinema.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiddev.cinema.service.Salon0Service;
import com.tiddev.cinema.service.common.exception.CustomException;
import com.tiddev.cinema.service.common.respone.Response;
import com.tiddev.cinema.service.modelDto.SeatsSalon0Dto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest

class Salon0ControllerTest {
@Autowired
    private MockMvc mockMvc;
@Autowired
    private ObjectMapper objectMapper;
@Autowired
    private Salon0Service salon0Service;

//    @PostMapping("/add/seat")
//    public Response<String> addSeat(@RequestBody SeatsSalon0Dto dto) {
//        try {
//            service.addSeat(dto);
//            return new Response<>("seat added", HttpStatus.OK);
//        } catch (DataIntegrityViolationException ex) {
//            throw new CustomException("enter the data with a greater aspect with database");
//        } catch (Exception e) {
//            throw new CustomException("some thing went wrong");
//        }
//    }

@Test
public void given_when_then() {
    // Arrange


    // Act


    // Assert


}
}