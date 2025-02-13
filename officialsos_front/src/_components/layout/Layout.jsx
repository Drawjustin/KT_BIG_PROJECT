// components/layout/Layout.jsx\
import PropTypes from 'prop-types';
import styles from './Layout.module.css';

function Layout({ children }) {
  return (
    <div className={styles.wrapper}>
      <div className={styles.container}>
        {children}
      </div>
    </div>
  );
}

// PropTypes를 사용한 타입 검증
Layout.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.node),
    PropTypes.node
  ]).isRequired
 };
export default Layout;