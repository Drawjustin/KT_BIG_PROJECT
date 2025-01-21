import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store';
import { logout } from '../../slices/loginSlice';
import styles from './Navbar.module.css';

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const { isLoggedIn } = useSelector((state: RootState) => state.login);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/'); // 로그아웃 후 홈으로 이동
  };

  return (
    <nav className={styles.navbar}>
      <div className={styles.logo}>
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
        {isLoggedIn ? (
          <>
            <Link to="/mypage" className={styles.menuLink}>
              마이페이지
            </Link>
            <button onClick={handleLogout} className={styles.logoutButton}>
              로그아웃
            </button>
          </>
        ) : (
          <button onClick={() => navigate('/login')} className={styles.loginButton}>
            로그인
          </button>
        )}
      </div>
    </nav>
  );
};

export default Navbar;