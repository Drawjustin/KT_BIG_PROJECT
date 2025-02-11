import { useState } from 'react';
import styled from 'styled-components';
import { documentApi } from '../../../api';
import SearchResults from './SearchResults';
import AiBtn from './AiBtn';
import LoadingScreen from '../../../_components/loading/LoadingScreen';

// 스타일 컴포넌트
const SearchContainer = styled.div`
  padding: 20px;
  margin: 0 auto;
  max-width: 1200px;
`;

const SearchForm = styled.form`
  margin-bottom: 30px;
  display: flex;
  gap: 10px;
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
    border-color: #2196f3;
  }
`;

const SearchButton = styled.button`
  padding: 12px 24px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #1976d2;
  }

  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
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
          placeholder="ai에게 공문서 검색을 맡겨보세요!"
          disabled={loading}
        />
        <AiBtn onClick={handleSubmit}/>
        {/* <SearchButton type="submit" disabled={loading || !searchTerm.trim()}>
          {loading ? '검색 중...' : '검색'}
        </SearchButton> */}
      </SearchForm>

      {error && <ErrorMessage>{error}</ErrorMessage>}
      <ResultsContainer>
        {loading ? (
          <LoadingScreen />  // 또는 <LoadingText />
        ) : (
          searchResults && <SearchResults data={searchResults} />
        )}
      </ResultsContainer>
      {/* {loading ? (
        <LoadingSpinner>검색 중...</LoadingSpinner>
      ) : (
        searchResults && <SearchResults data={searchResults} />
      )} */}
    </SearchContainer>
  );
};

// SearchResults 컴포넌트는 이전에 작성한 것과 동일
// ... SearchResults 컴포넌트 코드 ...

export default SearchPage;