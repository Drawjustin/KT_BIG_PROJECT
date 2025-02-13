import styles from './SignupPage.module.css';
import SignupForm from './SignupForm';
function SignupPage() {
  return (
    <>
    <div className={styles.container}>
      <h1 className={styles.title}>회원가입</h1>
      <SignupForm />
    </div>
    </>
    
  );
}

export default SignupPage;
