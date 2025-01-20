import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './_components/navbar/Navbar';
import Footer from './_components/footer/Footer';
import HomePage from './pages/home/Homepage';
import Complaint from './pages/helper/complaint/Complaint';
import Official from './pages/helper/\bofficial/Official';
import Filepage from './pages/filepage/Filepage';
import About from './pages/about/About';
import LoginPage from './pages/login/LoginPage';
import SignupPage from './pages/signup/SignupPage';


function App() {
  return (
    <Router>
      {/* 공통 레이아웃 */}
      <Navbar />
      <Routes>
        {/* 라우트 설정 */}
        <Route path="/" element={<HomePage />} />
        <Route path="/complaint" element={<Complaint />} />
        <Route path="/official" element={<Official />} />
        <Route path="/dataroom" element={<Filepage/>} />
        <Route path="/about" element={<About />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        
        
        {/* <Route path="/" element={} />
        <Route path="/" element={} />
         */}
        

        {/* 추가 라우트는 여기에 추가 */}
      </Routes>
      <Footer />
    </Router>
  );
}

export default App;