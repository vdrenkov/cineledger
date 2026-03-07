package dev.vdrenkov.cineledger.controllers;

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

import static dev.vdrenkov.cineledger.testUtils.constants.ReportConstants.END_DATE;
import static dev.vdrenkov.cineledger.testUtils.constants.ReportConstants.ID;
import static dev.vdrenkov.cineledger.testUtils.constants.ReportConstants.INCOMES;
import static dev.vdrenkov.cineledger.testUtils.constants.ReportConstants.START_DATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class IncomeReportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IncomeReportService incomeReportService;

    @InjectMocks
    private IncomeReportController incomeReportController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(incomeReportController).build();
    }

    @Test
    public void testGetAllIncomesByCinemaId_doubleReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByCinemaId(anyInt(), any(), any())).thenReturn(INCOMES);

        mockMvc
            .perform(get(URIConstants.REPORTS_CINEMAS_ID_INCOMES_PATH, ID)
                .queryParam("startDate", String.valueOf(START_DATE))
                .queryParam("endDate", String.valueOf(END_DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(INCOMES));
    }

    @Test
    public void testGetAllIncomesByHallId_doubleReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByHallId(anyInt(), any(), any())).thenReturn(INCOMES);

        mockMvc
            .perform(get(URIConstants.REPORTS_HALLS_ID_INCOMES_PATH, ID)
                .queryParam("startDate", String.valueOf(START_DATE))
                .queryParam("endDate", String.valueOf(END_DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(INCOMES));
    }

    @Test
    public void testGetAllIncomesByItemId_doubleReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByItemId(anyInt(), any(), any())).thenReturn(INCOMES);

        mockMvc
            .perform(get(URIConstants.REPORTS_ITEMS_ID_INCOMES_PATH, ID)
                .queryParam("startDate", String.valueOf(START_DATE))
                .queryParam("endDate", String.valueOf(END_DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(INCOMES));
    }

    @Test
    public void testGetAllIncomesByMovieId_doubleReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByMovieId(anyInt(), any(), any())).thenReturn(INCOMES);

        mockMvc
            .perform(get(URIConstants.REPORTS_MOVIES_ID_INCOMES_PATH, ID)
                .queryParam("startDate", String.valueOf(START_DATE))
                .queryParam("endDate", String.valueOf(END_DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(INCOMES));
    }

    @Test
    public void testGetAllIncomesByUserId_doubleReturned_success() throws Exception {
        when(incomeReportService.getAllIncomesByUserId(anyInt(), any(), any())).thenReturn(INCOMES);

        mockMvc
            .perform(get(URIConstants.REPORTS_USERS_ID_INCOMES_PATH, ID)
                .queryParam("startDate", String.valueOf(START_DATE))
                .queryParam("endDate", String.valueOf(END_DATE)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(INCOMES));
    }
}




