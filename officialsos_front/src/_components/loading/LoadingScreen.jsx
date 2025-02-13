import { useState, useEffect } from "react";
import Lottie from "lottie-react";
import styled from "styled-components";
import loadingAnimation from "../../assets/animation/loading.json";

// const LoadingContainer = styled.div`
//   display: flex;
//   flex-direction: column;
//   justify-content: center;
//   align-items: center;
//   height: 100vh;
//   background: #f5f5f5;
// `;
const LoadingContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 400px; // 100vh 대신 고정된 높이 사용
  background: transparent; // 배경색 제거
`;

const TypingText = styled.div`
  font-size: 20px;
  color: #2a5c96;
  font-weight: bold;
  margin-top: 20px;
`;

const LoadingScreen = () => {
  const text = "AI가 공문서를 찾는 중입니다...";
  const [displayText, setDisplayText] = useState("");
  const [isActive, setIsActive] = useState(true); // 컴포넌트 활성 상태 추가

  useEffect(() => {
    if (!isActive) return; // 컴포넌트가 비활성 상태면 실행하지 않음
        let cancelled = false;

    const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

    const typeEffect = async () => {      
      while (!cancelled && isActive) {        
        for (let i = 0; i <= text.length && !cancelled && isActive; i++) {
          setDisplayText(text.slice(0, i));
          await sleep(150);
        }
        if (!cancelled && isActive) {
          await sleep(1000);
          setDisplayText("");
        }
      }
    };

    typeEffect();

    return () => {
      cancelled = true;
      setIsActive(false);
    };
  }, [text, isActive]);

  // 컴포넌트가 마운트될 때 isActive를 true로 설정
  useEffect(() => {
    setIsActive(true);
    return () => setIsActive(false);
  }, []);

  return (
    <LoadingContainer>
      <Lottie
        animationData={loadingAnimation}
        loop={true}
        style={{ width: 200, height: 200 }}
      />
      <TypingText>{displayText}</TypingText>
    </LoadingContainer>
  );
};

export default LoadingScreen;