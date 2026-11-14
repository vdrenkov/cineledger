package dev.vdrenkov.cineledger.controllers;

import static dev.vdrenkov.cineledger.testutils.constants.ReportConstants.END_DATE;
import static dev.vdrenkov.cineledger.testutils.constants.ReportConstants.ID;
import static dev.vdrenkov.cineledger.testutils.constants.ReportConstants.INCOMES;
import static dev.vdrenkov.cineledger.testutils.constants.ReportConstants.START_DATE;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.vdrenkov.cineledger.services.IncomeReportService;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class IncomeReportControllerTest {
    private MockMvc mockMvc;

    @Mock private IncomeReportService incomeReportService;

    @InjectMocks private IncomeReportController incomeReportController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(incomeReportController).build();
    }

    @Test
    void testGetAllIncomesByCinemaId_amountReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByCinemaId(anyInt(), any(), any()))
                .thenReturn(INCOMES);

        mockMvc.perform(
                        get(URIConstants.REPORTS_CINEMAS_ID_INCOMES_PATH, ID)
                                .queryParam("startDate", String.valueOf(START_DATE))
                                .queryParam("endDate", String.valueOf(END_DATE)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$")
                                .value(
                                        org.hamcrest.Matchers.comparesEqualTo(INCOMES),
                                        BigDecimal.class));
    }

    @Test
    void testGetAllIncomesByHallId_amountReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByHallId(anyInt(), any(), any())).thenReturn(INCOMES);

        mockMvc.perform(
                        get(URIConstants.REPORTS_HALLS_ID_INCOMES_PATH, ID)
                                .queryParam("startDate", String.valueOf(START_DATE))
                                .queryParam("endDate", String.valueOf(END_DATE)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$")
                                .value(
                                        org.hamcrest.Matchers.comparesEqualTo(INCOMES),
                                        BigDecimal.class));
    }

    @Test
    void testGetAllIncomesByItemId_amountReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByItemId(anyInt(), any(), any())).thenReturn(INCOMES);

        mockMvc.perform(
                        get(URIConstants.REPORTS_ITEMS_ID_INCOMES_PATH, ID)
                                .queryParam("startDate", String.valueOf(START_DATE))
                                .queryParam("endDate", String.valueOf(END_DATE)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$")
                                .value(
                                        org.hamcrest.Matchers.comparesEqualTo(INCOMES),
                                        BigDecimal.class));
    }

    @Test
    void testGetAllIncomesByMovieId_amountReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByMovieId(anyInt(), any(), any()))
                .thenReturn(INCOMES);

        mockMvc.perform(
                        get(URIConstants.REPORTS_MOVIES_ID_INCOMES_PATH, ID)
                                .queryParam("startDate", String.valueOf(START_DATE))
                                .queryParam("endDate", String.valueOf(END_DATE)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$")
                                .value(
                                        org.hamcrest.Matchers.comparesEqualTo(INCOMES),
                                        BigDecimal.class));
    }

    @Test
    void testGetAllIncomesByUserId_amountReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByUserId(anyInt(), any(), any())).thenReturn(INCOMES);

        mockMvc.perform(
                        get(URIConstants.REPORTS_USERS_ID_INCOMES_PATH, ID)
                                .queryParam("startDate", String.valueOf(START_DATE))
                                .queryParam("endDate", String.valueOf(END_DATE)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$")
                                .value(
                                        org.hamcrest.Matchers.comparesEqualTo(INCOMES),
                                        BigDecimal.class));
    }
}
