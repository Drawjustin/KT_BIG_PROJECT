import React from 'react';
import styles from './Navbar.module.css';

function Navbar() {


  return (
    <nav className={styles.navbar}>
      <div className={styles.logo}>공무원SOS</div>
      <ul className={styles.menu}>
        <li>
          <a href="/" className={styles.menuLink}>Home</a>
        </li>
        <li>
          <a href="/complainhelper" className={styles.menuLink}>민원 도우미</a>
        </li>
        <li>
          <a href="/docuhelper" className={styles.menuLink}>공문서 도우미</a>
        </li>
        <li>
          <a href="/dataroom" className={styles.menuLink}>자료실</a>
        </li>
        <li>
          <a href="/about" className={styles.menuLink}>이용안내</a>
        </li>
      </ul>
      <div>
        btn
      </div>
    </nav>
  );
}

export default Navbar;
