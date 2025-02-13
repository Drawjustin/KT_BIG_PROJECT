// Nav.js
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import styles from './Nav.module.css';

// menuItems의 타입 정의
/** 
 * @typedef {Object} MenuItem
 * @property {string} to - 링크 경로
 * @property {string} text - 메뉴 텍스트
 * @property {boolean} [isActive] - 활성화 여부 (선택적)
 */

/** 
 * @param {Object} props
 * @param {string} props.title - 네비게이션 제목
 * @param {MenuItem[]} props.menuItems - 메뉴 항목 배열
 */
const Nav = ({ title, menuItems }) => {
  return (
    <nav className={styles.nav}>
      <div className={styles.heading}>
        <h2 className={styles.title}>{title}</h2>
      </div>
      <div className={styles.list}>
        {menuItems.map((item, index) => (
          <Link
            key={index}
            to={item.to}
            className={`${styles.item} ${item.isActive ? styles.active : ''}`}
          >
            <span>{item.text}</span>
          </Link>
        ))}
      </div>
    </nav>
  );
};
// PropTypes를 사용한 타입 검증
Nav.propTypes = {
  title: PropTypes.string.isRequired,
  menuItems: PropTypes.arrayOf(
    PropTypes.shape({
      to: PropTypes.string.isRequired,
      text: PropTypes.string.isRequired,
      isActive: PropTypes.bool
    })
  ).isRequired
};

export default Nav;