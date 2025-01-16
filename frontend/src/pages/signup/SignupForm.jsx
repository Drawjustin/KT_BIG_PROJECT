import React, { useState } from 'react';
import axios from 'axios';
import Input from '../../_components/button/Input';
import Button from '../../_components/button/Button';
import Checkbox from '../../_components/checkbox/Checkbox';
import styles from './SignUpForm.module.css';

const SignupForm = () => {
  const [form, setForm] = useState({
    name: '',
    email: '',
    username: '',
    password: '',
    confirmPassword: '',
    phone: '',
    agreeAll: false,
    agreeTerms: false,
    agreePrivacy: false,
    agreeLocation: false,
    agreeMarketing: false,
    agreeAge: false,
  });

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
        agreeLocation: checked,
        agreeMarketing: checked,
        agreeAge: checked,
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
    if (form.password !== form.confirmPassword) {
      alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
      return;
    }

    if (!form.agreeTerms || !form.agreePrivacy || !form.agreeAge) {
      alert('필수 약관에 동의해주세요.');
      return;
    }

    try {
      const response = await axios.post('/api/signup', form);
      console.log('API Response:', response.data);
      alert('회원가입이 성공적으로 완료되었습니다!');
    } catch (error) {
      console.error('API Error:', error);
      alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <Input
        type="text"
        name="name"
        placeholder="예) 홍길동"
        label="이름*"
        value={form.name} // 상태와 연결
        onChange={handleInputChange} // 상태 업데이트 함수
      />
      <Input
        type="email"
        name="email"
        placeholder="예) abc@gmail.com"
        label="이메일*"
        value={form.email} // 상태와 연결
        onChange={handleInputChange} // 상태 업데이트 함수
      />
      <Input
        type="text"
        name="username"
        placeholder="영문,숫자 조합 8-16자"
        label="아이디*"
        value={form.username} // 상태와 연결
        onChange={handleInputChange} // 상태 업데이트 함수
      />
      <Input
        type="password"
        name="password"
        placeholder="영문,숫자 조합 8-16자"
        label="비밀번호*"
        value={form.password} // 상태와 연결
        onChange={handleInputChange} // 상태 업데이트 함수
      />
      <Input
        type="password"
        name="confirmPassword"
        placeholder="비밀번호를 한 번 더 입력해주세요"
        label="비밀번호 확인*"
        value={form.confirmPassword} // 상태와 연결
        onChange={handleInputChange} // 상태 업데이트 함수
      />
      <Input
        type="text"
        name="phone"
        placeholder="휴대폰 번호 입력"
        label="휴대폰번호 본인인증*"
        value={form.phone} // 상태와 연결
        onChange={handleInputChange} // 상태 업데이트 함수
      />
      <Button type="button" onClick={() => console.log('인증 요청')}>인증하기</Button>

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
        <Checkbox
          label="위치정보 이용 약관 필수 동의"
          name="agreeLocation"
          checked={form.agreeLocation}
          onChange={handleCheckboxChange}
        />
        <Checkbox
          label="마케팅 정보 수신 선택 동의"
          name="agreeMarketing"
          checked={form.agreeMarketing}
          onChange={handleCheckboxChange}
        />
        <Checkbox
          label="만 14세 이상에 필수 동의"
          name="agreeAge"
          checked={form.agreeAge}
          onChange={handleCheckboxChange}
        />
      </div>

      <Button type="submit">회원가입</Button>
    </form>
  );
};

export default SignupForm;
