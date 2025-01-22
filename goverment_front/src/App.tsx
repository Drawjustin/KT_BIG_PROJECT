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
import { Provider } from 'react-redux';
import store from './store'; // store.ts파일
import ProtectedRoute from './routes/ProtectedRoute';

function App() {
  return (
    <Provider store={store}> {/* Redux Provider로 애플리케이션 감싸기 */}
      <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/about" element={<ProtectedRoute><About/></ProtectedRoute>} />
        <Route path="/board" element={<ProtectedRoute><List /></ProtectedRoute>} />
        <Route path="/board/:id" element={<ProtectedRoute><Detail/></ProtectedRoute>} />
        <Route path="/board/create" element={<ProtectedRoute><BoardForm isEdit={false} /></ProtectedRoute>} /> {/**게시판 등록 */}
        <Route path="/board/edit/:id" element={<ProtectedRoute><BoardForm isEdit={true} /></ProtectedRoute>} />{/**게시판 수정 */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage/>} />
      </Routes>
      <Footer />
    </Router>
    </Provider>
    
  );
}

export default App;