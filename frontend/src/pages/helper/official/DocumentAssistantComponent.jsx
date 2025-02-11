//=========================================================================
import { useState, useEffect } from "react";
import styled from "styled-components";
import { useLocation } from "react-router-dom";
import { documentApi } from "../../../api";
import AiBtn from "./AiBtn";
// import AnswerSection from "./AnswerSection";

// 전체 컨테이너 스타일링
const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 20px;
`;

// 검색 컨테이너 스타일링
const SearchContainer = styled.div`
  width: 700px;
  height: 118px;
  display: inline-flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 10px;
  padding: 28px 9px;
`;

// 검색 입력 래퍼 스타일링
const SearchInputWrapper = styled.div`
  width: 620px;
  height: 62px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 60px;
  overflow: hidden;
  border: 2px solid #2a5c96;
  padding: 13px 20px;
`;

// 검색 입력 필드 스타일링
const SearchInput = styled.input`
  width: 450px;
  color: #626b73;
  font-size: 18.5px;
  font-family: "Noto Sans KR";
  font-weight: 300;
  line-height: 30px;
  border: none;
  outline: none;
  &::placeholder {
    color: #626b73;
  }
`;
// 응답 텍스트 스타일링
// const ResponseText = styled.div`
//   color: #2d2d2d;
//   font-size: 16px;
//   font-family: "Pretendard";
//   line-height: 1.6;
// `;

// 로딩 스피너 스타일링
const LoadingSpinner = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  color: #2a5c96;
  font-size: 16px;
`;
// 문서 검색 컴포넌트 정의
const DocumentAssistantComponent = () => {
  // 검색어 상태 관리
  const [searchTerm, setSearchTerm] = useState("");

  // API 응답 상태 관리
  const [response, setResponse] = useState(null);

  // 로딩 상태 관리
  const [loading, setLoading] = useState(false);

  // 라우트 위치 정보
  const location = useLocation();

  // 라우트를 통해 전달된 검색어 처리 및 자동 검색
  useEffect(() => {
    // location.state에 searchQuery가 있는 경우
    if (location.state?.searchQuery) {
      // 검색어 상태 업데이트
      setSearchTerm(location.state.searchQuery);

      // 자동 검색 함수
      const autoSearch = async () => {
        try {
          // 로딩 상태 시작
          setLoading(true);

          // documentApi를 통해 검색 요청
          const response = await documentApi.search({
            user_query: location.state.searchQuery, // 검색어 전달
          });

          // 응답 상태 업데이트
          setResponse(response.data.response);
          setResponse(response.data);
          console.log("response", response );
        } catch (error) {
          // 에러 처리
          console.error("검색 중 오류 발생:", error);
          alert("검색 중 오류가 발생했습니다.");
        } finally {
          // 로딩 상태 종료
          // setLoading(false);
        }
      };

      // 자동 검색 실행
      autoSearch();
    }
  }, [location.state]); // location.state 변경 시 실행

  // 엔터키 검색 핸들러
  const handleKeyDown = (e) => {
    // 엔터키 입력 && 한글 입력 중 아닐 때
    if (e.key === "Enter" && !e.nativeEvent.isComposing) {
      handleSearch();
    }
  };
  
  
  const renderResponse = (response) => {
    return (
      <div style={{ width: '700px', padding: '20px' }}>
        <pre>{JSON.stringify(response, null, 2)}</pre>
      </div>
    );
   };
   
   return (
    <Container>
      <SearchContainer>
        <SearchInputWrapper>
          <SearchInput
            type="text" 
            placeholder="안녕하세요 공무원 도우미 입니다."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            onKeyDown={handleKeyDown}
          />
        </SearchInputWrapper>
        <AiBtn onClick={handleSearch} />
      </SearchContainer>
      {loading ? (
        <LoadingSpinner>검색중...</LoadingSpinner>
      ) : (
        response && renderResponse(response)
      )}
    </Container>
   );
};

export default DocumentAssistantComponent;
