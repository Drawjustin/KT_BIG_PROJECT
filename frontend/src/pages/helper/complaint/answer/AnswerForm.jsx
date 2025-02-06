import { useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import styles from "../Answer.module.css";
import { complaintApi } from "../../../../api";

/** AI 민원 답변 생성 및 저장 API 호출 컴포넌트 */
const AnswerForm = ({ complaintSeq, memberSeq = 1, teamSeq = 1 }) => {
  const [answer, setAnswer] = useState(""); // 답변 내용
  const [isGenerating, setIsGenerating] = useState(false); // 생성 중 상태
  const [isSaving, setIsSaving] = useState(false); // 저장 중 상태
  const [error, setError] = useState(null); // 에러 상태
  const [success, setSuccess] = useState(false); // 저장 성공 여부

  // AI API 호출하여 답변 자동 생성
  const generateAnswer = async () => {
    try {
      setIsGenerating(true); // 생성 중 상태로 변경

      const response = await axios.post(
        "http://localhost:5000/api/generate-answer", // ai 응답 api
        {
          question: "현재 게시글에 대한 질문 내용", // 필요한 데이터를 AI API로 전달
        }
      );
      setAnswer(response.data.generatedAnswer); // 생성된 답변 설정
    } catch (err) {
      console.error("답변 생성 중 오류 발생:", err);
      setError(err.message);
    } finally {
      setIsGenerating(false); // 생성 완료
    }
  };

  // 답변 저장 API 호출
  const saveAnswer = async () => {
    try {
      setIsSaving(true); // 저장 중 상태로 변경
      setError(null);
      setSuccess(false);

      // FormData 객체 생성
      const formData = new FormData();
      formData.append("memberSeq", memberSeq); // 사용자 ID
      formData.append("teamSeq", teamSeq); // 팀/부서 ID
      formData.append("title", "Complaint Answer"); // 제목 (하드코딩, 필요시 변경)
      formData.append("content", answer); // 답변 내용
      formData.append("complaintSeq", complaintSeq); // 민원 번호 (외부에서 전달)

      const response = await complaintApi.create(formData);
      // 저장 API 호출
      // const response = await axios.post("http://localhost:8080/complaint/create", formData, {
      //   headers: {
      //     Authorization: `Bearer ${jwtToken}`, // JWT 토큰
      //     "Content-Type": "multipart/form-data",
      //   },
      // });

      if (response.status === 200) {
        setSuccess(true);
        alert(
          `답변이 성공적으로 저장되었습니다! Complaint Seq: ${response.data.complaintSeq}`
        );
      }
    } catch (err) {
      console.error("저장 중 오류 발생:", err);
      setError(err.message);
    } finally {
      setIsSaving(false); // 저장 완료
    }
  };

  return (
    <div className={styles["answer-form-section"]}>
      <h2>답변하기 |</h2>
      <textarea
        rows="5"
        value={answer}
        onChange={(e) => setAnswer(e.target.value)}
        placeholder="답변 내용을 입력하세요"
        className={styles["answer-textarea"]}
      ></textarea>
      <div className={styles["answer-actions"]}>
        {/* AI 답변 생성 버튼 */}
        <button
          onClick={generateAnswer}
          disabled={isGenerating}
          className={`${styles["answer-actions button"]} ${styles["generate-button"]}`}
        >
          {isGenerating ? "생성 중..." : "답변 자동 생성"}
        </button>
        {/* 답변 저장 버튼 */}
        <button onClick={saveAnswer} disabled={isSaving || !answer}>
          {isSaving ? "저장 중..." : "답변 저장"}
        </button>
      </div>
      {error && <p className={styles["error-message"]}>오류 발생: {error}</p>}
      {success && (
        <p className={styles["success-message"]}>저장이 완료되었습니다.</p>
      )}
    </div>
  );
};

/** PropTypes로 props 검증 */
AnswerForm.propTypes = {
  complaintSeq: PropTypes.number.isRequired, // 민원 번호, 필수
  memberSeq: PropTypes.number, // 사용자 ID, 선택적 (기본값: 1)
  teamSeq: PropTypes.number, // 팀/부서 ID, 선택적 (기본값: 1)
  jwtToken: PropTypes.string.isRequired, // JWT 토큰, 필수
};

export default AnswerForm;
