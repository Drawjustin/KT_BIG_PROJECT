import React from 'react';
import PropTypes from 'prop-types';
import styles from './LoginButton.module.css';

function LoginButton({ onClick, text = "로그인" }) {
  return (
    <button className={styles.loginButton} onClick={onClick}>
      {text}
    </button>
  );
}

// PropTypes 추가
LoginButton.propTypes = {
  onClick: PropTypes.func.isRequired, // onClick은 필수이며 함수 타입이어야 함
  text: PropTypes.string,            // text는 문자열 타입 (선택 사항)
};

export default LoginButton;
