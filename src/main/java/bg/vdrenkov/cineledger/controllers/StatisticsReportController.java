package bg.vdrenkov.cineledger.controllers;

import bg.vdrenkov.cineledger.services.StatisticsReportService;
import bg.vdrenkov.cineledger.utils.constants.URIConstants;
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

@RestController
public class StatisticsReportController {

  private static final Logger log = LoggerFactory.getLogger(StatisticsReportController.class);

  private final StatisticsReportService statisticsReportService;

  @Autowired
  public StatisticsReportController(StatisticsReportService statisticsReportService) {
    this.statisticsReportService = statisticsReportService;
  }

  @GetMapping(URIConstants.REPORTS_MOVIES_CATEGORIES_ID_TICKETS_COUNT_PATH)
  public ResponseEntity<Integer> getPurchasedTicketsCountByMovieCategory(
    @PathVariable int id,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    int movieCategoryTicketCount =
      statisticsReportService.getPurchasedTicketsCountByMovieCategory(id, startDate, endDate);
    log.info("Received request to get purchased tickets count by movie category");

    return ResponseEntity.ok(movieCategoryTicketCount);
  }

  @GetMapping(URIConstants.REPORTS_MOVIES_TICKETS_COUNT_PATH)
  public ResponseEntity<Integer> getPurchasedTicketsCountByMovieTitle(
    @RequestParam String title,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    int movieTitleTicketCount = statisticsReportService.getPurchasedTicketsCountByMovieTitle(title, startDate, endDate);
    log.info("Received request to get purchased tickets count by movie title");

    return ResponseEntity.ok(movieTitleTicketCount);
  }

  @GetMapping(URIConstants.REPORTS_ITEMS_ITEMS_COUNT_PATH)
  public ResponseEntity<Integer> getPurchasedItemsCountByItemName(
    @RequestParam String name,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

    int itemNameCount = statisticsReportService.getPurchasedItemsCountByItemName(name, startDate, endDate);
    log.info("Received request to get purchased tickets count by movie title");

    return ResponseEntity.ok(itemNameCount);
  }
}


