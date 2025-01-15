import React, { useState } from 'react';
import Button from '../../_components/button/Button';
import Input from '../../_components/button/Input';

import styles from './LoginForm.module.css';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    console.log('Login button clicked');
    console.log('Email:', email);
    console.log('Password:', password);
    // 여기에 실제 로그인 로직 추가
  };

  return (
    <div className={styles.form}>
      <Input/>
      <Input/>
      <Button onClick={handleLogin}>로그인</Button>
    </div>
  );
};

export default LoginForm;
