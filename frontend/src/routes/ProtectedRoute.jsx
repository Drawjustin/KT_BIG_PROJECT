import PropTypes from 'prop-types'; // PropTypes를 가져옴
import { Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

const ProtectedRoute = ({ children }) => {
  const isLoggedIn = useSelector((state) => state.login.isLoggedIn);

  if (!isLoggedIn) {
    // 비로그인 상태라면 로그인 페이지로 리다이렉트
    alert("로그인 권한이 필요합니다. 로그인 페이지로 이동합니다.");
    return <Navigate to="/login" replace />;
  }

  // 로그인 상태라면 요청한 컴포넌트를 렌더링
  return <>{children}</>;
};

// PropTypes로 props 유효성 검사 추가
ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired, // children이 필수이며 React 노드여야 함
};

export default ProtectedRoute;