import { useState } from "react";
import PropTypes from "prop-types"; // PropTypes import

const TestAnswerForm = ({ initialAnswer = "", mockGenerateAnswer }) => {
  const [answer, setAnswer] = useState(initialAnswer); // 답변 내용
  const [loading, setLoading] = useState(false); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태

  // 더미 데이터로 답변 생성
  const generateAnswer = async () => {
    if (mockGenerateAnswer) {
      try {
        setLoading(true);
        const generatedAnswer = await mockGenerateAnswer();
        setAnswer(generatedAnswer);
      } catch (err) {
        setError("오류 발생: " + err.message);
      } finally {
        setLoading(false);
      }
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
      {error && <p className="error-message">{error}</p>}
    </div>
  );
};

/** PropTypes를 사용한 props 검증 */
TestAnswerForm.propTypes = {
  initialAnswer: PropTypes.string, // initialAnswer는 문자열이어야 함
  mockGenerateAnswer: PropTypes.func.isRequired, // mockGenerateAnswer는 필수 함수
};

export default TestAnswerForm;