import { useState,useEffect } from 'react';
import { useLocation } from "react-router-dom";
import styled from 'styled-components';
import { documentApi } from '../../../api';
import SearchResults from './SearchResults';
import AiBtn from './AiBtn';
import LoadingScreen from '../../../_components/loading/LoadingScreen';

// 스타일 컴포넌트
const SearchContainer = styled.div`
  padding: 20px;
  margin: 0 auto;
  max-width: 1000px;
`;

const SearchForm = styled.form`
  margin-bottom: 30px;
  display: flex;
  gap: 50px;
`;

const SearchInput = styled.input`
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e1e1e1;
  border-radius: 6px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.2s;

  &:focus {
    border-color: #2a5c96;
  }
`;
const ResultsContainer = styled.div`
  min-height: 400px; // 로딩 화면이 표시될 최소 높이
  position: relative; // 로딩 화면의 절대 위치 지정을 위해
`;


// const LoadingSpinner = styled.div`
//   text-align: center;
//   padding: 20px;
//   color: #666;
// `;

const ErrorMessage = styled.div`
  color: #d32f2f;
  background-color: #ffebee;
  padding: 12px;
  border-radius: 4px;
  margin-bottom: 20px;
`;
// SearchPage 컴포넌트 정의
const SearchPage = () => {
    // 상태 관리를 위한 useState 훅 사용
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // 라우트 위치 정보
  const location = useLocation();
  //메인페이지>공문서 도우미 검색
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
            setSearchResults(response.data);
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
  

 // 검색 폼 제출 핸들러
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!searchTerm.trim()) return;

    try {
      setLoading(true);
      setError(null);

     // API 호출로 검색 수행
      const response = await documentApi.search({
        user_query: searchTerm.trim()
      });

      console.log("검색 응답:", response);
      setSearchResults(response.data);
      // 데이터가 빈 문자열인 경우만 체크
      if (response.data === "") {
        alert("검색 결과가 없습니다. 다른 검색어로 다시 시도해주세요.");
        setSearchResults(null);
        return;
    }
        setSearchResults(response.data);

      
    } catch (error) {
      console.error("검색 오류:", error);
      setError("검색 중 오류가 발생했습니다. 다시 시도해주세요.");
      setSearchResults(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <SearchContainer>
      <SearchForm onSubmit={handleSubmit}>
        <SearchInput
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="AI에게 공문서 검색을 맡겨보세요!"
          disabled={loading}
        />
        <AiBtn onClick={handleSubmit}/>
      </SearchForm>

      {error && <ErrorMessage>{error}</ErrorMessage>}
      <ResultsContainer>
        {loading ? (
          <LoadingScreen />  // 또는 <LoadingText />
        ) : (
          searchResults && <SearchResults data={searchResults} />
        )}
      </ResultsContainer>
      
    </SearchContainer>
  );
};

export default SearchPage;
