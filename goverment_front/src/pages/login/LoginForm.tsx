import React, { useState, ChangeEvent } from 'react';
import axios from 'axios';
import Button from '../../_components/button/Button';
import Input from '../../_components/button/Input';

import styles from './LoginForm.module.css';

// Form 데이터 타입 정의
interface FormState {
  email: string;
  password: string;
}

const LoginForm: React.FC = () => {
  const [form, setForm] = useState<FormState>({
    email: '',
    password: '',
  });

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  };

  const handleLogin = async () => {
    try {
      const response = await axios.post('/api/login', form);
      console.log('Login successful:', response.data);
      alert('로그인 성공!');
    } catch (error) {
      console.error('Login failed:', error);
      alert('로그인 실패. 다시 시도해주세요.');
    }
  };

  return (
    <div className={styles.form}>
      <Input
        type="email"
        name="email"
        placeholder="이메일 입력"
        label="이메일"
        value={form.email}
        onChange={handleInputChange}
      />
      <Input
        type="password"
        name="password"
        placeholder="비밀번호 입력"
        label="비밀번호"
        value={form.password}
        onChange={handleInputChange}
      />
      <Button onClick={handleLogin}>로그인</Button>
    </div>
  );
};

export default LoginForm;