import styles from "./HomePage.module.css";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { useSelector } from "react-redux";
import speakerImage from "../../assets/images/speaker.png";

const HomePage = () => {
  const navigate = useNavigate();
  const isLoggedIn = useSelector((state) => state.login.isLoggedIn);
  const memberEmail = useSelector((state) => state.login.memberEmail);

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
              <img src={speakerImage} alt="" />
              <br />
              편리하게 <span>민원</span>을 접수하고,
              <br />
              빠른처리를 경험하세요!
            </h1>
          </section>

          <section className={styles.rightSection}>
            <div className={styles.loginCard}>
              {isLoggedIn ? (
                <>
                  <div className={styles.loginHeader}>
                    <h2 className={styles.loginTitle}>
                      {maskEmail(memberEmail)}님,
                      <br />
                      환영합니다!
                    </h2>
                    <button
                    className={styles.loginButton}
                    onClick={() => navigate("/complaints")}
                  >
                    민원 접수
                  </button>
                  </div>
                </>
              ) : (
                <>
                  <div className={styles.loginHeader}>
                    <h2 className={styles.loginTitle}>
                      환영합니다!
                      <br />
                      <br />
                      로그인을 하고
                      <br />
                      민원을 접수해 보세요
                    </h2>
                  </div>

                  <button
                    className={styles.loginButton}
                    onClick={() => navigate("/login")}
                  >
                    로그인
                  </button>

                  <div className={styles.loginLinks}>
                    <button onClick={() => navigate("/signup")}>회원가입</button>
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