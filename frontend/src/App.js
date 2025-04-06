import React from "react";
import VideoChapterparser from "./component/VideoChapterParser"; // 경로가 다르면 조정!

function App() {
  return (
    <div className="App">
      <h1>🎧 유튜브 챕터 추출기</h1>
      <VideoChapterparser />
    </div>
  );
}

export default App;
