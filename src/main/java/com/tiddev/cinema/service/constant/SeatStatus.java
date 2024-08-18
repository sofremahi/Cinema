package com.tiddev.cinema.service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SeatStatus {
  NOT_SOLD((byte) 0 ,  "Not Sold"),
  SOLD((byte) 1,"Sold")
    ;
  private final byte code;
  private final String title;
}
