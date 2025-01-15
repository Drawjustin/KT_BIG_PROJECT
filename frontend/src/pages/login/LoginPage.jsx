import React from 'react';
import styles from './LoginPage.module.css';
import LoginForm from './LoginForm';
import Navbar from '../../_components/navbar/Navbar';
import Footer from '../../_components/footer/Footer'
function LoginPage() {
  return (
    <>
    <Navbar />
    <div className={styles.container}>
      <h1 className={styles.title}>공무원 SOS</h1>
      <LoginForm />
      <div className={styles.links}>
        <a href="/signup">회원가입</a>
        <a href="/find-password">비밀번호 찾기</a>
        <a href="/find-id">아이디(이메일) 찾기</a>
      </div>
    </div>
    <Footer/>
    </>
    
  );
}

export default LoginPage;
