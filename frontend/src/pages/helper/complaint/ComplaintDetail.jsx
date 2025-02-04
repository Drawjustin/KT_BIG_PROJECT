import { useState, useEffect } from "react";
import Contents from "./answer/Contents";
import AnswerForm from "./Answer/AnswerForm";
import styles from './Answer.module.css'
import { useParams } from "react-router-dom";
import { complaintApi } from "../../../api";
import AnswerSave from "./answer/AnswerSave";

//** 민원 답변 글쓰기 페이지 : 백엔드 호출, 레이아웃 구성 담당 */
const ComplaintDetail = () => {
  const { id :complainSeq } = useParams(); // URL 파라미터에서 가져오도록 수정

  const [postData, setPostData] = useState(null); // 백엔드 데이터 저장
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태
  

  // 백엔드 api 호출 : title, isBad, content, summary, date -> 어떤 타입으로 주는지 확인 필요, prop-type validation변경 필요
  const fetchPostData = async () => {
    try {
      setLoading(true);
      const response = await complaintApi.getDetail(complainSeq);
      // const response = await jwtAxios.get(`/complaint-comments/${complainSeq}`);
      
      const formattedData = {
        ...response.data,
        date: new Date(response.data.date).toLocaleDateString('ko-KR', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit'
        }).replace(/\. /g, '-').replace('.', '') // YYYY-MM-DD 형식으로 변환
      };
      setPostData(formattedData);

      
    } catch (err) {
      console.error("데이터를 가져오는 중 오류 발생:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if(complainSeq){
      fetchPostData(); // 컴포넌트 마운트 시 호출
    }
  }, [complainSeq]);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error}</p>;

  return (
    <div className={styles["answer-detail-container"]}>
      <div className={styles["main-content"]}>
        {/* Title, Content, Summary */}
        <Contents data={postData} />
        {postData.isAnswered ? (
          <AnswerSave data={postData.complaintSeq} />
        ) : (
          <AnswerForm 
            complaintSeq={postData.complaintSeq}
          />
        )}
        {/* Answer Form ......>  1. DTO list사이즈로 내가 계산해서 조건을 나눈다. 2. isCompleted = true 조건에 따라 AnswerForm 혹은 AnswerSave 뜨도록. 
        <AnswerForm complaintSeq={postData.complaint_seq} jwtToken="your-jwt-token" /> */}
      </div>
    </div>
  );
};

export default ComplaintDetail;