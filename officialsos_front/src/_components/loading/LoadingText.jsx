// LoadingText.jsx
import { motion } from "framer-motion";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";

// const LoadingContainer = styled.div`
//   display: flex;
//   flex-direction: column;
//   justify-content: center;
//   align-items: center;
//   height: 100vh;
//   background: #f5f5f5;
//   cursor: pointer; /* 클릭 가능하도록 */
// `;
const LoadingContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 400px; // 100vh 대신 고정된 높이 사용
  background: transparent; // 배경색 제거
`;

const AnimatedText = styled(motion.div)`
  font-size: 24px;
  color: #2a5c96;
  pointer-events: none; /* 클릭 이벤트가 부모 컨테이너에 전달되도록 */
`;

const LoadingText = () => {
  const navigate = useNavigate();

  return (
    <LoadingContainer onClick={() => navigate("/")}>
      <AnimatedText
        animate={{ opacity: [0, 1] }}
        transition={{ duration: 0.8, repeat: Infinity, repeatType: "reverse" }}
      >
        AI가 공문서를 찾는 중입니다...
      </AnimatedText>
    </LoadingContainer>
  );
};

export default LoadingText;