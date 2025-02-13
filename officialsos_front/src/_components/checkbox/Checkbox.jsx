import PropTypes from 'prop-types';
import styles from './Checkbox.module.css';

const Checkbox = ({ label, name, checked, onChange }) => {
  return (
    <div className={styles.checkboxContainer}>
      <label className={styles.checkboxLabel}>
        <input
          type="checkbox"
          name={name}
          checked={checked}
          onChange={onChange}
          className={styles.checkboxInput}
        />
        {label}
      </label>
    </div>
  );
};

Checkbox.propTypes = {
  label: PropTypes.string.isRequired, // 체크박스 옆에 표시될 텍스트
  name: PropTypes.string.isRequired, // 체크박스의 name 속성
  checked: PropTypes.bool.isRequired, // 체크 상태
  onChange: PropTypes.func.isRequired, // 체크박스 상태 변경 핸들러
};

export default Checkbox;