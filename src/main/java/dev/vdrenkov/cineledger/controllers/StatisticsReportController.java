package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.StatisticsReportService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class StatisticsReportController {

    private final StatisticsReportService statisticsReportService;

    public StatisticsReportController(StatisticsReportService statisticsReportService) {
        this.statisticsReportService = statisticsReportService;
    }

    @GetMapping(URIConstants.REPORTS_MOVIES_CATEGORIES_ID_TICKETS_COUNT_PATH)
    public ResponseEntity<Long> getPurchasedTicketsCountByMovieCategory(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        long movieCategoryTicketCount =
                statisticsReportService.getPurchasedTicketsCountByMovieCategory(
                        id, startDate, endDate);

        return ResponseEntity.ok(movieCategoryTicketCount);
    }

    @GetMapping(URIConstants.REPORTS_MOVIES_TICKETS_COUNT_PATH)
    public ResponseEntity<Long> getPurchasedTicketsCountByMovieTitle(
            @RequestParam String title,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        long movieTitleTicketCount =
                statisticsReportService.getPurchasedTicketsCountByMovieTitle(
                        title, startDate, endDate);

        return ResponseEntity.ok(movieTitleTicketCount);
    }

    @GetMapping(URIConstants.REPORTS_ITEMS_ITEMS_COUNT_PATH)
    public ResponseEntity<Long> getPurchasedItemsCountByItemName(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        final long itemNameCount =
                statisticsReportService.getPurchasedItemsCountByItemName(name, startDate, endDate);

        return ResponseEntity.ok(itemNameCount);
    }
}
