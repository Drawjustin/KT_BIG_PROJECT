import React from 'react';
import SignupForm from './SignupForm';
import styles from './SignUpPage.module.css';

const SignupPage = () => {
  return (
    
    <div className={styles.container}>
      <h1 className={styles.title}>회원가입</h1>
      <SignupForm />
    </div>
  );
};

export default SignupPage;
