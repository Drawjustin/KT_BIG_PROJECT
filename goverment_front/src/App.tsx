import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Navbar from './_components/navbar/Navbar';
import Footer from './_components/footer/Footer';
import About from './_pages/about/About';
import LoginPage from './_pages/login/LoginPage';
import SignupPage from './_pages/signup/SignupPage';
import HomePage from './_pages/home/HomePage';
import List from './_pages/board/List';
import Detail from './_pages/board/Detail';
import BoardForm from './_pages/board/BoardForm';


function App() {
  return (
    
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/about" element={<About />} />
        <Route path="/board" element={<List />} />
        <Route path="/board/:id" element={<Detail/>} />
        <Route path="/board/create" element={<BoardForm isEdit={false} />} /> {/**게시판 등록 */}
        <Route path="/board/edit/:id" element={<BoardForm isEdit={true} />} />{/**게시판 수정 */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage/>} />
      </Routes>
      <Footer />
    </Router>
  );
}

export default App;