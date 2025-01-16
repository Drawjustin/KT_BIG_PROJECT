import React from 'react';
import styles from './HomePage.module.css'; // 스타일 파일

const HomePage = () => {
  return (
    <div className={styles.container}>

      <main className={styles.mainContent}>
        <h1>공무원 SOS에 오신 것을 환영합니다!</h1>
        <p>공무원 SOS는 민원 도우미, 공문서 도우미, 자료실 등 다양한 서비스를 제공합니다.</p>
        <p>로그인하거나 회원가입하여 서비스를 이용해보세요.</p>
      </main>

    </div>
  );
};

export default HomePage;