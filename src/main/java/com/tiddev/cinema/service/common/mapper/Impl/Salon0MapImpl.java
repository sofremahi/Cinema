package com.tiddev.cinema.service.common.mapper.Impl;

import com.tiddev.cinema.service.common.mapper.Salon0Map;
import com.tiddev.cinema.service.model.SeatsSalon0;
import com.tiddev.cinema.service.modelDto.SeatsSalon0Dto;
import org.springframework.stereotype.Component;

@Component
public class Salon0MapImpl implements Salon0Map {
    @Override
    public SeatsSalon0 dtoToSeat(SeatsSalon0Dto dto) {
        SeatsSalon0 seat = SeatsSalon0.builder().build();
        seat.setColumn(dto.getColumn());
        seat.setRow(dto.getRow());
        seat.setStatus(dto.getStatus());
        seat.setPrice(dto.getPrice());
        return seat;
    }

    @Override
    public SeatsSalon0Dto seatToDto(SeatsSalon0 seat) {
       SeatsSalon0Dto dto =  SeatsSalon0Dto.builder().build();
       dto.setColumn(seat.getColumn());
       dto.setRow(seat.getRow());
       dto.setStatus(seat.getStatus());
       dto.setPrice(seat.getPrice());
       return dto;
    }
}
