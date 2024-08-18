package com.tiddev.cinema.service.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tiddev.cinema.service.constant.SalonsTransaction;
import com.tiddev.cinema.service.constant.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "tbl_transactions")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;
    @Column(name = "salon")
    private SalonsTransaction salon;
    @Column(name = "type")
    private TransactionType type;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "success_status")
    private Boolean isSuccessful;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonBackReference
    private User user;
}
