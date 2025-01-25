import { useState, useEffect } from "react";
import axios from "axios";
import Contents from "./answer/Contents";
import AnswerSave from "./answer/AnswerSave";

/** 민원 답변 상세 페이지 */
const AnswerView = () => {
  const [postData, setPostData] = useState(null); // 게시물 데이터
  const [answerData, setAnswerData] = useState(null); // 저장된 답변 데이터
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태

  // 게시물 및 답변 데이터를 가져오는 API 호출
  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);

      // 게시물 데이터 가져오기
      const postResponse = await axios.get("http://localhost:5000/api/post-detail"); // 게시물 API
      setPostData(postResponse.data);

      // 답변 데이터 가져오기
      const complaintSeq = localStorage.getItem("complaintSeq"); // 저장된 complaintSeq
      if (complaintSeq) {
        const answerResponse = await axios.get(
          `http://localhost:8080/complaint/detail/${complaintSeq}` // 답변 API
        );
        setAnswerData(answerResponse.data);
      }
    } catch (err) {
      console.error("데이터를 가져오는 중 오류 발생:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // 컴포넌트 마운트 시 데이터 가져오기
  useEffect(() => {
    fetchData();
  }, []);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error}</p>;

  return (
    <div className="answer-view-container">
      {/* 게시물 내용 */}
      <Contents data={postData} />
      <hr />
      {/* 답변 내용 */}
      <AnswerSave data={answerData} />
    </div>
  );
};

export default AnswerView;