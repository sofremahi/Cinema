package com.tiddev.cinema.service;


import com.tiddev.cinema.service.common.exception.CustomException;
import com.tiddev.cinema.service.common.exception.InsufficientFounds;
import com.tiddev.cinema.service.common.mapper.Impl.Salon0MapImpl;
import com.tiddev.cinema.service.constant.SalonsTransaction;
import com.tiddev.cinema.service.constant.SeatStatus;
import com.tiddev.cinema.service.constant.TransactionType;
import com.tiddev.cinema.service.model.SeatsSalon0;
import com.tiddev.cinema.service.model.Transactions;
import com.tiddev.cinema.service.model.User;
import com.tiddev.cinema.service.modelDto.SeatsSalon0Dto;
import com.tiddev.cinema.service.repo.Salon0Repo;
import com.tiddev.cinema.service.repo.TransRepo;
import com.tiddev.cinema.service.repo.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class Salon0ServiceTest {
    @Mock
    private Salon0Repo salon0Repo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private Salon0MapImpl map;
    @Mock
    private TransRepo transRepo;
    @InjectMocks
    private Salon0Service service;

    private SeatsSalon0 salon;
    private SeatsSalon0Dto salonDto;
    private User user;
    private Transactions transaction;

    @BeforeEach
    void setUp() {
        salon = SeatsSalon0.builder()
                .id("1")
                .row(2L)
                .price(BigDecimal.valueOf(500L))
                .column(2L)
                .status(SeatStatus.NOT_SOLD)
                .build();

        user = User.builder()
                .id(1L)
                .username("testUser")
                .accountAmount(BigDecimal.valueOf(1000L))
                .build();

        transaction = Transactions.builder()
                .salon(SalonsTransaction.Salon0)
                .type(TransactionType.Buy_ticket)
                .amount(salon.getPrice())
                .isSuccessful(true)
                .user(user)
                .build();
    }

    @Test
    public void givenStringSeatId_whenFind_thenReturnSeat() {
        // Arrange
        given(salon0Repo.findById(salon.getId())).willReturn(Optional.of(salon));
        given(map.seatToDto(salon)).willReturn(salonDto);

        // Act
        SeatsSalon0Dto seat = service.getSeat(salon.getId());

        // Assert
        assertEquals(salonDto, seat);
        verify(salon0Repo, times(1)).findById(salon.getId());
    }

    //Junit test for add seat
    @Test
    public void givenSeatDto_whenAddSeat_thenSave() {
        given(map.dtoToSeat(salonDto)).willReturn(salon);
        given(salon0Repo.save(salon)).willReturn(salon);

        service.addSeat(salonDto);

        verify(salon0Repo, times(1)).save(salon);
    }

    @Test
    public void givenStringIdAndUsername_whenBuyingTicket_thenCheckForCalls() {
        transaction.setUser(user);
        given(salon0Repo.findById(salon.getId())).willReturn(Optional.of(salon));
        given(userRepo.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(userRepo.save(user)).willReturn(user);
        given(salon0Repo.save(salon)).willReturn(salon);
        given(transRepo.save(Mockito.any(Transactions.class))).willReturn(transaction);

        service.buyTicket(salon.getId(), user.getUsername());

        verify(salon0Repo, times(1)).save(salon);
        verify(userRepo, times(1)).save(user);
        verify(transRepo, times(1)).save(Mockito.any(Transactions.class));
    }

    @Test
    public void givenInvalidSeatId_whenBuyTicket_thenThrowException() {
        // Arrange
        given(salon0Repo.findById(salon.getId())).willReturn(Optional.empty());
        Assertions.assertThrows(Exception.class, () ->
                service.buyTicket(salon.getId(), user.getUsername())
        );
    }

    @Test
    public void givenInvalidUsername_whenBuyTicket_thenThrowException() {

        given(salon0Repo.findById(salon.getId())).willReturn(Optional.of(salon));
        given(userRepo.findByUsername(user.getUsername())).willReturn(Optional.empty());
        Assertions.assertThrows(Exception.class, () ->
                service.buyTicket(salon.getId(), user.getUsername()));
        verify(salon0Repo, never()).save(any(SeatsSalon0.class));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void givenSoldSeat_whenBuying_thenThrowException() {
        salon.setStatus(SeatStatus.SOLD);
        given(salon0Repo.findById(salon.getId())).willReturn(Optional.of(salon));
        given(userRepo.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        Assertions.assertThrows(Exception.class,
                () -> service.buyTicket(salon.getId(), user.getUsername()));
    }

    @Test
    public void givenNoAccAmount_whenBuying_thenThrowException() {
        user.setAccountAmount(BigDecimal.ZERO);
        given(salon0Repo.findById(salon.getId())).willReturn(Optional.of(salon));
        given(userRepo.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        Assertions.assertThrows(InsufficientFounds.class, () ->
                service.buyTicket(salon.getId(), user.getUsername()));
        verify(salon0Repo, never()).save(any(SeatsSalon0.class));
    }

    @Test
    public void givenSeatIdAndUsername_whenDiscardTicket_thenCheckCalls() {
        salon.setStatus(SeatStatus.SOLD);
        salon.setUser(user);
        transaction.setType(TransactionType.Discard_Ticket);
        transaction.setUser(user);
        // Arrange
        given(salon0Repo.findById(salon.getId())).willReturn(Optional.of(salon));
        given(userRepo.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(userRepo.save(user)).willReturn(user);
        given(salon0Repo.save(salon)).willReturn(salon);
        given(transRepo.save(Mockito.any(Transactions.class))).willReturn(transaction);

        // Act
        service.discardSeat(salon.getId(), user.getUsername());


        // Assert
        verify(salon0Repo, times(1)).save(salon);
        verify(userRepo, times(1)).save(user);
        verify(transRepo, times(1)).save(Mockito.any(Transactions.class));

    }

    @Test
    public void givenUsername_whenGetTransactions_thenVerify() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Transactions> mockTransactions = Arrays.asList(
                Transactions.builder().id("1").type(TransactionType.Buy_ticket).build(),
                Transactions.builder().id("2").type(TransactionType.Discard_Ticket).build()
        );
        Page<Transactions> transactionsPage = new PageImpl<>(mockTransactions, pageable, mockTransactions.size());
        given(transRepo.findByUserUsername(user.getUsername(), pageable)).willReturn(transactionsPage);
        List<Transactions> transactions = service.getLast10Transactions(user.getUsername());
        assertEquals(2, transactions.size());
        assertEquals("1", transactions.get(0).getId());
        assertEquals("2", transactions.get(1).getId());
        Mockito.verify(transRepo, Mockito.times(1))
                .findByUserUsername(user.getUsername(), pageable);

    }
}
