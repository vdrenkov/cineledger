package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.IncomeReportService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Exposes REST endpoints for managing income report data.
 */
@RestController
public class IncomeReportController {
    private static final Logger log = LoggerFactory.getLogger(IncomeReportController.class);

    private final IncomeReportService incomeReportService;

    /**
     * Creates a new income report controller with its required collaborators.
     *
     * @param incomeReportService
     *     income report service used by the operation
     */
    @Autowired
    public IncomeReportController(IncomeReportService incomeReportService) {
        this.incomeReportService = incomeReportService;
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.REPORTS_CINEMAS_ID_INCOMES_PATH)
    public ResponseEntity<Double> getAllIncomesByCinemaId(@PathVariable int id,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final double cinemaIncome = incomeReportService.getAllIncomesByCinemaId(id, startDate, endDate);
        log.info("All incomes by cinema id {} report requested", id);

        return ResponseEntity.ok(cinemaIncome);
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.REPORTS_HALLS_ID_INCOMES_PATH)
    public ResponseEntity<Double> getAllIncomesByHallId(@PathVariable int id,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final double hallIncome = incomeReportService.getAllIncomesByHallId(id, startDate, endDate);
        log.info("All incomes by hall id {} report requested", id);

        return ResponseEntity.ok(hallIncome);
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.REPORTS_ITEMS_ID_INCOMES_PATH)
    public ResponseEntity<Double> getAllIncomesByItemId(@PathVariable int id,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final double itemIncome = incomeReportService.getAllIncomesByItemId(id, startDate, endDate);
        log.info("All incomes by item id {} report requested", id);

        return ResponseEntity.ok(itemIncome);
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.REPORTS_MOVIES_ID_INCOMES_PATH)
    public ResponseEntity<Double> getAllIncomesByMovieId(@PathVariable int id,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final double movieIncome = incomeReportService.getAllIncomesByMovieId(id, startDate, endDate);
        log.info("All incomes by movie id {} report requested", id);

        return ResponseEntity.ok(movieIncome);
    }

    /**
     * Returns all incomes matching the supplied criteria.
     *
     * @param id
     *     identifier of the target resource
     * @param startDate
     *     start date of the requested interval
     * @param endDate
     *     end date of the requested interval
     * @return HTTP response describing the operation result
     */
    @GetMapping(URIConstants.REPORTS_USERS_ID_INCOMES_PATH)
    public ResponseEntity<Double> getAllIncomesByUserId(@PathVariable int id,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final double userIncome = incomeReportService.getAllIncomesByUserId(id, startDate, endDate);
        log.info("All incomes by user id {} report requested", id);

        return ResponseEntity.ok(userIncome);
    }
}


