import React, { useState } from "react";
import axios from "axios";
import Player from "./Player";
import "../styles/VideoChapterparser.css";

const VideoChapterparser = () => {
  const [videoId, setVideoId] = useState("");
  const [videoLink, setVideoLink] = useState("");
  const [chapters, setChapters] = useState([]);

  // const fetchDescription = async (videoId) => {
  const fetchDescription = async () => {
    function extractVideoId(url) {
      const regex =
        /(?:youtube\.com\/(?:.*v=|v\/|embed\/)|youtu\.be\/)([a-zA-Z0-9_-]{11})/;
      const match = url.match(regex);
      return match ? match[1] : null;
    }
    const extractedId = extractVideoId(videoLink);
    console.log("video Id : ", extractedId);

    if (!extractedId) {
      alert("유뷰트 링크를 입력해주세요!");
      return;
    }

    setVideoId(extractedId);

    try {
      console.log("비디오아이디 입력 예정 :" + extractedId);
      console.log(typeof extractedId);
      const response = await axios.get(
        `http://localhost:8080/youtube/chapters`,
        {
          params: { videoId: extractedId },
        }
      );
      console.log("백엔드로 보낼 videoId:", extractedId); // 꼭 넣어봐!

      console.log("백엔드 응답 데이터 : ", response.data);
      setChapters(response.data);
    } catch (error) {
      console.error("불러오기 실패: ", error);
    }
  };

  return (
    <div className="container">
      <input
        type="text"
        placeholder="유튜브 주소를 입력해주세요"
        value={videoLink}
        onChange={(e) => setVideoLink(e.target.value)}
        className="input-group"
      />
      <button onClick={fetchDescription}>가져오기</button>

      {videoId && <Player videoId={videoId} />}

      <ul>
        {chapters.flatMap((c, i) => {
          const text = `${c.time} ${c.title}`;
          const pattern = /(\d{1,2}:\d{2})\s+(.*?)(?=\s+\d{1,2}:\d{2}|$)/g;

          const parts = [];
          let match;
          while ((match = pattern.exec(text)) !== null) {
            parts.push({ time: match[1], title: match[2] });
          }

          return parts.map((part, idx) => (
            <li key={`${i}-${idx}`}>
              ⏱ {part.time} - 🎵 {part.title}
            </li>
          ));
        })}
      </ul>
    </div>
  );
};

export default VideoChapterparser;
