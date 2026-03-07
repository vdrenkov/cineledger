package dev.vdrenkov.cineledger.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Tests imdb service behavior.
 */
@ExtendWith(MockitoExtension.class)
class ImdbServiceTest {
    private static final String IMDB_KEY = "test-key";

    private static final String BASE_URL = "https://imdb-api.com/en/API/";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ImdbService imdbService;

    /**
     * Initializes the test fixture before each test case.
     */
    @BeforeEach
    void setUp() {
        imdbService.setImdbKey(IMDB_KEY);
        imdbService.setBaseUrl(BASE_URL);
    }

    /**
     * Verifies that get Movies return Top success.
     */
    @Test
    void testGetMovies_returnTop_success() throws JSONException {
        final String filter = "top";
        String mockResponseBody =
            "{\"items\": [{\"title\": \"The Shawshank Redemption\", \"year\": \"1994\", \"image\": \"image1.jpg\"}, "
                + "{\"title\": \"Movie 2\", \"year\": \"2023\", \"image\": \"image2.jpg\"}]}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponseBody);

        final JSONObject mockResponseJson = new JSONObject(mockResponseBody);
        final JSONArray mockMovies = mockResponseJson.getJSONArray("items");

        final JSONObject firstMovie = mockMovies.getJSONObject(0);
        final String expectedTitle = firstMovie.getString("title");
        final String expectedYear = firstMovie.getString("year");
        final String expectedImage = firstMovie.getString("image");

        final String result = imdbService.getMovies(filter);

        final JSONObject resultJson = new JSONObject(result);
        final JSONArray resultMovies = resultJson.getJSONArray("movies");

        final JSONObject firstResultMovie = resultMovies.getJSONObject(0);
        final String actualTitle = firstResultMovie.getString("title");
        final String actualYear = firstResultMovie.getString("year");
        final String actualImage = firstResultMovie.getString("image");

        assertEquals(expectedTitle, actualTitle);
        assertEquals(expectedYear, actualYear);
        assertEquals(expectedImage, actualImage);
    }

    /**
     * Verifies that get Movies return Box Offices success.
     */
    @Test
    void testGetMovies_returnBoxOffices_success() throws JSONException {
        final String filter = "box office";
        final String mockResponseBody = "{\"items\": [{\"title\": \"Avatar\", \"year\": \"2009\"}, {\"title\": \"Movie 2\", \"year\": \"2023\"}]}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponseBody);

        final JSONObject mockResponseJson = new JSONObject(mockResponseBody);
        final JSONArray mockMovies = mockResponseJson.getJSONArray("items");

        final JSONObject firstMovie = mockMovies.getJSONObject(0);
        final String expectedTitle = firstMovie.getString("title");
        final String expectedYear = firstMovie.getString("year");

        final String result = imdbService.getMovies(filter);

        final JSONObject resultJson = new JSONObject(result);
        final JSONArray resultMovies = resultJson.getJSONArray("movies");

        final JSONObject firstResultMovie = resultMovies.getJSONObject(0);
        final String actualTitle = firstResultMovie.getString("title");
        final String actualYear = firstResultMovie.getString("year");

        assertEquals(expectedTitle, actualTitle);
        assertEquals(expectedYear, actualYear);
    }

    /**
     * Verifies that get Movies return Coming Soon success.
     */
    @Test
    void testGetMovies_returnComingSoon_success() throws JSONException {
        final String filter = "coming soon";
        final String mockResponseBody = "{\"items\": [{\"title\": \"The Flash\", \"year\": \"2023\",\"image\": \"image1.jpg\"}, {\"title\": \"Movie 2\", \"year\": \"2023\",\"image\": \"image2.jpg\"}]}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponseBody);

        final JSONObject mockResponseJson = new JSONObject(mockResponseBody);
        final JSONArray mockMovies = mockResponseJson.getJSONArray("items");

        final JSONObject firstMovie = mockMovies.getJSONObject(0);
        final String expectedTitle = firstMovie.getString("title");
        final String expectedYear = firstMovie.getString("year");
        final String expectedImage = firstMovie.getString("image");

        final String result = imdbService.getMovies(filter);

        final JSONObject resultJson = new JSONObject(result);
        final JSONArray resultMovies = resultJson.getJSONArray("movies");

        final JSONObject firstResultMovie = resultMovies.getJSONObject(0);
        final String actualTitle = firstResultMovie.getString("title");
        final String actualYear = firstResultMovie.getString("year");
        final String actualImage = firstResultMovie.getString("image");

        assertEquals(expectedTitle, actualTitle);
        assertEquals(expectedYear, actualYear);
        assertEquals(expectedImage, actualImage);
    }

    /**
     * Verifies that get Movies invalid Filter success.
     */
    @Test
    void testGetMovies_invalidFilter_success() {
        final String invalidFilter = "unknown";

        try {
            imdbService.getMovies(invalidFilter);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid filter type", e.getMessage());
        }
    }
}




