import styled from 'styled-components';

const BaselineTitle = styled.div`
  background: #2a5c96;
  width: 100%;
  height: 55px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const BaselineTitleText = styled.div`
  color: #ffffff;
  font-family: 'Pretendard-Regular';
  font-size: 20px;
`;

const AnswerBox = styled.div`
  background: #ffffff;
  border: 2px solid #99b0cb;
  width: 100%;
  min-height: 277px;
  padding: 20px;
`;

const AnswerText = styled.div`
  color: #000000;
  text-align: left;
  font-family: "Pretendard-Light", sans-serif;
  font-size: 18.5px;
  line-height: 30px;
  font-weight: 300;
`;

const AnswerSection = ({ title, content }) => {
  return (
    <>
      <BaselineTitle>
        <BaselineTitleText>{title}</BaselineTitleText>
      </BaselineTitle>
      <AnswerBox>
        <AnswerText>{content}</AnswerText>
      </AnswerBox>
    </>
  );
};

export default AnswerSection;

/** 사용예시 */
{/* <AnswerSection 
          title="base라인 답변"
          content={answer}
        /> */}