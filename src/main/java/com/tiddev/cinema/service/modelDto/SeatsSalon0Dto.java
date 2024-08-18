package com.tiddev.cinema.service.modelDto;
import com.tiddev.cinema.service.constant.SeatStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class SeatsSalon0Dto {
    private Long row;

    private Long column;

    private BigDecimal price;

    private SeatStatus status;

}
