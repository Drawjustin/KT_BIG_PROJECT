import { Routes, Route } from 'react-router-dom';
import HomePage from '../pages/home/Homepage';
import Complaint from '../pages/helper/complaint/Complaint';
import Official from '../pages/helper/\bofficial/Official';
import Filepage from '../pages/filepage/Filepage';
import About from '../pages/about/About';
import LoginPage from '../pages/login/LoginPage';
import SignupPage from '../pages/signup/SignupPage';
import MyPage from '../pages/mypage/myPage';
import ProtectedRoute from './ProtectedRoute';

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      
      {/* 보호된 라우트 */}
      <Route element={<ProtectedRoute />}>
        <Route path="/complaint/*" element={<Complaint />} />
        <Route path="/official" element={<Official />} />
        <Route path="/dataroom/*" element={<Filepage />} />
        <Route path="/myPage" element={<MyPage />} />
      </Route>

      {/* 공개 라우트 */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/about" element={<About />} />

      {/* 404 페이지 */}
      {/* <Route path="*" element={<NotFoundPage />} /> */}
    </Routes>
  );
};

export default AppRoutes;