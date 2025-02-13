import { useState, useEffect } from "react";
import Contents from "./answer/Contents";
import AnswerForm from "./answer/AnswerForm";
import styles from "./Answer.module.css";
import { useParams, useNavigate } from "react-router-dom";
import { complaintApi } from "../../../api";
import AnswerSave from "./answer/AnswerSave";
import styled from "styled-components";

//** 민원 답변 글쓰기 페이지 : 백엔드 호출, 레이아웃 구성 담당 */
const ComplaintDetail = () => {
  const { id: complainSeq } = useParams(); // URL 파라미터에서 가져오도록 수정
  const navigate = useNavigate();
  const [postData, setPostData] = useState(null); // 백엔드 데이터 저장
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태
  const [answered, setAnswered] = useState(false); // ansered

  // const location = useLocation();
  // const answered = new URLSearchParams(location.search).get('answered') === 'true';
  // const answered = new URLSearchParams(location.search).get('commentResponseDTOList').length > 0;
  // if 0보다 크면 true false로

  const WarningMessage = styled.div`
    width: 917px;
    margin: 0 auto 15px auto;
    display: flex;
    align-items: center;
    gap: 8px;
    color: #dc2626;
    font-size: 16px;
    justify-content: flex-end; // 오른쪽 정렬
    text-align: left; // 텍스트는 왼쪽 정렬
    padding-right: 30px; // 오른쪽 여백 추가
  `;

  // 백엔드 api 호출 : title, isBad, content, summary, date -> 어떤 타입으로 주는지 확인 필요, prop-type validation변경 필요
  const fetchPostData = async () => {
    try {
      setLoading(true);
      const response = await complaintApi.getDetail(complainSeq);
      console.log(response);
      // const response = await jwtAxios.get(`/complaint-comments/${complainSeq}`);
      console.log("단건 조회 response", response.data);
      // commentResponseDTOList 길이 체크하여 answered 설정
      const hasComments =
        response.data.commentResponseDTOList &&
        response.data.commentResponseDTOList.length > 0;
      setAnswered(hasComments); // answered state 추가 필요
      const formattedData = {
        ...response.data,
        date: new Date(response.data.date)
          .toLocaleDateString("ko-KR", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
          })
          .replace(/\. /g, "-")
          .replace(".", ""), // YYYY-MM-DD 형식으로 변환
      };
      setPostData(formattedData);
      // console.log(answered);
    } catch (err) {
      console.error("데이터를 가져오는 중 오류 발생:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (complainSeq) {
      fetchPostData(); // 컴포넌트 마운트 시 호출
    }
  }, [complainSeq]);

  if (loading) return (
      <div className={styles.listPageContainer}>
        <p style={{ 
          textAlign: 'center', 
          position: 'absolute', 
          left: '50%', 
          top: '50%', 
          transform: 'translate(-50%, -50%)',
          color:'#2A5C96'
        }}>로딩 중...</p>
      </div>
    );
    if (error) return (
      <div className={styles.listPageContainer}>
        <p style={{ 
          textAlign: 'center', 
          position: 'absolute', 
          left: '50%', 
          top: '50%', 
          transform: 'translate(-50%, -50%)', 
          color:'red'
        }}>오류발생: {error}</p>
      </div>
    );
  
  return (
    <>
    <div className={styles["warning-container"]}>
    {postData?.bad && (
        <WarningMessage>
          해당 게시글은 특이민원으로 추정되는 게시글입니다!
        </WarningMessage>
      )}
    </div>
      <div className={styles["answer-detail-container"]}>
        <div className={styles["main-content"]}>
          {/* Title, Content, Summary */}
          <Contents data={postData} />
          {/* <AnswerSave/> */}
          {answered ? (
            <AnswerSave data={postData} />
          ) : (
            <AnswerForm
              complaintSeq={postData.complaintSeq}
              class_department={postData.departmentName} // departmentName을 class_department로 전달
              memberSeq={postData.memberSeq} // 필요하다면 이것도 추가
              summary ={postData.summary}// 요약
            />
          )}
          {/* Answer Form ......>  1. DTO list사이즈로 내가 계산해서 조건을 나눈다. 2. isCompleted = true 조건에 따라 AnswerForm 혹은 AnswerSave 뜨도록. 
        <AnswerForm complaintSeq={postData.complaint_seq} jwtToken="your-jwt-token" /> */}
        </div>
        <div
          className={styles.listButton}
          onClick={() => navigate("/complaint")}
        >
          목록
        </div>
      </div>
    </>
  );
};

export default ComplaintDetail;
