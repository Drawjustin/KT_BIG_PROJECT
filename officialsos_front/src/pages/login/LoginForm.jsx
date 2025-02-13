import { useState } from 'react';
import { useDispatch } from 'react-redux'; // Redux 디스패치 훅
import { loginPostAsync } from '../../slices/loginSlice';
import Button from '../../_components/button/Button';
import Input from '../../_components/button/Input';
import styles from './LoginForm.module.css';
import { useNavigate } from 'react-router-dom';

const LoginForm = () => {
  const [form, setForm] = useState({ userEmail: '', password: '' });
  const dispatch = useDispatch(); // Redux 디스패치 훅 사용
  const navigate = useNavigate(); // useNavigate 훅 사용

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  };

  const handleLogin = async () => {
    try {
      // loginParam 수정: userEmail, userPassword로 매핑
      const loginParam = {
        userEmail: form.userEmail,
        userPassword: form.password, // password -> userPassword로 변경
      };

      // 비동기 디스패치 호출
      await dispatch(loginPostAsync(loginParam)).unwrap();

      // 성공 메시지
      // alert('로그인 성공!');
      navigate('/'); // 리다이렉트 경로
    } catch (error) {
      const status = error.response?.status;
      if(status==400){ // 잘못된 요청
        console.error('[400 Error] 요청 형식 오류:', error.response?.data);
        alert('입력 형식이 올바르지 않습니다. 다시 확인해주세요.');

      }
      else if(status== 401){ // 인증실패, 아이디 비번 오류
        console.error('[401 Error] 인증 실패:', error.response?.data);
        alert('이메일 또는 비밀번호가 일치하지 않습니다.');
      }
      // 실패 메시지
      console.error('로그인 실패:', error);
      alert('로그인 실패. 다시 시도해주세요.');
    }
  };

  return (
    <div className={styles.form}>
      <Input
        type="email"
        name="userEmail"
        placeholder="이메일 입력"
        label="이메일"
        value={form.userEmail}
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
