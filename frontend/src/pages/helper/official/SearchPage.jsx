import { useState } from 'react';
import styled from 'styled-components';
import { documentApi } from '../../../api';
import SearchResults from './SearchResults';

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

const LoadingSpinner = styled.div`
  text-align: center;
  padding: 20px;
  color: #666;
`;

const ErrorMessage = styled.div`
  color: #d32f2f;
  background-color: #ffebee;
  padding: 12px;
  border-radius: 4px;
  margin-bottom: 20px;
`;

const SearchPage = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!searchTerm.trim()) return;

    try {
      setLoading(true);
      setError(null);

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
          placeholder="검색어를 입력하세요"
          disabled={loading}
        />
        <SearchButton type="submit" disabled={loading || !searchTerm.trim()}>
          {loading ? '검색 중...' : '검색'}
        </SearchButton>
      </SearchForm>

      {error && <ErrorMessage>{error}</ErrorMessage>}
      
      {loading ? (
        <LoadingSpinner>검색 중...</LoadingSpinner>
      ) : (
        searchResults && <SearchResults data={searchResults} />
      )}
    </SearchContainer>
  );
};

// SearchResults 컴포넌트는 이전에 작성한 것과 동일
// ... SearchResults 컴포넌트 코드 ...

export default SearchPage;