import { useState, useRef, useEffect } from 'react';
import styled from 'styled-components';
import jwtAxios from '../../../util/jwtUtils';
import uploadIcon from '../../../assets/images/upload_file.png';
import { useLocation } from 'react-router-dom'; // 추가된 import
import searchIcon from '../../../assets/images/search.png'


// 전체 컨테이너
const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  gap: 20px;
`;

// 검색 컴포넌트 스타일링 (기존과 동일)
const SearchContainer = styled.div`
  width: 700px;
  height: 118px;
  display: inline-flex;
  justify-content: flex-start;
  align-items: flex-start;
  gap: 10px;
  padding: 28px 9px;
`;

const SearchInputWrapper = styled.div`
  width: 620px;
  height: 62px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 13px;
  overflow: hidden;
  border: 2px solid #2A5C96;
  padding: 13px 20px;
`;

const SearchInput = styled.input`
  width: 450px;
  color: #626B73;
  font-size: 18.50px;
  font-family: 'Noto Sans KR';
  font-weight: 300;
  line-height: 30px;
  border: none;
  outline: none;
  &::placeholder {
    color: #626B73;
  }
`;

const SearchIcon = styled.div`
  width: 40px; /* 40px */
  height: 40px;
  padding: 0;
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: opacity 0.2s;
  
`;

const FileUploadButton = styled.div`
  width: 48px;
  height: 48px;
  position: relative;
  cursor: pointer;

  .button-bg {
    width: 46px;
    height: 44px;
    position: absolute;
    left: 0;
    top: 1px;
    background: #2A5C96;
    border-radius: 13px;
  }

  .button-icon {
    width: 30px;
    height: 30px;
    position: absolute;
    left: 8px;
    top: 8px;
    background: url(${uploadIcon}) no-repeat center;
    background-size: contain;
  }
`;

const HiddenFileInput = styled.input`
  display: none;
`;

// 응답 박스 스타일링
const ResponseContainer = styled.div`
  width: 796px;
  min-height: 300px;
  background: #F8F8F8;
  border-radius: 13px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const ResponseText = styled.div`
  color: #2D2D2D;
  font-size: 16px;
  font-family: 'Pretendard';
  line-height: 1.6;
`;

const LoadingSpinner = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  color: #2A5C96;
  font-size: 16px;
`;

const DocumentAssistantComponent = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedFile, setSelectedFile] = useState(null);
  const [response, setResponse] = useState(null);
  const [loading, setLoading] = useState(false);
  const fileInputRef = useRef(null);
  const location = useLocation(); // 추가된 hook

  useEffect(() => {
    if (location.state?.searchQuery) {
      setSearchTerm(location.state.searchQuery);
      // 홈페이지에서 전달받은 검색어로 자동 검색 실행
      const autoSearch = async () => {
        const query = location.state.searchQuery;
        const formData = new FormData();
        formData.append('searchTerm', query);
  
        try {
          setLoading(true);
          const response = await jwtAxios.post('/api/search', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });
          setResponse(response.data.response);
        } catch (error) {
          console.error('검색 중 오류 발생:', error);
          alert('검색 중 오류가 발생했습니다.');
        } finally {
          setLoading(false);
        }
      };
  
      autoSearch();
    }
  }, [location.state]);

  //엔터키 검색
  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.nativeEvent.isComposing) {
      handleSearch();
    }
  };

  //file selector, 파일 확장자, 사이즈 검증
  const handleFileSelect = (event) => {
    const file = event.target.files[0];
    
    // 허용된 파일 확장자 목록
    const allowedExtensions = ['hwpx', 'hwp', 'word', 'pdf', 'txt'];
    
    // 파일 확장자 추출
    const fileExtension = file.name.split('.').pop().toLowerCase();
    
    // 확장자 검증
    if (!allowedExtensions.includes(fileExtension)) {
      alert(`허용된 파일 확장자는 ${allowedExtensions.join(', ')}입니다.`);
      event.target.value = null; // 파일 선택 초기화
      setSelectedFile(null);
      return;
    }
  
    // 파일 크기 제한 (예: 10MB)
    const maxSize = 10 * 1024 * 1024; // 10MB
    if (file.size > maxSize) {
      alert('파일 크기는 10MB를 초과할 수 없습니다.');
      event.target.value = null;
      setSelectedFile(null);
      return;
    }
  
    setSelectedFile(file);
  };

  const handleSearch = async () => {
    if (!searchTerm.trim() && !selectedFile) {
      alert('검색어나 파일을 입력해주세요.');
      return;
    }

    const formData = new FormData();
    formData.append('searchTerm', searchTerm);
    
    if (selectedFile) {
      formData.append('file', selectedFile);
    }

    try {
      setLoading(true);
      const response = await jwtAxios.post('/api/search', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      setResponse(response.data.response);
    } catch (error) {
      console.error('검색 중 오류 발생:', error);
      alert('검색 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }

    // 입력 초기화
    setSearchTerm('');
    setSelectedFile(null);
  };

  const triggerFileInput = () => {
    fileInputRef.current.click();
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
            onKeyDown={handleKeyDown} // 엔터키 이벤트 추가
          />
          <SearchIcon onClick={handleSearch}>
            <img src={searchIcon} alt="" 
            style={{ width: '40px', height: '40px' }}/>
          </SearchIcon>
        </SearchInputWrapper>
        <FileUploadButton onClick={triggerFileInput}>
          <div className="button-bg" />
          <div className="button-icon" />
          <HiddenFileInput 
            type="file"
            ref={fileInputRef}
            onChange={handleFileSelect}
            accept=".hwpx, .hwp, .word, .pdf, .txt"
          />
        </FileUploadButton>
      </SearchContainer>

      <ResponseContainer>
        {loading ? (
          <LoadingSpinner>검색 중입니다...</LoadingSpinner>
        ) : response ? (
          <ResponseText>{response}</ResponseText>
        ) : (
          <ResponseText>검색 결과가 여기에 표시됩니다.</ResponseText>
        )}
      </ResponseContainer>
    </Container>
  );
};

export default DocumentAssistantComponent;