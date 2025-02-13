import styles from './LoginPage.module.css';
import LoginForm from './LoginForm';
import { Link } from 'react-router-dom';
function LoginPage() {
  return (
    <>
    <div className={styles.container}>
      <h1 className={styles.title}>공무원 SOS</h1>
      <LoginForm />
      <div className={styles.links}>
        <Link to="/signup">회원가입</Link>
      </div>
    </div>
    </>
    
  );
}

export default LoginPage;
