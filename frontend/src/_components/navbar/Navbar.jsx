import React from 'react';
import styles from './Navbar.module.css';
import { Link } from 'react-router-dom';
import LoginButton from '../button/LoginButton';

function Navbar() {
  const handleLoginClick = () => {
    console.log("Login button clicked!");
    // 여기에 로그인 로직 추가
  };

  return (
    <nav className={styles.navbar}>
      <div className={styles.logo}>
        {/* 로고 클릭 시 홈으로 이동 */}
        <Link to="/" className={styles.logoLink}>
          공무원SOS
        </Link>
      </div>
      <ul className={styles.menu}>
        <li>
          <Link to="/complaint" className={styles.menuLink}>민원 도우미</Link>
        </li>
        <li>
          <Link to="/official" className={styles.menuLink}>공문서 도우미</Link>
        </li>
        <li>
          <Link to="/dataroom" className={styles.menuLink}>자료실</Link>
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
}

export default Navbar;