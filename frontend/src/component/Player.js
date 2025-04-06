import React from "react";

const Player = ({ videoId }) => {
  const embedUrl = `https://www.youtube.com/embed/${videoId}`;

  return (
    <div className="youtube-container">
      <iframe
        width="100%"
        height="360"
        src={embedUrl}
        title="YouTube video player"
        frameBorder="0"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowFullScreen
      ></iframe>
    </div>
  );
};

export default Player;
