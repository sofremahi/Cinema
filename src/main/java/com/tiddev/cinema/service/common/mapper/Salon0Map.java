package com.tiddev.cinema.service.common.mapper;

import com.tiddev.cinema.service.model.SeatsSalon0;
import com.tiddev.cinema.service.modelDto.SeatsSalon0Dto;

public interface Salon0Map {
    SeatsSalon0 dtoToSeat(SeatsSalon0Dto dto);
    SeatsSalon0Dto seatToDto(SeatsSalon0 seat);
}
