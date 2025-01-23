import PropTypes from 'prop-types';
import styles from './Input.module.css';

const Input = ({
  label = '', // 기본값 설정
  type,
  name,
  placeholder,
  onChange,
  value = '', // 기본값 설정
}) => (
  <div className={styles.inputContainer}>
    {/* 라벨이 있는 경우 표시 */}
    {label && <label htmlFor={name} className={styles.label}>{label}</label>}
    <input
      id={name}
      type={type}
      name={name}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      className={styles.input} // 스타일 적용
    />
  </div>
);

Input.propTypes = {
  label: PropTypes.string, // 라벨 텍스트 (선택)
  type: PropTypes.string.isRequired, // input 타입 (예: text, email, password)
  name: PropTypes.string.isRequired, // input의 name 속성
  placeholder: PropTypes.string.isRequired, // placeholder 텍스트
  onChange: PropTypes.func.isRequired, // 입력값 변경 핸들러
  value: PropTypes.string, // 입력값
};

export default Input;