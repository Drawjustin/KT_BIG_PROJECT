import styles from "./HomePage.module.css";
import searchIcon from "../../assets/images/search.png";
import logoSOS from "../../assets/images/logo.png";
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { useSelector} from 'react-redux'; // Redux 훅, 로그인 상태 확인을 위한(로그인/로그아웃 버튼)


const HomePage = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const isLoggedIn = useSelector((state) => state.login.isLoggedIn); // 로그인 상태 확인
  const userEmail = useSelector((state) => state.login.userEmail); // userEmail로드하기 위한 상태 저장
  console.log('Login State:', { isLoggedIn, userEmail });

  // const isLoggedIn = 1; // 로그인 후 상태 테스트를 위한 하드코딩
  const fullReduxState = useSelector((state) => state);
  
  useEffect(() => {
    console.log('Full Redux State:', fullReduxState);
    console.log('Login Slice State:', fullReduxState.login);
  }, [fullReduxState]);

  console.log('Login State:', { isLoggedIn, userEmail });

  const handleSearch = () => {
    if (searchTerm.trim()) {
      navigate("/official", { state: { searchQuery: searchTerm } });
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.nativeEvent.isComposing) {
      handleSearch();
    }
  };

  // 이메일 마스킹 함수
  const maskEmail = (email) => {
    if (!email) return "";
    const [localPart] = email.split("@"); // '@' 기준으로 앞부분만 가져옴
    if (localPart.length <= 3) return `${localPart}****`; // 너무 짧으면 전부 표시
    return `${localPart.slice(0, 3)}****`; // 앞 3글자 + ****
  };
  return (
    <main className={styles.main}>
      <div className={styles.container}>
        <div className={styles.contentWrapper}>
          <section className={styles.leftSection}>
            <h1 className={styles.banner}>
              업무효율 UP!
              <br />
              도와줘요, 공무원SOS
            </h1>
            
            <div className={styles.searchWrapper}>
              <div className={styles.searchBar}>
                <input
                  type="text"
                  placeholder="공문서 도우미에 검색어를 입력해주세요"
                  className={styles.searchInput}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  onKeyDown={handleKeyDown}
                />
                <button 
                  className={styles.searchButton} 
                  onClick={handleSearch}
                  aria-label="검색"
                >
                  <img src={searchIcon} alt="" />
                </button>
              </div>
            </div>
          </section>

          <section className={styles.rightSection}>
            <div className={styles.loginCard}>
            <img src={logoSOS} alt="SOS Logo" className={styles.logo} />
              {isLoggedIn ? (
                <>
                  <div className={styles.loginHeader}>
                    <h2 className={styles.loginTitle}>
                      {maskEmail(userEmail)}님,
                      <br />
                      환영합니다!
                    </h2>
                  </div>
                  
                  <button
                    className={styles.loginButton}
                    onClick={() => navigate("/mypage")}
                  >
                    마이페이지
                  </button>
                </>
              ) : (
                <>
                  <div className={styles.loginHeader}>
                    <h2 className={styles.loginTitle}>
                      공무원SOS의
                      <br />
                      다양한 서비스를 확인하세요.
                    </h2>
                  </div>
                
                  <button
                    className={styles.loginButton}
                    onClick={() => navigate("/login")}
                  >
                    로그인
                  </button>

                  <div className={styles.loginLinks}>
                    <button onClick={() => navigate("/signup")}>
                      회원가입
                    </button>
                  </div>
                </>
              )}
            </div>
          </section>
        </div>
      </div>
    </main>
  );
};

export default HomePage;