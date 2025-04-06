package org.playlists.backend.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/youtube")
public class YoutubeProxyController {

    @Value("${youtube.api.key}.trim()")
    private String apiKey;

    @GetMapping("/chapters")
    public ResponseEntity<String> getVideoDescription(@RequestParam String videoId) {
        String url = "https://www.googleapis.com/youtube/v3/videos" +
                "?part=snippet" +
                "&id=" + videoId +
                "&key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
