import React, { useState } from 'react';
import axios from 'axios';
import Input from '../../_components/button/Input';
import Button from '../../_components/button/Button';
import Checkbox from '../../_components/checkbox/Checkbox';
import styles from './SignUpForm.module.css';
import { useNavigate } from 'react-router-dom';

const SignupForm = () => {
  const [form, setForm] = useState({
    userName: '',
    userEmail: '',
    userId: '',
    userPassword: '',
    confirmPassword: '',
    userNumber: '',
    agreeAll: false,
    agreeTerms: false,
    agreePrivacy: false,
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate(); //navigate함수 초기화

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  };

  const handleCheckboxChange = (e) => {
    const { name, checked } = e.target;

    if (name === 'agreeAll') {
      setForm((prevForm) => ({
        ...prevForm,
        agreeAll: checked,
        agreeTerms: checked,
        agreePrivacy: checked,
      }));
    } else {
      setForm((prevForm) => ({
        ...prevForm,
        [name]: checked,
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate form
    if (form.userPassword !== form.confirmPassword) {
      alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
      return;
    }

    if (!form.agreeTerms || !form.agreePrivacy ) {
      alert('필수 약관에 동의해주세요.');
      return;
    }

    setLoading(true);

    try {
      const response = await axios.post(
        'http://localhost:8080/api/join', // api end point
        { // api request
          userName: form.userName,
          userEmail: form.userEmail,
          userId: form.userId,
          userPassword: form.userPassword,
          userNumber: form.userNumber,
          userRole: 'ADMIN',
        }
      );

      if (response.status === 201) {
        console.log('API Response:', response.data);
        alert('회원가입이 성공적으로 완료되었습니다!');
        navigate('/login');
      } else {
        alert('예상치 못한 오류가 발생했습니다.');
      }
    } catch (error) {
      // 이메일 중복 오류 처리
      if (error.response && error.response.status === 409) {
        alert('이미 사용 중인 이메일입니다.');
      } else {
        console.error('API Error:', error);
        alert('오류가 발생했습니다. 다시 시도해주세요.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <Input
        type="text"
        name="userName"
        placeholder="예) 홍길동"
        label="이름*"
        value={form.userName}
        onChange={handleInputChange}
      />
      <Input
        type="email"
        name="userEmail"
        placeholder="예) abc@gmail.com"
        label="이메일*"
        value={form.userEmail}
        onChange={handleInputChange}
      />
      <Input
        type="text"
        name="userId"
        placeholder="영문,숫자 조합 8-16자"
        label="아이디*"
        value={form.userId}
        onChange={handleInputChange}
      />
      <Input
        type="password"
        name="userPassword"
        placeholder="영문,숫자 조합 8-16자"
        label="비밀번호*"
        value={form.userPassword}
        onChange={handleInputChange}
      />
      <Input
        type="password"
        name="confirmPassword"
        placeholder="비밀번호를 한 번 더 입력해주세요"
        label="비밀번호 확인*"
        value={form.confirmPassword}
        onChange={handleInputChange}
      />
      <Input
        type="text"
        name="userNumber"
        placeholder="휴대폰 번호 입력"
        label="휴대폰번호*"
        value={form.userNumber}
        onChange={handleInputChange}
      />

      <div className={styles.checkboxes}>
        <Checkbox
          label="아래 약관에 모두 동의합니다."
          name="agreeAll"
          checked={form.agreeAll}
          onChange={handleCheckboxChange}
        />
        <Checkbox
          label="이용약관 필수 동의"
          name="agreeTerms"
          checked={form.agreeTerms}
          onChange={handleCheckboxChange}
        />
        <Checkbox
          label="개인정보 처리방침 필수 동의"
          name="agreePrivacy"
          checked={form.agreePrivacy}
          onChange={handleCheckboxChange}
        />
      </div>

      <Button
        type="submit"
        onClick={loading ? null : handleSubmit}
      >
        {loading ? '처리 중...' : '회원가입'}
      </Button>
    </form>
  );
};

export default SignupForm;