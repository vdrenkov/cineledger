package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.IncomeReportService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
public class IncomeReportController {

    private final IncomeReportService incomeReportService;

    public IncomeReportController(IncomeReportService incomeReportService) {
        this.incomeReportService = incomeReportService;
    }

    @GetMapping(URIConstants.REPORTS_CINEMAS_ID_INCOMES_PATH)
    public ResponseEntity<BigDecimal> getAllIncomesByCinemaId(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final BigDecimal cinemaIncome =
                incomeReportService.getAllIncomesByCinemaId(id, startDate, endDate);

        return ResponseEntity.ok(cinemaIncome);
    }

    @GetMapping(URIConstants.REPORTS_HALLS_ID_INCOMES_PATH)
    public ResponseEntity<BigDecimal> getAllIncomesByHallId(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final BigDecimal hallIncome =
                incomeReportService.getAllIncomesByHallId(id, startDate, endDate);

        return ResponseEntity.ok(hallIncome);
    }

    @GetMapping(URIConstants.REPORTS_ITEMS_ID_INCOMES_PATH)
    public ResponseEntity<BigDecimal> getAllIncomesByItemId(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final BigDecimal itemIncome =
                incomeReportService.getAllIncomesByItemId(id, startDate, endDate);

        return ResponseEntity.ok(itemIncome);
    }

    @GetMapping(URIConstants.REPORTS_MOVIES_ID_INCOMES_PATH)
    public ResponseEntity<BigDecimal> getAllIncomesByMovieId(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final BigDecimal movieIncome =
                incomeReportService.getAllIncomesByMovieId(id, startDate, endDate);

        return ResponseEntity.ok(movieIncome);
    }

    @GetMapping(URIConstants.REPORTS_USERS_ID_INCOMES_PATH)
    public ResponseEntity<BigDecimal> getAllIncomesByUserId(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final BigDecimal userIncome =
                incomeReportService.getAllIncomesByUserId(id, startDate, endDate);

        return ResponseEntity.ok(userIncome);
    }
}
