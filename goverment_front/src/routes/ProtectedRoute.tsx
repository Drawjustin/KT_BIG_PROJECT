import React from 'react';
import { Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { RootState } from '../store'; // Redux RootState 타입

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const isLoggedIn = useSelector((state: RootState) => state.login.isLoggedIn);

  if (!isLoggedIn) {
    // 비로그인 상태라면 로그인 페이지로 리다이렉트
    alert("로그인 권한이 필요합니다. 로그인 페이지로 이동합니다.");
    return <Navigate to="/login" replace />;
  }

  // 로그인 상태라면 요청한 컴포넌트를 렌더링
  return <>{children}</>;
};

export default ProtectedRoute;