import styles from "./HomePage.module.css";
import searchIcon from "../../assets/images/search.png";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

const HomePage = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");

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
            </div>
          </section>
        </div>
      </div>
    </main>
  );
};

export default HomePage;