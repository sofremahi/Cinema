package com.tiddev.cinema.service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tiddev.cinema.service.constant.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
@Getter
@Setter
@Table(name = "tbl_salon0" ,
        uniqueConstraints = {@UniqueConstraint(columnNames = {"SEAT_ROW", "SEAT_COLUMN"})})
@Entity
public class SeatsSalon0 {
    @Id
    @UuidGenerator
    @Column(name = "SEAT_ID")
    private String id;
    @Column(name = "SEAT_ROW")
    private Long row;
    @Column(name = "SEAT_COLUMN")
    private Long column;
    @Column(name = "SEAT_PRICE")
    private BigDecimal price;
    @Column(name = "SEAT_STATUS")
    @Enumerated(EnumType.ORDINAL)
    private SeatStatus status;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference
    private User user;
}
