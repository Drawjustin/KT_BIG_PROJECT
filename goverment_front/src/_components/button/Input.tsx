import React, { ChangeEvent } from 'react';
import styles from './Input.module.css';

// Props 타입 정의
interface InputProps {
  label?: string; // 라벨 텍스트 (선택)
  type: string; // input 타입 (예: text, email, password)
  name: string; // input의 name 속성
  placeholder: string; // placeholder 텍스트
  onChange: (e: ChangeEvent<HTMLInputElement>) => void; // 입력값 변경 핸들러
  value?: string; // 입력값 (선택)
}

const Input: React.FC<InputProps> = ({
  label,
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

export default Input;