import { useState, useEffect } from "react";
import { useParams } from "react-router-dom"; // useParams 임포트
import axios from "axios";
import Contents from "./answer/Contents";
import AnswerSave from "./answer/AnswerSave";
import styles from './Answer.module.css'
import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { complaintApi } from "../../../api";
/** 민원 답변 상세 페이지 */
const AnswerView = () => {
  const [postData, setPostData] = useState(null); // 게시물 데이터
  const [answerData, setAnswerData] = useState(null); // 저장된 답변 데이터
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태
  const { id } = useParams(); // URL에서 id 파라미터 추출
  const navigate = useNavigate();

  // // 임의 데이터 설정
  // useEffect(() => {
  //   setLoading(true);

  //   // mockPostData와 mockAnswerData를 id를 기반으로 설정할 수 있습니다.
  //   const mockPostData = {
  //     title: `민원 제목 예시 ${id}`,
  //     isBad: false,
  //     content: "이것은 민원 내용입니다.",
  //     summary: "요약된 민원 내용",
  //     date: "2025-01-25",
  //     department: "고객 서비스",
  //     complaint_seq: id, // URL에서 가져온 id를 complaint_seq로 사용
  //   };
  //   const mockAnswerData = {
  //     title: `답변 제목 예시 ${id}`,
  //     content: "이것은 답변 내용입니다.",
  //     memberName: "홍길동",
  //     departmentName: "고객 서비스",
  //     updatedAt: "2025-01-25T12:00:00Z",
  //     filePath: "https://example.com/file.pdf",
  //   };

  //   setPostData(mockPostData);
  //   setAnswerData(mockAnswerData);
  //   setLoading(false);
  // }, [id]); // id가 변경될 때마다 다시 데이터 로드


  // 게시물 및 답변 데이터를 가져오는 API 호출
  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);

      // 게시물 데이터 가져오기
      const response = await complaintApi.getDetail(complaintSeq);
      // const postResponse = await jwtAxios.get(`/complaint/${complaintSeq}`); // 게시물 API
      setPostData(response.data);

      // 답변 데이터 가져오기
      const complaintSeq = localStorage.getItem("complaintSeq"); // 저장된 complaintSeq
      if (complaintSeq) {
        const answerResponse = await axios.get(
          `http://localhost:8080/complaint/${complaintSeq}` // 답변 API BE
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

  // 컴포넌트 마운트 시 데이터 가져오기 (id가 변경될때마다)
  useEffect(() => {
    fetchData();
  }, [id]);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error}</p>;

  return (
    <div className={styles["answer-view-container"]}>
      {/* 게시물 내용 */}
      <Contents data={postData} />
      <hr />
      {/* 답변 내용 */}
      <AnswerSave data={answerData} />
      {/*목록으로 리다이렉트 */}
      <Button onClick={() => navigate("/complaint")}>목록</Button> 
    </div>
  );
};

export default AnswerView;