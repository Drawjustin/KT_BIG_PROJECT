import React, { ReactNode } from 'react';
import styles from './Button.module.css';

interface ButtonProps {
  children: ReactNode; // children은 ReactNode로 지정해 다양한 요소를 지원
  onClick: () => void; // onClick은 반환값이 없는 함수 타입
}

const Button: React.FC<ButtonProps> = ({ children, onClick }) => {
  return (
    <button className={styles.button} onClick={onClick}>
      {children}
    </button>
  );
};

export default Button;