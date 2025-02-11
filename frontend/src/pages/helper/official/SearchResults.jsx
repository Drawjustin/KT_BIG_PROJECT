import styled from 'styled-components';

// 스타일 컴포넌트
const Container = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
`;

const Section = styled.div`
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
`;

const ResultItem = styled.div`
  padding: 15px 0;
  border-bottom: 1px solid #eee;
  &:last-child {
    border-bottom: none;
  }
`;

const Title = styled.h3`
  font-size: 18px;
  margin: 0 0 10px 0;
  color: #1a1a1a;
`;

const MetaInfo = styled.div`
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;

  span {
    margin-right: 12px;
  }
`;

const Summary = styled.p`
  color: #333;
  line-height: 1.6;
  margin: 0;
`;

const SearchResults = ({ data }) => {
  if (!data) return null;

  return (
    <Container>
      {/* 기본 답변 섹션 */}
      {data.base_answer && (
        <Section>
          <Title>기본 답변</Title>
          <Summary>{data.base_answer}</Summary>
        </Section>
      )}

      {/* 상위 검색결과 섹션 */}
      {data.high_results && data.high_results.length > 0 && (
        <Section>
          <Title>상위 검색결과</Title>
          {data.high_results.map((result, index) => (
            <ResultItem key={index}>
              <Title>{result.title}</Title>
              <MetaInfo>
                <span>부처: {result.ministry}</span>
                <span>부서: {result.department}</span>
                <span>날짜: {result.document_issue_date}</span>
                <span>점수: {result.score}</span>
              </MetaInfo>
              <Summary>{result.summary}</Summary>
            </ResultItem>
          ))}
        </Section>
      )}

      {/* 중위 검색결과 섹션 */}
      {data.medium_results && data.medium_results.length > 0 && (
        <Section>
          <Title>중위 검색결과</Title>
          {data.medium_results.map((result, index) => (
            <ResultItem key={index}>
              <Title>{result.title}</Title>
              <MetaInfo>
                <span>부처: {result.ministry}</span>
                <span>부서: {result.department}</span>
                <span>날짜: {result.document_issue_date}</span>
                <span>점수: {result.score}</span>
              </MetaInfo>
              <Summary>{result.summary}</Summary>
            </ResultItem>
          ))}
        </Section>
      )}

      {/* QnA 섹션 */}
      {data.qna_list && data.qna_list.length > 0 && (
        <Section>
          <Title>자주 묻는 질문</Title>
          {data.qna_list.map((qna, index) => (
            <ResultItem key={index}>
              <Title>{qna.question}</Title>
              <Summary>{qna.answer}</Summary>
            </ResultItem>
          ))}
        </Section>
      )}
    </Container>
  );
};

export default SearchResults;
