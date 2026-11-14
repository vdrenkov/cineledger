package dev.vdrenkov.cineledger.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ImdbService {
    private static final Logger log = LoggerFactory.getLogger(ImdbService.class);

    private final RestTemplate restTemplate;

    private String imdbKey;

    private String baseUrl;

    public void setImdbKey(final String imdbKey) {
        this.imdbKey = imdbKey;
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ImdbService(RestTemplate restTemplate, @Value("${imdb.key}") String imdbKey,
        @Value("${imdb.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.imdbKey = imdbKey;
        this.baseUrl = baseUrl;
    }

    public String getMovies(String filter) {
        String searchUrl = switch (filter) {
            case "top" -> baseUrl + "Top250Movies/" + imdbKey;
            case "coming soon" -> baseUrl + "ComingSoon/" + imdbKey;
            case "box office" -> baseUrl + "BoxOfficeAllTime/" + imdbKey;
            case null, default -> throw new IllegalArgumentException("Invalid filter type");
        };

        if (!StringUtils.hasText(imdbKey)) {

            return emptyMoviesResponse();
        }

        try {
            final String unfilteredMovies = restTemplate.getForObject(searchUrl, String.class);
            return filterMoviesTopMovies(unfilteredMovies);
        } catch (RestClientException exception) {
            log.warn("IMDb lookup failed for filter '{}'", filter, exception);
            return emptyMoviesResponse();
        }
    }

    private static String filterMoviesTopMovies(String responseBody) {
        final JSONObject responseJson = new JSONObject(responseBody);
        final JSONArray movies = responseJson.getJSONArray("items");
        final JSONArray filteredMovies = new JSONArray();

        for (int i = 0; i < movies.length(); i++) {
            final JSONObject movie = movies.getJSONObject(i);
            final JSONObject filteredMovie = new JSONObject();

            filteredMovie.put("title", movie.getString("title"));
            filteredMovie.put("year", movie.getString("year"));

            final String IMAGE_STRING = "image";
            if (movie.has(IMAGE_STRING)) {
                filteredMovie.put(IMAGE_STRING, movie.getString(IMAGE_STRING));
            }

            filteredMovies.put(filteredMovie);
        }

        final JSONObject resultJson = new JSONObject();
        resultJson.put("movies", filteredMovies);

        return resultJson.toString(2);
    }

    private static String emptyMoviesResponse() {
        return new JSONObject().put("movies", new JSONArray()).toString(2);
    }
}
