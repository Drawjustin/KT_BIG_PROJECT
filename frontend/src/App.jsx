import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './_components/navbar/Navbar';
import Footer from './_components/footer/Footer';
import HomePage from './pages/home/Homepage';

function App() {
  return (
    <Router>
      {/* 공통 레이아웃 */}
      <Navbar />
      <Routes>
        {/* 라우트 설정 */}
        <Route path="/" element={<HomePage />} />
        

        {/* 추가 라우트는 여기에 추가 */}
      </Routes>
      <Footer />
    </Router>
  );
}

export default App;