package dev.vdrenkov.cineledger.controllers;

import dev.vdrenkov.cineledger.services.ReviewService;
import dev.vdrenkov.cineledger.testutil.constants.CinemaConstants;
import dev.vdrenkov.cineledger.testutil.constants.MovieConstants;
import dev.vdrenkov.cineledger.testutil.constants.UserConstants;
import dev.vdrenkov.cineledger.testutil.factories.CinemaFactory;
import dev.vdrenkov.cineledger.testutil.factories.ReviewFactory;
import dev.vdrenkov.cineledger.utils.constants.URIConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.DATE_MODIFIED;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.ID;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.RATING;
import static dev.vdrenkov.cineledger.testutil.constants.ReviewConstants.REVIEW_TEXT;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests review controller behavior.
 */
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ReviewControllerTest {
    private static final String REVIEW_ID_PATH_PLACEHOLDER = "/reviews/1";
    private static final String RETURN_OLD = "returnOld";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    /**
     * Verifies that add Cinema Review review Added success.
     */
    @Test
    void testAddCinemaReview_reviewAdded_success() throws Exception {
        when(reviewService.addCinemaReview(any(), anyInt())).thenReturn(ReviewFactory.getDefaultReview());
        final String json = objectMapper.writeValueAsString(ReviewFactory.getDefaultReviewRequest());

        mockMvc
            .perform(post(URIConstants.CINEMAS_ID_REVIEWS_PATH, CinemaConstants.ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", REVIEW_ID_PATH_PLACEHOLDER));
    }

    /**
     * Verifies that add Movie Review review Added success.
     */
    @Test
    void testAddMovieReview_reviewAdded_success() throws Exception {
        when(reviewService.addMovieReview(any(), anyInt())).thenReturn(ReviewFactory.getDefaultReview());
        final String json = objectMapper.writeValueAsString(ReviewFactory.getDefaultReviewRequest());

        mockMvc
            .perform(post(URIConstants.MOVIES_ID_REVIEWS_PATH, MovieConstants.ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", REVIEW_ID_PATH_PLACEHOLDER));
    }

    /**
     * Verifies that get Reviews By Cinema Id no Exceptions success.
     */
    @Test
    void testGetReviewsByCinemaId_noExceptions_success() throws Exception {
        when(reviewService.getReviewsByCinemaId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDtoList());

        mockMvc
            .perform(get(URIConstants.CINEMAS_ID_REVIEWS_PATH, CinemaConstants.ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].rating").value(RATING))
            .andExpect(jsonPath("$[0].reviewText").value(REVIEW_TEXT))
            .andExpect(jsonPath("$[0].dateModified", is(DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$[0].movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$[0].cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$[0].user.id").value(UserConstants.ID))
            .andExpect(jsonPath("$[0].user.username").value(UserConstants.USERNAME))
            .andExpect(jsonPath("$[0].user.email").value(UserConstants.EMAIL))
            .andExpect(jsonPath("$[0].user.firstName").value(UserConstants.FIRST_NAME))
            .andExpect(jsonPath("$[0].user.lastName").value(UserConstants.LAST_NAME))
            .andExpect(jsonPath("$[0].user.joinDate").value(UserConstants.JOIN_DATE.toString()));
    }

    /**
     * Verifies that get Reviews By Movie Id no Exceptions success.
     */
    @Test
    void testGetReviewsByMovieId_noExceptions_success() throws Exception {
        when(reviewService.getReviewsByMovieId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDtoList());

        mockMvc
            .perform(get(URIConstants.MOVIES_ID_REVIEWS_PATH, MovieConstants.ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].rating").value(RATING))
            .andExpect(jsonPath("$[0].reviewText").value(REVIEW_TEXT))
            .andExpect(jsonPath("$[0].dateModified", is(DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$[0].movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$[0].cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$[0].user.id").value(UserConstants.ID))
            .andExpect(jsonPath("$[0].user.username").value(UserConstants.USERNAME))
            .andExpect(jsonPath("$[0].user.email").value(UserConstants.EMAIL))
            .andExpect(jsonPath("$[0].user.firstName").value(UserConstants.FIRST_NAME))
            .andExpect(jsonPath("$[0].user.lastName").value(UserConstants.LAST_NAME))
            .andExpect(jsonPath("$[0].user.joinDate").value(UserConstants.JOIN_DATE.toString()));
    }

    /**
     * Verifies that get Movie Reviews By User Id no Exceptions success.
     */
    @Test
    void testGetMovieReviewsByUserId_noExceptions_success() throws Exception {
        when(reviewService.getMovieReviewsByUserId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDtoList());

        mockMvc
            .perform(get(URIConstants.USERS_ID_MOVIES_REVIEWS_PATH, UserConstants.ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].rating").value(RATING))
            .andExpect(jsonPath("$[0].reviewText").value(REVIEW_TEXT))
            .andExpect(jsonPath("$[0].dateModified", is(DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$[0].movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$[0].cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$[0].user.id").value(UserConstants.ID))
            .andExpect(jsonPath("$[0].user.username").value(UserConstants.USERNAME))
            .andExpect(jsonPath("$[0].user.email").value(UserConstants.EMAIL))
            .andExpect(jsonPath("$[0].user.firstName").value(UserConstants.FIRST_NAME))
            .andExpect(jsonPath("$[0].user.lastName").value(UserConstants.LAST_NAME))
            .andExpect(jsonPath("$[0].user.joinDate").value(UserConstants.JOIN_DATE.toString()));
    }

    /**
     * Verifies that get Cinema Reviews By User Id no Exceptions success.
     */
    @Test
    void testGetCinemaReviewsByUserId_noExceptions_success() throws Exception {
        when(reviewService.getCinemaReviewsByUserId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDtoList());

        mockMvc
            .perform(get(URIConstants.USERS_ID_CINEMAS_REVIEWS_PATH, UserConstants.ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(ID))
            .andExpect(jsonPath("$[0].rating").value(RATING))
            .andExpect(jsonPath("$[0].reviewText").value(REVIEW_TEXT))
            .andExpect(jsonPath("$[0].dateModified", is(DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$[0].movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$[0].cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$[0].user.id").value(UserConstants.ID))
            .andExpect(jsonPath("$[0].user.username").value(UserConstants.USERNAME))
            .andExpect(jsonPath("$[0].user.email").value(UserConstants.EMAIL))
            .andExpect(jsonPath("$[0].user.firstName").value(UserConstants.FIRST_NAME))
            .andExpect(jsonPath("$[0].user.lastName").value(UserConstants.LAST_NAME))
            .andExpect(jsonPath("$[0].user.joinDate").value(UserConstants.JOIN_DATE.toString()));
    }

    /**
     * Verifies that update Review return Old True success.
     */
    @Test
    void testUpdateReview_returnOldTrue_success() throws Exception {
        when(reviewService.updateReview(any(), anyInt())).thenReturn(ReviewFactory.getDefaultReviewDto());
        final String json = objectMapper.writeValueAsString(ReviewFactory.getDefaultReviewRequest());

        mockMvc
            .perform(put(URIConstants.REVIEWS_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.rating").value(RATING))
            .andExpect(jsonPath("$.reviewText").value(REVIEW_TEXT))
            .andExpect(jsonPath("$.dateModified", is(DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$.movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$.movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$.movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$.user.id").value(UserConstants.ID))
            .andExpect(jsonPath("$.user.username").value(UserConstants.USERNAME))
            .andExpect(jsonPath("$.user.email").value(UserConstants.EMAIL))
            .andExpect(jsonPath("$.user.firstName").value(UserConstants.FIRST_NAME))
            .andExpect(jsonPath("$.user.lastName").value(UserConstants.LAST_NAME))
            .andExpect(jsonPath("$.user.joinDate").value(UserConstants.JOIN_DATE.toString()));
    }

    /**
     * Verifies that update Review return Old False success.
     */
    @Test
    void testUpdateReview_returnOldFalse_success() throws Exception {
        when(reviewService.updateReview(any(), anyInt())).thenReturn(ReviewFactory.getDefaultReviewDto());
        final String json = objectMapper.writeValueAsString(ReviewFactory.getDefaultReviewRequest());

        mockMvc
            .perform(put(URIConstants.REVIEWS_ID_PATH, ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam(RETURN_OLD, String.valueOf(false)))
            .andExpect(status().isNoContent());
    }

    /**
     * Verifies that delete Review return Old True success.
     */
    @Test
    void testDeleteReview_returnOldTrue_success() throws Exception {
        when(reviewService.deleteReview(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDto());

        mockMvc
            .perform(delete(URIConstants.REVIEWS_ID_PATH, ID).queryParam(RETURN_OLD, String.valueOf(true)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.rating").value(RATING))
            .andExpect(jsonPath("$.reviewText").value(REVIEW_TEXT))
            .andExpect(jsonPath("$.dateModified", is(DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.movie.id").value(MovieConstants.ID))
            .andExpect(jsonPath("$.movie.title").value(MovieConstants.TITLE))
            .andExpect(jsonPath("$.movie.description").value(MovieConstants.DESCRIPTION))
            .andExpect(jsonPath("$.movie.releaseDate", is(MovieConstants.RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.cinema").value(CinemaFactory.getDefaultCinemaDto()))
            .andExpect(jsonPath("$.user.id").value(UserConstants.ID))
            .andExpect(jsonPath("$.user.username").value(UserConstants.USERNAME))
            .andExpect(jsonPath("$.user.email").value(UserConstants.EMAIL))
            .andExpect(jsonPath("$.user.firstName").value(UserConstants.FIRST_NAME))
            .andExpect(jsonPath("$.user.lastName").value(UserConstants.LAST_NAME))
            .andExpect(jsonPath("$.user.joinDate").value(UserConstants.JOIN_DATE.toString()));
    }

    /**
     * Verifies that delete Review return Old False success.
     */
    @Test
    void testDeleteReview_returnOldFalse_success() throws Exception {
        when(reviewService.deleteReview(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDto());

        mockMvc
            .perform(delete(URIConstants.REVIEWS_ID_PATH, ID).queryParam(RETURN_OLD, String.valueOf(false)))
            .andExpect(status().isNoContent());
    }
}





