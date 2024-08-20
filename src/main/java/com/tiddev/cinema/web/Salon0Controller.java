package com.tiddev.cinema.web;

import com.tiddev.cinema.service.Salon0Service;
import com.tiddev.cinema.service.common.exception.CustomException;
import com.tiddev.cinema.service.common.exception.InsufficientFounds;
import com.tiddev.cinema.service.common.respone.Response;
import com.tiddev.cinema.service.constant.SalonsTransaction;
import com.tiddev.cinema.service.constant.TransactionType;
import com.tiddev.cinema.service.model.SeatsSalon0;
import com.tiddev.cinema.service.model.Transactions;
import com.tiddev.cinema.service.model.User;
import com.tiddev.cinema.service.modelDto.SeatsSalon0Dto;
import com.tiddev.cinema.service.repo.Salon0Repo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/cinema/salon0")
public class Salon0Controller {
    private final Salon0Service service;
    private final Salon0Repo repository;

    @PostMapping("/add/seat")
    public Response<String> addSeat(@RequestBody SeatsSalon0Dto dto) {
        try {
            service.addSeat(dto);
            return new Response<>("seat added", HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            throw new CustomException("enter the data with a greater aspect with database");
        } catch (Exception e) {
            throw new CustomException("some thing went wrong");
        }
    }

    @GetMapping("/seats/page")
    public Response<Page<SeatsSalon0Dto>> seatsPage(@RequestParam(required = false, defaultValue = "0")
                                                    int page,
                                                    @RequestParam(required = false, defaultValue = "10")
                                                    int size) {
        Page<SeatsSalon0Dto> seatPage = service.seatsPage(page, size);
        return new Response<>(seatPage, "Success", HttpStatus.OK);
    }

    @PutMapping("/buy/ticket/{id}")
    public Response<String> buyTicket(@PathVariable("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            service.buyTicket(id, username);
            return new Response<>("ticket purchased successfully ", HttpStatus.OK);
        } catch (InsufficientFounds e) {
            service.createTransaction(SalonsTransaction.Salon0,
                    TransactionType.Buy_ticket,
                    service.getSeat(id).getPrice(),
                    false,
                    username
            );
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @PutMapping("/discard/ticket/{id}")
    public Response<String> discardSeat(@PathVariable("id") String id) {
        System.out.println("Received seat ID: " + id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            service.discardSeat(id, username);
            return new Response<>("ticket discarded successfully ", HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/get/user")
    public Response<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Authenticated user: " + username);
        return new Response<>(service.getUser(username));
    }

    @GetMapping("/get/transactions")
    public Response<Page<Transactions>> getTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new Response<>(service.transactions(username));
    }

    @GetMapping("/get/user/details")
    public Response<Map<String, Object>> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = service.getUser(username);
        List<Transactions> transactions = service.getLast10Transactions(username);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("transactions", transactions);

        return new Response<>(response);
    }

    @PostMapping("/add/user")
    public Response<String> addUser(@RequestParam String username,
                                    @RequestParam String password) {
        List<String> authorities = List.of("USER");
        service.addUser(username, password, authorities);
        return new Response<>("success", HttpStatus.OK);
    }

    @GetMapping("/get/user/seats")
    public Response<List<SeatsSalon0>> userSeats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new Response<>(service.userSeats(username), "Success", HttpStatus.OK);
    }

    @GetMapping("/seats/{row}/{column}")
    public Response<SeatsSalon0> getSeatByRowColumn(@PathVariable("row") Long row, @PathVariable("column") Long column) {
        SeatsSalon0 seat = repository.findByRowAndColumn(row, column)
                .orElseThrow(() -> new CustomException("Seat not found"));
        return new Response<>(seat, "Seat retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/find/user")
    public Response<Boolean> findUser(@RequestParam String username) {
        return new Response<>(service.findByUsername(username), "Success", HttpStatus.OK);
    }

}
