package com.tiddev.cinema.service;

import com.tiddev.cinema.service.common.exception.CustomException;
import com.tiddev.cinema.service.common.exception.InsufficientFounds;
import com.tiddev.cinema.service.common.mapper.Salon0Map;
import com.tiddev.cinema.service.constant.SalonsTransaction;
import com.tiddev.cinema.service.constant.SeatStatus;
import com.tiddev.cinema.service.constant.TransactionType;
import com.tiddev.cinema.service.model.Authority;
import com.tiddev.cinema.service.model.SeatsSalon0;
import com.tiddev.cinema.service.model.Transactions;
import com.tiddev.cinema.service.model.User;
import com.tiddev.cinema.service.modelDto.SeatsSalon0Dto;
import com.tiddev.cinema.service.repo.AuthorityRepo;
import com.tiddev.cinema.service.repo.Salon0Repo;
import com.tiddev.cinema.service.repo.TransRepo;
import com.tiddev.cinema.service.repo.UserRepo;
import com.tiddev.cinema.service.security.PasswordEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Salon0Service {
    private final Salon0Repo repository;
    public final Salon0Map mapper;
    private final UserRepo userRepo;
    private final TransRepo transRepo;
    private final AuthorityRepo authorityRepo;
    private final Salon0Repo salon0Repo;

    @Transactional
    public void addSeat(SeatsSalon0Dto dto) {
        SeatsSalon0 seat = mapper.dtoToSeat(dto);
        repository.save(seat);

    }

    public Page<SeatsSalon0Dto> seatsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SeatsSalon0> seatsPage = repository.pageOfSeats(pageable);

        return seatsPage.map(mapper::seatToDto);
    }

    public SeatsSalon0Dto getSeat(String id) {
        SeatsSalon0 seat = repository.findById(id).orElseThrow(
                () -> new CustomException("seat with id doesnt exist")
        );
        return mapper.seatToDto(seat);
    }

    @Transactional()
    public void buyTicket(String id, String username) {

        SeatsSalon0 seat = repository.findById(id).orElseThrow(
                () -> new CustomException("seat with id doesnt exist")
        );
        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new CustomException("user not found")
        );
        if (seat.getStatus().equals(SeatStatus.SOLD)) {
            throw new CustomException("seat is already sold");
        }
        if (user.getAccountAmount().subtract(seat.getPrice()).compareTo(BigDecimal.ONE) <= 0) {

            throw new InsufficientFounds("insufficient funds");
        }
        user.setAccountAmount(user.getAccountAmount().subtract(seat.getPrice()));
        userRepo.save(user);
        seat.setUser(user);
        seat.setStatus(SeatStatus.SOLD);
        repository.save(seat);
        createTransaction(SalonsTransaction.Salon0,
                TransactionType.Buy_ticket,
                seat.getPrice(),
                true,
                username
        );
    }

    @Transactional()
    public void discardSeat(String id, String username) {
        SeatsSalon0 seat = repository.findById(id).orElseThrow(
                () -> new CustomException("seat with id doesnt exist")
        );
        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new CustomException("user not found")
        );
        if (seat.getStatus().equals(SeatStatus.NOT_SOLD)) {
            log.info("seat is open for");
            throw new CustomException("seat is already discarded");
        }
        if (!seat.getUser().getId().equals(user.getId())) {
            throw new CustomException("You are not allowed to discard this seat");
        }
        user.setAccountAmount(user.getAccountAmount().add(seat.getPrice().multiply(BigDecimal.valueOf(0.8))));
        userRepo.save(user);
        seat.setUser(null);
        seat.setStatus(SeatStatus.NOT_SOLD);
        repository.save(seat);
        createTransaction(SalonsTransaction.Salon0,
                TransactionType.Discard_Ticket,
                seat.getPrice(),
                true,
                username
        );
    }

    public User getUser(String username) {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new CustomException("user not found")
        );
    }

    @Transactional
    public void createTransaction(SalonsTransaction salon,
                                  TransactionType type,
                                  BigDecimal amount,
                                  Boolean isSuccessful,
                                  String username) {
        Transactions transaction = Transactions.builder().build();
        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new CustomException("user not found")
        );
        transaction.setSalon(salon);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setIsSuccessful(isSuccessful);
        transaction.setUser(user);
        transRepo.save(transaction);
    }

    public Page<Transactions> transactions(String username) {
        Pageable pageable = PageRequest.of(0, 10);
        return transRepo.findByUserUsername(username, pageable);
    }

    public List<Transactions> getLast10Transactions(String username) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transactions> transactionsPage = transRepo.findByUserUsername(username, pageable);
        return transactionsPage.getContent(); // list
    }

    @Transactional
    public void addUser(String username, String password, List<String> authorities) {
        if (password.isEmpty()) {
            throw new CustomException("password must not be empty");
        }
        if(userRepo.findByUsername(username).isPresent()){
            throw new CustomException("user already exists");
        }
        if (username.isBlank()) {
            throw new CustomException("username must not be empty");
        }
        if (password.isBlank()) {
            throw new CustomException("password must not be empty");
        }
        List<Authority> auth = authorityRepo.findByAuthorityIn(authorities);
        if (auth.isEmpty()) {
            throw new CustomException("authority not found");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setAccountAmount(BigDecimal.ZERO);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setAuthorities(auth);
        userRepo.save(user);
    }

    public List<SeatsSalon0> userSeats(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new CustomException("user not found")
        );
        return user.getSeats();
    }
    public Boolean findByUsername(String username) {
        return userRepo.findByUsername(username).isPresent();
    }
}
