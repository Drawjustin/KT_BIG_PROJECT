import PropTypes from 'prop-types';
import styles from './Input.module.css';

const Input = ({
  label = '', // 기본값 설정
  type,
  name,
  placeholder,
  onChange,
  value = '', // 기본값 설정
  error = '', // 에러 메시지 기본값
  maxLength, // maxLength 추가
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
      maxLength={maxLength}
      className={`${styles.input} ${error ? styles.inputError : ''}`} // 에러 상태에 따른 스타일 적용
    />
    {error && <p className={styles.errorMessage}>{error}</p>} {/* 에러 메시지 표시 */}
  </div>
);

Input.propTypes = {
  label: PropTypes.string, // 라벨 텍스트 (선택)
  type: PropTypes.string.isRequired, // input 타입 (예: text, email, password)
  name: PropTypes.string.isRequired, // input의 name 속성
  placeholder: PropTypes.string.isRequired, // placeholder 텍스트
  onChange: PropTypes.func.isRequired, // 입력값 변경 핸들러
  value: PropTypes.string, // 입력값
  error: PropTypes.string, // 에러 메시지 (선택)
  maxLength: PropTypes.number, // 최대 길이 (선택)
};

export default Input;
