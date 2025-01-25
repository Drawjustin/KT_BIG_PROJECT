import { useState } from "react";
import axios from "axios";

const AnswerForm = () => {
  const [answer, setAnswer] = useState(""); // 답변 내용
  const [loading, setLoading] = useState(false); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태

  // AI API 호출하여 답변 자동 생성
  const generateAnswer = async () => {
    try {
      setLoading(true);
      const response = await axios.post("http://localhost:5000/api/generate-answer", { // 백엔드 api 엔드 포인트 확인 필요. 
        question: "현재 게시글에 대한 질문 내용" // 필요한 데이터를 AI API로 전달
      });
      setAnswer(response.data.generatedAnswer); // 생성된 답변 설정
    } catch (err) {
      console.error("답변 생성 중 오류 발생:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="answer-form-section">
      <h3>답변</h3>
      <textarea
        rows="5"
        value={answer}
        onChange={(e) => setAnswer(e.target.value)}
        placeholder="답변 내용을 입력하세요"
        className="answer-textarea"
      ></textarea>
      <div className="answer-actions">
        <button onClick={generateAnswer} disabled={loading}>
          {loading ? "생성 중..." : "답변 자동 생성"}
        </button>
      </div>
      {error && <p className="error-message">오류 발생: {error}</p>}
    </div>
  );
};

export default AnswerForm;