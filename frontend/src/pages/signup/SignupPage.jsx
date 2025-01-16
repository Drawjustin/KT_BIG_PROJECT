import React from 'react';
import SignUpForm from './SignupForm';
import styles from './SignUpPage.module.css';

const SignUpPage = () => {
  return (
    
    <div className={styles.container}>
      <h1 className={styles.title}>회원가입</h1>
      <SignUpForm />
    </div>
  );
};

export default SignUpPage;
