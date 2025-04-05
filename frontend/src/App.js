import React,{ useEffect, useState } from "react";
import axios from "axios";

function App() {
  const [msg, setMsg] = useState("");

  useEffect(() => {
    axios.get("http://localhost:8080/api/hello", {
      withCredentials: true
    })
    .then(res => setMsg(res.data))
    .catch(err => console.log(err));
  }, []);

  return (
    <div>
      <h1>{msg}</h1>
    </div>
  );
}

export default App;
