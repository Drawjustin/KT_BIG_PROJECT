// DataroomDetail.jsx
import styled from 'styled-components';
import PropTypes from 'prop-types';

const Container = styled.div`
  padding: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 963px;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
`;

const Header = styled.header`
  width: 100%;
  border-bottom: 1px solid #00162f;
  padding: 21px 30px;
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 16px;
`;

const Title = styled.h1`
  color: #00162f;
  font-family: "NotoSansKr-Regular", sans-serif;
  font-size: 23px;
  line-height: 34.5px;
  letter-spacing: -0.3px;
  font-weight: 400;
  margin: 0;
  flex: 1;
`;

const MetaInfo = styled.div`
  color: #00162f;
  font-family: "NotoSansKr-Regular", sans-serif;
  font-size: 15px;
  line-height: 22.5px;
  letter-spacing: -0.3px;
`;

const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
  padding: 20px 0;
`;

const ContentTitle = styled.h2`
  color: #666666;
  font-family: "NotoSansKr-Regular", sans-serif;
  font-size: 30px;
  line-height: 51px;
  letter-spacing: -0.3px;
  font-weight: 400;
  margin: 0;
`;

const ContentText = styled.div`
  color: #666666;
  font-family: "NotoSansKr-Regular", sans-serif;
  font-size: 15px;
  line-height: 1.6;
  letter-spacing: -0.3px;
  white-space: pre-line;
`;

const AttachmentSection = styled.div`
  width: 100%;
  border-top: 1px solid #dcdcdc;
  padding: 20px 30px;
`;

const AttachmentItem = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  gap: 10px;
`;

const FileName = styled.span`
  color: #666666;
  font-family: "NotoSansKr-Regular", sans-serif;
  font-size: 15px;
  flex: 1;
`;

const AttachButton = styled.button`
  background: #2a5c96;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 5px 10px;
  font-size: 11px;
  font-weight: bold;
  cursor: pointer;
  height: 21px;
  width: 47px;
  display: flex;
  align-items: center;
  justify-content: center;

  &:hover {
    background: #1f4571;
  }
`;

const FileDetail = ({ data }) => {
  return (
    <Container>
      <Header>
        <Title>{data.title}</Title>
        <MetaInfo>{data.date}</MetaInfo>
        <MetaInfo>{data.author}</MetaInfo>
      </Header>

      <Content>
        <ContentTitle>{data.contentTitle}</ContentTitle>
        <ContentText>{data.content}</ContentText>
      </Content>

      <AttachmentSection>
        {data.attachments.map((file, index) => (
          <AttachmentItem key={index}>
            <FileName>{file.name}</FileName>
            <AttachButton>첨부</AttachButton>
          </AttachmentItem>
        ))}
      </AttachmentSection>
    </Container>
  );
};

FileDetail.propTypes = {
  data: PropTypes.shape({
    title: PropTypes.string.isRequired,
    date: PropTypes.string.isRequired,
    author: PropTypes.string.isRequired,
    contentTitle: PropTypes.string.isRequired,
    content: PropTypes.string.isRequired,
    attachments: PropTypes.arrayOf(
      PropTypes.shape({
        name: PropTypes.string.isRequired,
        url: PropTypes.string
      })
    ).isRequired
  }).isRequired
};

export default FileDetail;
