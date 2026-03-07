package bg.vdrenkov.cineledger.services;

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

  public ImdbService(
    RestTemplate restTemplate, @Value("${imdb.key}") String imdbKey, @Value("${imdb.url}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.imdbKey = imdbKey;
    this.baseUrl = baseUrl;
  }

  public void setImdbKey(String imdbKey) {
    this.imdbKey = imdbKey;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getMovies(String filter) {
    String searchUrl;

    if ("top".equals(filter)) {
      searchUrl = baseUrl + "Top250Movies/" + imdbKey;
    } else if ("coming soon".equals(filter)) {
      searchUrl = baseUrl + "ComingSoon/" + imdbKey;
    } else if ("box office".equals(filter)) {
      searchUrl = baseUrl + "BoxOfficeAllTime/" + imdbKey;
    } else {
      throw new IllegalArgumentException("Invalid filter type");
    }

    if (!StringUtils.hasText(imdbKey)) {
      log.info("Skipping IMDb lookup because no API key is configured");
      return emptyMoviesResponse();
    }

    try {
      String unfilteredMovies = restTemplate.getForObject(searchUrl, String.class);
      return filterMoviesTopMovies(unfilteredMovies);
    } catch (RestClientException exception) {
      log.warn("IMDb lookup failed for filter '{}'", filter, exception);
      return emptyMoviesResponse();
    }
  }

  private String filterMoviesTopMovies(String responseBody) {
    JSONObject responseJson = new JSONObject(responseBody);
    JSONArray movies = responseJson.getJSONArray("items");
    JSONArray filteredMovies = new JSONArray();

    for (int i = 0; i < movies.length(); i++) {
      JSONObject movie = movies.getJSONObject(i);
      JSONObject filteredMovie = new JSONObject();

      filteredMovie.put("title", movie.getString("title"));
      filteredMovie.put("year", movie.getString("year"));

      if (movie.has("image")) {
        filteredMovie.put("image", movie.getString("image"));
      }

      filteredMovies.put(filteredMovie);
    }

    JSONObject resultJson = new JSONObject();
    resultJson.put("movies", filteredMovies);

    return resultJson.toString(2);
  }

  private String emptyMoviesResponse() {
    return new JSONObject().put("movies", new JSONArray()).toString(2);
  }
}



