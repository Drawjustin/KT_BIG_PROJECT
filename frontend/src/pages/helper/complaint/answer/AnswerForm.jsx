/**
 * AnswerForm 컴포넌트 - AI 기반 민원 답변 생성 및 저장 기능을 제공하는 컴포넌트
 *
 * @component
 * @description
 * 민원에 대한 답변을 AI로 생성하고, 생성된 답변을 저장하는 기능을 제공합니다.
 * 답변 내용을 직접 수정할 수 있으며, AI 생성과 저장 과정의 상태를 사용자에게 표시합니다.
 */

import { useState } from "react";
import PropTypes from "prop-types";
import styles from "../Answer.module.css";
import { complaintApi, commentsApi } from "../../../../api";

const AnswerForm = ({
  complaintSeq,
  class_department,
  memberSeq = 1,
  teamSeq = 1,
  summary,
}) => {
  // 상태 관리
  const [answer, setAnswer] = useState("");
  const [retrievedDocs, setRetrievedDocs] = useState([]);
  const [isGenerating, setIsGenerating] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  // const [answer, setAnswer] = useState(""); // 답변 내용 상태
  // const [isGenerating, setIsGenerating] = useState(false); // AI 답변 생성 중 상태
  // const [isSaving, setIsSaving] = useState(false); // 답변 저장 중 상태
  // const [error, setError] = useState(null); // 에러 메시지 상태
  // const [success, setSuccess] = useState(false); // 저장 성공 여부 상태
/**
 * AI 응답 데이터를 포맷팅하는 함수
 * 
 * @param {Object} responseData - AI 서버로부터 받은 응답 데이터
 * @returns {Object} 포맷팅된 답변과 참고 문서
 */
const formatAnswer = (responseData) => {
  if (!responseData?.answer) {
    return {
      answer: '응답을 받지 못했습니다.',
      docs: []
    };
  }

  // answer에서 '답변:' 이후의 내용만 추출
  const answerContent = responseData.answer.split('답변:')[1]?.trim() || responseData.answer;

  return {
    answer: answerContent,
    docs: responseData.retrievedDocs || []
  };
};

/**
 * AI 답변을 생성하는 함수
 */
const generateAnswer = async () => {
  try {
    setIsGenerating(true);
    setError(null);

    const params = {
      complaintSeq,
      class_department,
      summary,
    };

    const response = await commentsApi.create(params);
    const { answer: formattedAnswer, docs } = formatAnswer(response.data);
    setAnswer(formattedAnswer);
    setRetrievedDocs(docs);
  } catch (err) {
    console.error("답변 생성 중 오류 발생:", err);
    setError(err.message);
  } finally {
    setIsGenerating(false);
  }
};
  // /**
  //  * AI 응답 데이터를 포맷팅하는 함수
  //  *
  //  * @param {Object} responseData - AI 서버로부터 받은 응답 데이터
  //  * @param {string} responseData.answer - AI가 생성한 답변 내용
  //  * @param {Array} responseData.retrievedDocs - 참고한 문서 목록
  //  * @returns {string} 포맷팅된 답변 문자열
  //  */
  // const formatAnswer = (responseData) => {
  //   if (!responseData?.answer) {
  //     return "응답을 받지 못했습니다.";
  //   }

  //   let formattedAnswer = responseData.answer;

  //   // 참고 문서 정보가 있는 경우 답변 하단에 추가
  //   if (responseData.retrievedDocs?.length > 0) {
  //     formattedAnswer += "\n\n참고 문서:\n";
  //     formattedAnswer += responseData.retrievedDocs
  //       .map((doc) => doc.split("\n").slice(0, 2).join("\n"))
  //       .join("\n\n");
  //   }

  //   return formattedAnswer;
  // };

  // /**
  //  * AI 답변을 생성하는 함수
  //  *
  //  * @async
  //  * @function generateAnswer
  //  * @throws {Error} API 호출 실패 시 에러
  //  */
  // const generateAnswer = async () => {
  //   try {
  //     setIsGenerating(true);
  //     setError(null);

  //     const params = {
  //       complaintSeq,
  //       class_department,
  //       summary,
  //     };

  //     const response = await commentsApi.create(params);
  //     const formattedAnswer = formatAnswer(response.data);
  //     setAnswer(formattedAnswer);
  //   } catch (err) {
  //     console.error("답변 생성 중 오류 발생:", err);
  //     setError(err.message);
  //   } finally {
  //     setIsGenerating(false);
  //   }
  // };

  /**
   * 생성된 답변을 저장하는 함수
   *
   * @async
   * @function saveAnswer
   * @throws {Error} API 호출 실패 시 에러
   */
  const saveAnswer = async () => {
    try {
      setIsSaving(true);
      setError(null);
      setSuccess(false);

      const formData = new FormData();
      formData.append("memberSeq", memberSeq);
      formData.append("teamSeq", teamSeq);
      formData.append("title", "Complaint Answer");
      formData.append("content", answer);
      formData.append("complaintSeq", complaintSeq);

      const response = await complaintApi.create(formData);

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
      setIsSaving(false);
    }
  };

  // JSX 렌더링
  return (
    <div className={styles["main-content"]}>
      {/* 1. 질문 섹션 */}
      <div className={styles.section}>
        <div className={styles.summaryHeader}>
          <h3>1. 질문</h3>
        </div>
        <div className={styles.sectionContent}>{summary}</div>
      </div>

      {/* 2. 답변 폼 섹션 */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          saveAnswer();
        }}
        className={styles["answer-form-section"]}
      >
        <h2>2. 답변</h2>
        <textarea
          value={answer}
          onChange={(e) => setAnswer(e.target.value)}
          placeholder="답변 내용을 입력하세요"
          className={styles["answer-textarea"]}
          required
        />
        <div className={styles["answer-actions"]}>
        <p>오른쪽 버튼을 눌러 AI 답변 생성 서비스를 이용해보세요!</p>

          <button
            type="button"
            onClick={generateAnswer}
            disabled={isGenerating}
            className={styles["generate-button"]}
          >
            {isGenerating ? "생성 중..." : "AI 답변 생성"}
          </button>
          <button type="submit" disabled={isSaving || !answer}>
            {isSaving ? "저장 중..." : "저장"}
          </button>
        </div>
        {error && <p className={styles["error-message"]}>오류 발생: {error}</p>}
        {success && (
          <p className={styles["success-message"]}>저장이 완료되었습니다.</p>
        )}
      </form>

      {/* 3. 검색된 문서 섹션 */}
      <div className={styles.section}>
        <div className={styles.summaryHeader}>
          <h3>AI가 답변 생성에 참고한 문서</h3>
        </div>
        <div className={styles.sectionContent}>
          {retrievedDocs.map((doc, index) => (
            <div key={index} className={styles["doc-item"]}>
              <div className={styles["info-row"]}>
                <div className={styles["info-label"]}>문서 {index + 1}</div>
                <div className={styles["info-value"]}>{doc}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
//     <div className={styles["answer-form-section"]}>
//       <h2>답변하기</h2>
//       <textarea
//         rows="5"
//         value={answer}
//         onChange={(e) => setAnswer(e.target.value)}
//         placeholder="답변 내용을 입력하세요"
//         className={styles["answer-textarea"]}
//       />
//       <div className={styles["answer-actions"]}>
//         <p>오른쪽 버튼을 눌러 AI 답변 생성 서비스를 이용해보세요</p>
//         <button
//           onClick={generateAnswer}
//           disabled={isGenerating}
//           className={styles["generate-button"]}
//         >
//           {isGenerating ? "생성 중..." : "AI 답변 생성"}
//         </button>
//         <button
//           onClick={saveAnswer}
//           disabled={isSaving || !answer}
//         >
//           {isSaving ? "저장 중..." : "저장"}
//         </button>
//       </div>
//       {error && <p className={styles["error-message"]}>오류 발생: {error}</p>}
//       {success && <p className={styles["success-message"]}>저장이 완료되었습니다.</p>}
//     </div>
//   );
// };

// Props 타입 검증
AnswerForm.propTypes = {
  /** 민원 고유 번호 */
  complaintSeq: PropTypes.number.isRequired,
  /** 민원 처리 부서명 */
  class_department: PropTypes.string.isRequired,
  /** 민원 내용 요약 */
  summary: PropTypes.string.isRequired,
  /** 답변 작성자 ID (기본값: 1) */
  memberSeq: PropTypes.number,
  /** 답변 작성 팀 ID (기본값: 1) */
  teamSeq: PropTypes.number,
  /** JWT 인증 토큰 */
  jwtToken: PropTypes.string.isRequired,
};

export default AnswerForm;
// import { useState } from "react";
// import PropTypes from "prop-types";
// import styles from "../Answer.module.css";
// import { complaintApi } from "../../../../api";
// // import jwtAxios from "../../../../util/jwtUtils";
// import { commentsApi } from "../../../../api";

// /** AI 민원 답변 생성 및 저장 API 호출 컴포넌트 */
// const AnswerForm = ({
//   complaintSeq,
//   // departmentName,
//   class_department,
//   memberSeq = 1,
//   teamSeq = 1,
//   summary
// }) => {
//   const [answer, setAnswer] = useState(""); // 답변 내용
//   const [isGenerating, setIsGenerating] = useState(false); // 생성 중 상태
//   const [isSaving, setIsSaving] = useState(false); // 저장 중 상태
//   const [error, setError] = useState(null); // 에러 상태
//   const [success, setSuccess] = useState(false); // 저장 성공 여부

//   const formatAnswer = (responseData) => {
//     if (!responseData || !responseData.answer) {
//       return '응답을 받지 못했습니다.';
//     }

//     // 전체 응답 내용 그대로 사용
//     let formattedAnswer = responseData.answer;

//     // 참고 문서가 있다면 추가
//     if (responseData.retrievedDocs && responseData.retrievedDocs.length > 0) {
//       formattedAnswer += '\n\n참고 문서:\n';
//       formattedAnswer += responseData.retrievedDocs.map(doc =>
//         doc.split('\n').slice(0, 2).join('\n')
//       ).join('\n\n');
//     }

//     return formattedAnswer;
//   };
//   /**답변 생성 api (complaintSeq, departmentName) */
//   const generateAnswer = async () => {
//     try {
//       setIsGenerating(true);
//       const params = {
//         complaintSeq,
//         class_department,
//         summary
//       };
//       const response = await commentsApi.create(params);

//       const formattedAnswer = formatAnswer(response.data);
//       setAnswer(formattedAnswer);
//     } catch (err) {
//       console.error("답변 생성 중 오류 발생:", err);
//       setError(err.message);
//     } finally {
//       setIsGenerating(false);
//     }
//   };
//   const saveAnswer = async () => {
//     try {
//       setIsSaving(true); // 저장 중 상태로 변경
//       setError(null);
//       setSuccess(false);

//       // FormData 객체 생성
//       const formData = new FormData();
//       formData.append("memberSeq", memberSeq); // 사용자 ID
//       formData.append("teamSeq", teamSeq); // 팀/부서 ID
//       formData.append("title", "Complaint Answer"); // 제목 (하드코딩, 필요시 변경)
//       formData.append("content", answer); // 답변 내용
//       formData.append("complaintSeq", complaintSeq); // 민원 번호 (외부에서 전달)

//       const response = await complaintApi.create(formData);

//       if (response.status === 200) {
//         setSuccess(true);
//         alert(
//           `답변이 성공적으로 저장되었습니다! Complaint Seq: ${response.data.complaintSeq}`
//         );
//       }
//     } catch (err) {
//       console.error("저장 중 오류 발생:", err);
//       setError(err.message);
//     } finally {
//       setIsSaving(false); // 저장 완료
//     }
//   };

//   return (
//     <div className={styles["answer-form-section"]}>
//       <h2>답변하기</h2>
//       <textarea
//         rows="5"
//         value={answer}
//         onChange={(e) => setAnswer(e.target.value)}
//         placeholder="답변 내용을 입력하세요"
//         className={styles["answer-textarea"]}
//       ></textarea>
//       <div className={styles["answer-actions"]}>
//         <p>오른쪽 버튼을 눌러 AI 답변 생성 서비스를 이용해보세요</p>
//         <button
//           onClick={generateAnswer}
//           disabled={isGenerating}
//           className={styles["generate-button"]}
//         >
//           {isGenerating ? "생성 중..." : "AI 답변 생성  "}
//         </button>
//         <button onClick={saveAnswer} disabled={isSaving || !answer}>
//           {isSaving ? "저장 중..." : "저장"}
//         </button>
//       </div>
//       {error && <p className={styles["error-message"]}>오류 발생: {error}</p>}
//       {success && (
//         <p className={styles["success-message"]}>저장이 완료되었습니다.</p>
//       )}
//     </div>
//   );
// };

// /** PropTypes로 props 검증 */
// AnswerForm.propTypes = {
//   complaintSeq: PropTypes.number.isRequired, // 민원 번호, 필수
//   // departmentName: PropTypes.string.isRequired, // 부서이름, 필수
//   class_department: PropTypes.string.isRequired, // 부서이름, 필수
//   summary: PropTypes.string.isRequired, // 요약 필수
//   memberSeq: PropTypes.number, // 사용자 ID, 선택적 (기본값: 1)
//   teamSeq: PropTypes.number, // 팀/부서 ID, 선택적 (기본값: 1)
//   jwtToken: PropTypes.string.isRequired, // JWT 토큰, 필수
// };

// export default AnswerForm;
