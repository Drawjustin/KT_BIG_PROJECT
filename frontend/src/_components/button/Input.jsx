
import React from 'react';
import PropTypes from 'prop-types';
import styles from './Input.module.css';

const Input = ({ type, placeholder, onChange }) => (
  <input
    type={type}
    placeholder={placeholder}
    onChange={onChange}
    className={styles.input}
  />
);

// PropTypes로 props 타입과 필수 여부를 정의
Input.propTypes = {
  type: PropTypes.string.isRequired,
  placeholder: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
};

// (선택 사항) 기본값 정의
Input.defaultProps = {
  type: 'text',
  placeholder: '입력해주세요',
};

export default Input;
