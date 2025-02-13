import { Link } from 'react-router-dom';
import styles from './Footer.module.css'; // CSS 모듈 전체를 객체로 가져옵니다.

function Footer() {
  return (
    <footer className={styles.wrapper}>
      {/* 상단 링크 */}
      <div className={styles.section}>
        <Link to="/terms" className={styles.link}>
          이용약관
        </Link>
        <span className={styles.separator}>|</span>
        <Link to="/privacypolicy" className={styles.link}>
          개인정보 처리방침
        </Link>
      </div>

      {/* 프로젝트 및 팀 정보 */}
      <div className={styles.section}>
        <span>현재 버전: v1.0.0</span>
        <span className={styles.separator}>|</span>
        <span>에이블스쿨 6기 2조 사두용미</span>
      </div>

      {/* 저작권 정보 */}
      <div className={styles.section}>
        <p className={styles.copyright}>
          © 2025 공무원 SOS. All rights reserved.
        </p>
      </div>
    </footer>
  );
}

export default Footer;