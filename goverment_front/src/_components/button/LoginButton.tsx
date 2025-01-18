import styles from './LoginButton.module.css'

interface LoginButtonProps {
    onClick: () => void; // 클릭 이벤트 핸들러 타입
  }
  
  const LoginButton: React.FC<LoginButtonProps> = ({ onClick }) => {
    return (
      <button className={styles.loginButton} onClick={onClick}>로그인</button>
    );
  };
  
  export default LoginButton;