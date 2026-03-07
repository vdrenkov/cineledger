package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.StatisticsReportService;
import dev.vdrenkov.cineledger.testutil.constants.ItemConstants;
import dev.vdrenkov.cineledger.testutil.constants.MovieConstants;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static dev.vdrenkov.cineledger.testutil.constants.MovieConstants.ID;
import static dev.vdrenkov.cineledger.testutil.constants.ReportConstants.END_DATE;
import static dev.vdrenkov.cineledger.testutil.constants.ReportConstants.START_DATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests statistics report controller behavior.
 */
@ExtendWith(MockitoExtension.class)
class StatisticsReportControllerTest {
    private MockMvc mockMvc;

    @Mock
    private StatisticsReportService statisticsReportService;

    @InjectMocks
    private StatisticsReportController statisticsReportController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsReportController).build();
    }

    /**
     * Verifies that get Purchased Tickets Count By Movie Category integer Returned success.
     */
    @Test
    void testGetPurchasedTicketsCountByMovieCategory_integerReturned_success() throws Exception {
        when(statisticsReportService.getPurchasedTicketsCountByMovieCategory(anyInt(), any(), any())).thenReturn(1000);

        mockMvc
            .perform(get(URIConstants.REPORTS_MOVIES_CATEGORIES_ID_TICKETS_COUNT_PATH, ID)
                .queryParam("startDate", String.valueOf(START_DATE))
                .queryParam("endDate", String.valueOf(END_DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1000));
    }

    /**
     * Verifies that get Purchased Tickets Count By Movie Title integer Returned success.
     */
    @Test
    void testGetPurchasedTicketsCountByMovieTitle_integerReturned_success() throws Exception {
        when(statisticsReportService.getPurchasedTicketsCountByMovieTitle(anyString(), any(), any())).thenReturn(10);

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.REPORTS_MOVIES_TICKETS_COUNT_PATH)
                .queryParam("title", MovieConstants.TITLE)
                .queryParam("startDate", String.valueOf(START_DATE))
                .queryParam("endDate", String.valueOf(END_DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(10));
    }

    /**
     * Verifies that get Purchased Items Count By Item Name integer Returned success.
     */
    @Test
    void testGetPurchasedItemsCountByItemName_integerReturned_success() throws Exception {
        when(statisticsReportService.getPurchasedItemsCountByItemName(anyString(), any(), any())).thenReturn(10);

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(URIConstants.REPORTS_ITEMS_ITEMS_COUNT_PATH)
                .queryParam("name", ItemConstants.NAME)
                .queryParam("startDate", String.valueOf(START_DATE))
                .queryParam("endDate", String.valueOf(END_DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(10));
    }
}



