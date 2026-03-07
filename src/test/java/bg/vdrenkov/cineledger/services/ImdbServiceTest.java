package bg.vdrenkov.cineledger.services;

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

@ExtendWith(MockitoExtension.class)
public class ImdbServiceTest {

    private static final String IMDB_KEY = "test-key";

    private static final String BASE_URL = "https://imdb-api.com/en/API/";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ImdbService imdbService;

    @BeforeEach
    public void setUp() {
        imdbService.setImdbKey(IMDB_KEY);
        imdbService.setBaseUrl(BASE_URL);
    }

    @Test
    public void testGetMovies_returnTop_success() throws JSONException {
        String filter = "top";
        String mockResponseBody =
            "{\"items\": [{\"title\": \"The Shawshank Redemption\", \"year\": \"1994\", \"image\": \"image1.jpg\"}, "
                + "{\"title\": \"Movie 2\", \"year\": \"2023\", \"image\": \"image2.jpg\"}]}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponseBody);

        JSONObject mockResponseJson = new JSONObject(mockResponseBody);
        JSONArray mockMovies = mockResponseJson.getJSONArray("items");

        JSONObject firstMovie = mockMovies.getJSONObject(0);
        String expectedTitle = firstMovie.getString("title");
        String expectedYear = firstMovie.getString("year");
        String expectedImage = firstMovie.getString("image");

        String result = imdbService.getMovies(filter);

        JSONObject resultJson = new JSONObject(result);
        JSONArray resultMovies = resultJson.getJSONArray("movies");

        JSONObject firstResultMovie = resultMovies.getJSONObject(0);
        String actualTitle = firstResultMovie.getString("title");
        String actualYear = firstResultMovie.getString("year");
        String actualImage = firstResultMovie.getString("image");

        assertEquals(expectedTitle, actualTitle);
        assertEquals(expectedYear, actualYear);
        assertEquals(expectedImage, actualImage);
    }

    @Test
    public void testGetMovies_returnBoxOffices_success() throws JSONException {
        String filter = "box office";
        String mockResponseBody = "{\"items\": [{\"title\": \"Avatar\", \"year\": \"2009\"}, {\"title\": \"Movie 2\", \"year\": \"2023\"}]}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponseBody);

        JSONObject mockResponseJson = new JSONObject(mockResponseBody);
        JSONArray mockMovies = mockResponseJson.getJSONArray("items");

        JSONObject firstMovie = mockMovies.getJSONObject(0);
        String expectedTitle = firstMovie.getString("title");
        String expectedYear = firstMovie.getString("year");

        String result = imdbService.getMovies(filter);

        JSONObject resultJson = new JSONObject(result);
        JSONArray resultMovies = resultJson.getJSONArray("movies");

        JSONObject firstResultMovie = resultMovies.getJSONObject(0);
        String actualTitle = firstResultMovie.getString("title");
        String actualYear = firstResultMovie.getString("year");

        assertEquals(expectedTitle, actualTitle);
        assertEquals(expectedYear, actualYear);
    }

    @Test
    public void testGetMovies_returnComingSoon_success() throws JSONException {
        String filter = "coming soon";
        String mockResponseBody = "{\"items\": [{\"title\": \"The Flash\", \"year\": \"2023\",\"image\": \"image1.jpg\"}, {\"title\": \"Movie 2\", \"year\": \"2023\",\"image\": \"image2.jpg\"}]}";
        when(restTemplate.getForObject(anyString(), any())).thenReturn(mockResponseBody);

        JSONObject mockResponseJson = new JSONObject(mockResponseBody);
        JSONArray mockMovies = mockResponseJson.getJSONArray("items");

        JSONObject firstMovie = mockMovies.getJSONObject(0);
        String expectedTitle = firstMovie.getString("title");
        String expectedYear = firstMovie.getString("year");
        String expectedImage = firstMovie.getString("image");

        String result = imdbService.getMovies(filter);

        JSONObject resultJson = new JSONObject(result);
        JSONArray resultMovies = resultJson.getJSONArray("movies");

        JSONObject firstResultMovie = resultMovies.getJSONObject(0);
        String actualTitle = firstResultMovie.getString("title");
        String actualYear = firstResultMovie.getString("year");
        String actualImage = firstResultMovie.getString("image");

        assertEquals(expectedTitle, actualTitle);
        assertEquals(expectedYear, actualYear);
        assertEquals(expectedImage, actualImage);
    }

    @Test
    public void testGetMovies_invalidFilter_success() {
        String invalidFilter = "unknown";

        try {
            imdbService.getMovies(invalidFilter);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid filter type", e.getMessage());
        }
    }
}




