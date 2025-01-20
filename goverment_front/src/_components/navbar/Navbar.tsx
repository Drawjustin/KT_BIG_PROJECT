import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './Navbar.module.css';
import LoginButton from '../button/LoginButton';

const Navbar: React.FC = () => {
  const navigate = useNavigate(); // useNavigate 훅 사용

  const handleLoginClick = () => {
    console.log("Login button clicked!");
    navigate('/login'); // 로그인 페이지로 이동
  };

  return (
    <nav className={styles.navbar}>
      <div className={styles.logo}>
        {/* 로고 클릭 시 홈으로 이동 */}
        <Link to="/" className={styles.logoLink}>
          국민 신문고
        </Link>
      </div>
      <ul className={styles.menu}>
        <li>
          <Link to="/board" className={styles.menuLink}>민원 게시판</Link>
        </li>
        <li>
          <Link to="/about" className={styles.menuLink}>이용안내</Link>
        </li>
      </ul>
      <div className={styles.loginButtonContainer}>
        <LoginButton onClick={handleLoginClick} />
      </div>
    </nav>
  );
};

export default Navbar;