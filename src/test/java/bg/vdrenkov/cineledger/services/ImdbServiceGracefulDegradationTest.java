package bg.vdrenkov.cineledger.services;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class ImdbServiceGracefulDegradationTest {

  @Mock
  private RestTemplate restTemplate;

  @Test
  void getMovies_withoutApiKey_returnsEmptyPayload() {
    ImdbService imdbService = new ImdbService(restTemplate, "", "https://imdb-api.com/en/API/");

    String response = imdbService.getMovies("top");

    assertEquals(0, new JSONObject(response).getJSONArray("movies").length());
    verifyNoInteractions(restTemplate);
  }

  @Test
  void getMovies_whenRemoteLookupFails_returnsEmptyPayload() {
    ImdbService imdbService = new ImdbService(restTemplate, "imdb-key", "https://imdb-api.com/en/API/");
    when(restTemplate.getForObject("https://imdb-api.com/en/API/Top250Movies/imdb-key", String.class))
      .thenThrow(new RestClientException("boom"));

    String response = imdbService.getMovies("top");

    assertEquals(0, new JSONObject(response).getJSONArray("movies").length());
  }
}

