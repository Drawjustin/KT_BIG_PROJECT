import React from 'react';
import styles from './HomePage.module.css'; // 스타일 파일

const HomePage = () => {
  return (
    <div className={styles.container}>

      <main className={styles.mainContent}>
        <h1>국민 신문고</h1>
        <p>국민 신문고에 오신 것을 환영합니다.</p>
        <p>국민들의 다양한 목소리를 경청하겠습니다!</p>
      </main>

    </div>
  );
};

export default HomePage;