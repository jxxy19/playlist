import React, { useState } from "react";
import axios from "axios";
import "../styles/VideoChapterparser.css"

const VideoChapterparser = () => {
  const [videoId, setVideoId] = useState("");
  const [chapters, setChapters] = useState([]);

  const fetchDescription = async (videoId) => {
    console.log("video Id : ", videoId);
    try {
      const response = await axios.get(
        `http://localhost:8080/youtube/chapters`,
        {
          params: { videoId },
        }
      );
      console.log("YouTube API Response:", response.data);
      const description = response.data.items?.[0]?.snippet?.description;

      const chapterRegex = /(\d{1,2}:\d{2})\s+(.+)/g;
      const extractedCapter = [];
      let match;
      while ((match = chapterRegex.exec(description)) !== null) {
        extractedCapter.push({ time: match[1], title: match[2] });
      }

      if (description) {
        console.log("Description:", description);
      } else {
        console.warn("Description을 찾지 못했어요.");
      }

      console.log(" 추츨된 챕터 :", extractedCapter);
      setChapters(extractedCapter);
    } catch (error) {
      console.error("불러오기 실패:", error);
    }
  };

  return (
    <div className="container">
      <input
        type="text"
        placeholder="유튜브 Video ID 입력"
        value={videoId}
        onChange={(e) => setVideoId(e.target.value)}
        className="input-group"
      />
      <button onClick={() => fetchDescription(videoId)}>
        가져오기
      </button>

      <ul>
        {chapters.map((c, i) => (
          <li key={i}>
            ⏱ {c.time} - 🎵 {c.title}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default VideoChapterparser;
