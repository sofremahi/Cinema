package com.tiddev.cinema.integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiddev.cinema.service.modelDto.SeatsSalon0Dto;

import com.tiddev.cinema.service.Salon0Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class Salon0ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Salon0Service service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void givenSeatDto_whenAddSeat_thenReturnSuccessResponse() throws Exception {
        // Arrange
        SeatsSalon0Dto seatDto = new SeatsSalon0Dto();
        // Set properties of seatDto as needed for the test

        BDDMockito.doNothing().when(service).addSeat(BDDMockito.any(SeatsSalon0Dto.class));

        // Act & Assert
        mockMvc.perform(post("/rest/cinema/salon0/add/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seatDto)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

    }
}
