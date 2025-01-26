// import React, { useState, ChangeEvent } from 'react';
// import { useAppDispatch } from '../../hooks/dispatchhook'; // 커스텀 디스패치 훅
// import { loginPostAsync } from '../../slices/loginSlice';
// import Button from '../../_components/button/Button';
// import Input from '../../_components/button/Input';
// import styles from './LoginForm.module.css';
// import { useNavigate } from 'react-router-dom';

// const LoginForm: React.FC = () => {
//   const [form, setForm] = useState({ email: '', password: '' });
//   const dispatch = useAppDispatch();
//   const navigate = useNavigate(); // useNavigate 훅 사용


//   const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
//     const { name, value } = e.target;
//     setForm((prevForm) => ({
//       ...prevForm,
//       [name]: value,
//     }));
//   };

//   const handleLogin = async () => {
//     try {
//       // loginParam 선언
//       const loginParam = {
//         userEmail: form.email,
//         userPassword: form.password, // password를 pw로 매핑
//       };

//       // 비동기 디스패치 호출
//       await dispatch(loginPostAsync(loginParam)).unwrap();

//       // 성공 메시지
//       alert('로그인 성공!');
//       navigate('/'); // 리다이렉트 경로

//     } catch (error) {
//       // 실패 메시지
//       console.error('로그인 실패:', error);
//       alert('로그인 실패. 다시 시도해주세요.');
//     }
//   };

//   return (
//     <div className={styles.form}>
//       <Input
//         type="email"
//         name="email"
//         placeholder="이메일 입력"
//         label="이메일"
//         value={form.email}
//         onChange={handleInputChange}
//       />
//       <Input
//         type="password"
//         name="password"
//         placeholder="비밀번호 입력"
//         label="비밀번호"
//         value={form.password}
//         onChange={handleInputChange}
//       />
//       <Button onClick={handleLogin}>로그인</Button>
//     </div>
//   );
// };

// export default LoginForm;


// (test code) 백엔드 api호출 없이 로그인, isloggedin을 console로 확인. memberApi.ts도 같이 변경
import React, { useState, ChangeEvent, useEffect } from 'react';
import { useAppDispatch} from '../../hooks/dispatchhook';
import { loginPostAsync } from '../../slices/loginSlice';
import Button from '../../_components/button/Button';
import Input from '../../_components/button/Input';
import styles from './LoginForm.module.css';
import { useNavigate } from 'react-router-dom';
import { RootState } from '../../store';
import { useSelector } from 'react-redux';

const LoginForm: React.FC = () => {
  const [form, setForm] = useState({ email: '', password: '' });
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const isLoggedIn = useSelector((state: RootState) => state.login.isLoggedIn);


  // Redux 상태 변화 감지
  useEffect(() => {
    console.log('현재 Redux 상태 isLoggedIn:', isLoggedIn);
  }, [isLoggedIn]);


  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  };

  const handleLogin = async () => {
    try {
      const loginParam = { email: form.email, pw: form.password };
      const result = await dispatch(loginPostAsync(loginParam)).unwrap();

      // 성공 시 토큰 출력
      console.log('받아온 JWT 토큰:', result.accessToken);
      alert('로그인 성공!');
      navigate('/');
    } catch (error) {
      console.error('로그인 실패:', error);
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