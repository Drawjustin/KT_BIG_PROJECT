import { useState, useEffect } from "react";
import axios from "axios";
import Contents from "./answer/Contents";
import AnswerForm from "./Answer/AnswerForm";

//** 민원 답변 글쓰기 페이지 : 백엔드 호출, 레이아웃 구성 담당 */
const AnswerWrite = () => {
  
  const [postData, setPostData] = useState(null); // 백엔드 데이터 저장
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태

  // 백엔드 api 호출 : title, isBad, content, summary, date -> 어떤 타입으로 주는지 확인 필요, prop-type validation변경 필요
  const fetchPostData = async () => {
    try {
      setLoading(true);
      const response = await axios.get("http://localhost:5000/api/post-detail"); // 백엔드 API 호출
      setPostData(response.data);
    } catch (err) {
      console.error("데이터를 가져오는 중 오류 발생:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPostData(); // 컴포넌트 마운트 시 호출
  }, []);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error}</p>;

  return (
    <div className="answer-detail-container">
      <div className="main-content">
        {/* Title, Content, Summary */}
        <Contents data={postData} />
        
        {/* Answer Form */}
        <AnswerForm />
      </div>
    </div>
  );
};

export default AnswerWrite;