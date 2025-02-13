import { Routes, Route } from "react-router-dom";
import HomePage from "../pages/home/Homepage";
import About from "../pages/about/About";
import LoginPage from "../pages/login/LoginPage";
import SignupPage from "../pages/signup/SignupPage";
import List from "../pages/board/List";
import BoardForm from "../pages/board/BoardForm";
import Detail from "../pages/board/Detail";
import PrivacyPolicy from "../_components/footer/privacypolicy";
import TermsOfService from "../_components/footer/TermsServie";
import ProtectedRoute from './ProtectedRoute';
 
const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
 
      {/* 보호된 라우트 */}
      <Route element={<ProtectedRoute/>}> 
        {/* 게시판 관련 */}
        <Route path="/complaints">
          <Route index element={<List />} />
          <Route path="create" element={<BoardForm isEdit={false} />} />
          <Route path=":id" element={<Detail />} />
          <Route path="edit/:id" element={<BoardForm isEdit={true} />} />
        </Route>
      </Route>
 
      {/* 공개 라우트 */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/about" element={<About />} />
      <Route path="/terms" element={<TermsOfService/>} />
      <Route path="/privacypolicy" element={<PrivacyPolicy />} />

 
      {/* 404 페이지 */}
      {/* <Route path="*" element={<NotFoundPage />} /> */}
    </Routes>
  );
};
 
export default AppRoutes;
 
