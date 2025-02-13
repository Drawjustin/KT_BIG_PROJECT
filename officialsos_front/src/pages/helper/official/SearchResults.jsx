import styled from "styled-components";
import { useState } from "react";

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
  color: black;
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
// 추가 스타일 컴포넌트
const BaseResponseSection = styled(Section)`
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 30px;
`;

const ResultHeader = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  &:hover {
    text-decoration:underline;
    transform: translateX(5px);
  }
`;


const ScoreIndicator = styled.span`
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
  color: ${(props) =>
    props.score >= 0.9
      ? "#ff4444"
      : props.score >= 0.8
      ? "#ff8800"
      : props.score >= 0.7
      ? "#00C851"
      : "#666"};
`;

const DetailContent = styled.div`
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
  display: ${(props) => (props.isOpen ? "block" : "none")};
`;

const SummaryText = styled.p`
  color: #666;
  line-height: 1.6;
`;

const SectionTitle = styled(Title)`
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #2196f3;
`;

// 컴포넌트 수정
const SearchResults = ({ data }) => {
  const [openItems, setOpenItems] = useState({});

  const toggleItem = (section, index) => {
    setOpenItems((prev) => ({
      ...prev,
      [`${section}-${index}`]: !prev[`${section}-${index}`],
    }));
  };

  return (
    <Container>
      {data.base_answer && (
        <BaseResponseSection>
          <SectionTitle>기본 응답</SectionTitle>
          <Summary>{data.base_answer}</Summary>
        </BaseResponseSection>
      )}

      {data.high_results && data.high_results.length > 0 && (
        <Section>
          <SectionTitle>상위 검색결과</SectionTitle>
          {data.high_results.map((result, index) => (
            <ResultItem key={index}>
              <ResultHeader onClick={() => toggleItem("high", index)}>
                <Title>{result.title}</Title>
                <ScoreIndicator score={result.score}>
                  {result.score}%
                </ScoreIndicator>
              </ResultHeader>
              <DetailContent isOpen={openItems[`high-${index}`]}>
                <MetaInfo>
                  <span>부처: {result.ministry}</span>
                  <span>부서: {result.department}</span>
                  <span>날짜: {result.document_issue_date}</span>
                </MetaInfo>
                <SummaryText>{result.summary}</SummaryText>
              </DetailContent>
            </ResultItem>
          ))}
        </Section>
      )}

      {data.medium_results && data.medium_results.length > 0 && (
        <Section>
          <SectionTitle>중위 검색결과</SectionTitle>
          {data.medium_results.map((result, index) => (
            <ResultItem key={index}>
              <ResultHeader onClick={() => toggleItem("medium", index)}>
                <Title>{result.title}</Title>
                <ScoreIndicator score={result.score}>
                  {result.score}%
                </ScoreIndicator>
              </ResultHeader>
              <DetailContent isOpen={openItems[`medium-${index}`]}>
                <MetaInfo>
                  <span>부처: {result.ministry}</span>
                  <span>부서: {result.department}</span>
                  <span>날짜: {result.document_issue_date}</span>
                </MetaInfo>
                <SummaryText>{result.summary}</SummaryText>
              </DetailContent>
            </ResultItem>
          ))}
        </Section>
      )}

      {data.qna_list && data.qna_list.length > 0 && (
        <Section>
          <SectionTitle>자주 묻는 질문</SectionTitle>
          {data.qna_list.map((qna, index) => (
            <ResultItem key={index}>
              <Title>{qna.question}</Title>
              <SummaryText>{qna.answer}</SummaryText>
            </ResultItem>
          ))}
        </Section>
      )}
    </Container>
  );
};


export default SearchResults;
