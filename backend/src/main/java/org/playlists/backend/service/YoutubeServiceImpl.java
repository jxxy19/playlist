package org.playlists.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.playlists.backend.dto.ChapterDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class YoutubeServiceImpl implements YoutubeServiceIf{

    @Value("${youtube.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<ChapterDTO> extractChapters(String videoId) {
//        유튜브 설명 란에 chapter 존재
        String description = fetchDescription(videoId);
        List<ChapterDTO> chapters = parseChapters(description);

     if (!chapters.isEmpty()) {
        return chapters;
    }

    System.out.println("❌ 설명에 챕터 없음 → 고정 댓글 시도");

    // 2. 고정 댓글 시도
    String topComment = fetchTopComment(videoId);
    chapters = parseChapters(topComment);

    if (!chapters.isEmpty()) {
        return chapters;
    }

    System.out.println("❌ 고정 댓글에도 없음 → 전체 댓글에서 시도");

    // 3. 전체 댓글 시도
    String allComments = fetchAllComments(videoId);
    chapters = parseChapters(allComments);

    return chapters;
}

    private String fetchDescription(String videoId) {
        String url = "https://www.googleapis.com/youtube/v3/videos" +
                "?part=snippet" +
                "&id=" + videoId +
                "&key=" + apiKey;
        String res = restTemplate.getForObject(url, String.class);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(res);
            return jsonNode.path("items").get(0).path("snippet").path("description").asText();
        } catch (Exception e) {
            throw new RuntimeException("description load Fail", e);
        }
    }

    private String fetchTopComment(String videoId) {
        String url = "https://www.googleapis.com/youtube/v3/commentThreads" +
                "?part=snippet" +
                "&videoId=" + videoId +
                "&maxResults=1" +  // 첫 번째 댓글만
                "&order=relevance" + // 고정 댓글이 위에 올 가능성 높음
                "&key=" + apiKey;

        try {
            String res = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(res);
            JsonNode first = root.path("items").get(0);
            String commentText = first.path("snippet").path("topLevelComment").path("snippet").path("textDisplay").asText();
            String cleanComment = Jsoup.parse(commentText).text();
            System.out.println("🧷 고정 댓글 내용: " + cleanComment);

            return cleanComment;
        } catch (Exception e) {
            System.out.println("고정 댓글 불러오기 실패: " + e.getMessage());
            return "";
        }
    }

    private String fetchAllComments(String videoId) {
        StringBuilder commentsBuilder = new StringBuilder();

        String url = "https://www.googleapis.com/youtube/v3/commentThreads" +
                "?part=snippet" +
                "&videoId=" + videoId +
                "&maxResults=100" +
                "&order=relevance" + // 최신순
                "&key=" + apiKey;

        try {
            String res = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(res);
            JsonNode items = root.path("items");

            for (JsonNode item : items) {
                String text = item.path("snippet").path("topLevelComment").path("snippet").path("textDisplay").asText();
                String cleanText = Jsoup.parse(text).text();
                System.out.println("💬 댓글 내용: " + cleanText);
                commentsBuilder.append(cleanText).append("\n");
            }

        } catch (Exception e) {
            System.out.println("전체 댓글 불러오기 실패: " + e.getMessage());
        }

        return commentsBuilder.toString();
    }



    private List<ChapterDTO> parseChapters(String description) {
        List<ChapterDTO> chapters = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d{1,2}:\\d{2})(?:\\s*|)(.+)");
        Matcher matcher = pattern.matcher(description);

        while (matcher.find()) {
            chapters.add(new ChapterDTO(matcher.group(1), matcher.group(2)));
        }
        return chapters;
    }

}
