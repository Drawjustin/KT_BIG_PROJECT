import styles from './Footer.module.css'; // CSS 모듈 전체를 객체로 가져옵니다.

function Footer() {
  return (
    <div className={styles.wrapper}>
      <div className={styles.copyright}>
        © 2025 My Company. All rights reserved.
      </div>
    </div>
  );
}

export default Footer;
