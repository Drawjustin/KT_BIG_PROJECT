import React from 'react';
import styles from './SignUpPage.module.css';
import SignupForm from './SignupForm';

const SignupPage: React.FC = () => {
  return (
    <div className={styles.container}>
      <h1 className={styles.title}>회원가입</h1>
      <SignupForm />
    </div>
  );
};

export default SignupPage;