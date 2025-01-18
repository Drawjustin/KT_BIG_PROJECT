import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Navbar from './_components/navbar/Navbar';
import Footer from './_components/footer/Footer';
import Home from './pages/home/Home';
import ComplaintPage from './pages/\bcomplaint/ComplaintPage';
import About from './pages/about/About';
import LoginPage from './pages/login/LoginPage';
import SignupPage from './pages/signup/SignupPage';

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/dataroom" element={<ComplaintPage/>} />
        <Route path="/about" element={<About />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage/>} />
      </Routes>
      <Footer />
    </Router>
  );
}

export default App;