import { Link, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux'; // Redux 훅
import { logoutAsync } from '../../slices/loginSlice';
import styles from './Navbar.module.css';

const Navbar = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // 로그인 상태를 Redux에서 가져오기
  const isLoggedIn = useSelector((state) => state.login.isLoggedIn);
  const handleLogout = async () => {
    try {
      await dispatch(logoutAsync()).unwrap(); // logoutAsync 액션 디스패치
      navigate('/'); // 로그아웃 성공 시 홈으로 이동
    } catch (error) {
      console.error('로그아웃 실패:', error);
      // 사용자에게 오류 메시지 표시
      alert('로그아웃 중 오류가 발생했습니다. 다시 시도해 주세요.');
    }
  };

  return (
    
    <nav className={styles.navbar}>
      <div className={styles.logo}>
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
        {isLoggedIn ? (
          <>
            {/* 로그인 상태일 때 표시 */}
            <Link to="/mypage" className={styles.logoutButton}>
              마이페이지
            </Link>
            <button onClick={handleLogout} className={styles.logoutButton}>
              로그아웃
            </button>
          </>
        ) : (
          // 비로그인 상태일 때 표시
          <button onClick={() => navigate('/login')} className={styles.loginButton}>
            로그인
          </button>
        )}
      </div>
    </nav>
  );
};

export default Navbar;