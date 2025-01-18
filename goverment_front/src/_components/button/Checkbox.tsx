import React, { ChangeEvent } from 'react';
import styles from './Checkbox.module.css';

// Props 타입 정의
interface CheckboxProps {
  label: string; // 체크박스 옆에 표시될 텍스트
  name: string; // 체크박스의 name 속성
  checked: boolean; // 체크 상태
  onChange: (e: ChangeEvent<HTMLInputElement>) => void; // 체크박스 상태 변경 핸들러
}

const Checkbox: React.FC<CheckboxProps> = ({ label, name, checked, onChange }) => {
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

export default Checkbox;