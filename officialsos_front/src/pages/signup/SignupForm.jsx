/**
 * SignupForm 컴포넌트 - 사용자 회원가입 양식을 처리하는 React 컴포넌트
 * 
 * 주요 기능:
 * - 사용자 정보 입력 및 유효성 검사
 * - 구/부서/팀 선택 기능
 * - 약관 동의 처리
 * - 회원가입 API 연동
 */
import { useEffect, useState } from 'react';
import Input from '../../_components/button/Input';
import Button from '../../_components/button/Button';
import Checkbox from '../../_components/checkbox/Checkbox';
import styles from './SignUpForm.module.css';
import { useNavigate } from 'react-router-dom';
import { signApi } from '../../api';
import Select from 'react-select';

const SignupForm = () => {
  // 폼 상태 관리
  const [form, setForm] = useState({
    userName: '',            // 사용자 이름
    userEmail: '',          // 이메일
    userId: '',             // 아이디
    userPassword: '',       // 비밀번호
    confirmPassword: '',    // 비밀번호 확인
    userNumber: '',         // 전화번호
    agreeAll: false,        // 전체 약관 동의
    agreeTerms: false,      // 이용약관 동의
    agreePrivacy: false,    // 개인정보 처리방침 동의
  });

  // 유효성 검사 에러 메시지 상태 관리
  const [errors, setErrors] = useState({
    userName: '',
    userEmail: '',
    userId: '',
    userPassword: '',
    confirmPassword: '',
    userNumber: '',
  });

  // 로딩 상태와 서버 에러 관리
  const [loading, setLoading] = useState(false);
  const [serverError, setServerError] = useState('');
  const navigate = useNavigate();

  // 부서-팀 선택 상태 관리
  const [districts, setDistricts] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [teams, setTeams] = useState([]);
  const [selectedDistrict, setSelectedDistrict] = useState(null);
  const [selectedDepartment, setSelectedDepartment] = useState(null);
  const [selectedTeam, setSelectedTeam] = useState(null);
 /**
   * 구 목록을 불러오는 useEffect
   * 컴포넌트 마운트 시 실행되어 구 데이터를 가져옴
   */
 useEffect(() => {
  const fetchDistricts = async () => {
    try {
      const response = await signApi.getDistricts(); // api : 구 호출 (for select문)
      const districtList = response.data;
      
      const formattedDistricts = districtList.map(district => ({
        value: district.districtSeq,
        label: district.districtName
      }));
      setDistricts(formattedDistricts);
    } catch (error) {
      console.error('구 목록 로드 실패:', error);
    }
  };

  fetchDistricts();
}, []);
 /**
   * 구 선택 시 해당 구의 부서 목록을 불러오는 핸들러
   * @param {Object} selectedOption - 선택된 구 정보
   */
 const handleDistrictChange = async (selectedOption) => {
  setSelectedDistrict(selectedOption);
  setSelectedDepartment(null); // 부서 선택 초기화
  setSelectedTeam(null); // 팀 선택 초기화
  setTeams([]); // 팀 목록 초기화

  try {
    const response = await signApi.getDepartmentsByDistrict(selectedOption.value); 
    const departmentList = response.data;
    
    const formattedDepartments = departmentList.map(dept => ({
      value: dept.departmentSeq,
      label: dept.departmentName
    }));
    setDepartments(formattedDepartments);
  } catch (error) {
    console.error('부서 목록 로드 실패:', error);
  }
};

  
  /**
   * 부서 선택 시 해당 부서의 팀 목록을 불러오는 핸들러
   * @param {Object} selectedOption - 선택된 부서 정보
   */
  const handleDepartmentChange = async (selectedOption) => {
    setSelectedDepartment(selectedOption);
    setSelectedTeam(null); // 팀 선택 초기화

    try {
      const response = await signApi.getTeamsByDepartment(selectedOption.value);
      const teamList = response.data;
      
      const formattedTeams = teamList.map(team => ({
        value: team.teamSeq,
        label: team.teamName
      }));
      setTeams(formattedTeams);
    } catch (error) {
      console.error('팀 목록 로드 실패:', error);
    }
  };

  /**
   * 각 필드별 유효성 검사 함수
   * @param {string} name - 필드 이름
   * @param {string} value - 필드 값
   * @returns {string} 유효성 검사 에러 메시지
   */
  const validateField = (name, value) => {
    switch (name) {
      case 'userName':
        return value.length < 2 ? '이름은 2자 이상이어야 합니다.' : '';
      case 'userEmail':
        return !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value) 
          ? '올바른 이메일 형식이 아닙니다.' : '';
      case 'userId':
        return !/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/.test(value)
          ? '아이디는 영문, 숫자 조합 8-20자로 입력해주세요.' : '';
      case 'userPassword':
        // 비밀번호 복합적 유효성 검사
        if (value.length < 8 || value.length > 20) {
          return '비밀번호는 8-20자로 입력해주세요.';
        }
        if (!/[A-Za-z]/.test(value)) {
          return '비밀번호에 영문이 포함되어야 합니다.';
        }
        if (!/\d/.test(value)) {
          return '비밀번호에 숫자가 포함되어야 합니다.';
        }
        if (!/[@$!%*#?&]/.test(value)) {
          return '비밀번호에 특수문자(@$!%*#?&)가 포함되어야 합니다.';
        }
        if (/\s/.test(value)) {
          return '비밀번호에 공백이 포함될 수 없습니다.';
        }
        // 연속된 문자/숫자 체크
        if (/012|123|234|345|456|567|678|789/.test(value) || 
            /abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz/.test(value.toLowerCase())) {
          return '연속된 문자나 숫자는 사용할 수 없습니다.';
        }
        return '';
      case 'confirmPassword':
        return value !== form.userPassword ? '비밀번호가 일치하지 않습니다.' : '';
      case 'userNumber':
        return !/^\d{2,3}-\d{3,4}-\d{4}$/.test(value) 
          ? '올바른 전화번호 형식이 아닙니다.' : '';
      default:
        return '';
    }
  };

  /**
   * 입력 필드 변경 핸들러
   * @param {Event} e - 입력 이벤트 객체
   */
  const handleInputChange = (e) => {
    const { name, value } = e.target;

    if (name === 'userNumber') {
      // 전화번호 자동 하이픈 포맷팅
      const numbers = value.replace(/[^0-9]/g, '');
      let formattedNumber = '';
      if (numbers.length <= 3) {
        formattedNumber = numbers;
      } else if (numbers.length <= 7) {
        formattedNumber = `${numbers.slice(0, 3)}-${numbers.slice(3)}`;
      } else {
        formattedNumber = `${numbers.slice(0, 3)}-${numbers.slice(3, 7)}-${numbers.slice(7, 11)}`;
      }
      
      setForm(prevForm => ({
        ...prevForm,
        [name]: formattedNumber
      }));

      setErrors(prevErrors => ({
        ...prevErrors,
        [name]: validateField(name, formattedNumber)
      }));
    } else {
      // 일반 입력 필드 처리
      setForm(prevForm => ({
        ...prevForm,
        [name]: value
      }));

      setErrors(prevErrors => ({
        ...prevErrors,
        [name]: validateField(name, value)
      }));

      // 비밀번호 변경 시 비밀번호 확인 필드 재검증
      if (name === 'userPassword' && form.confirmPassword) {
        setErrors(prevErrors => ({
          ...prevErrors,
          confirmPassword: validateField('confirmPassword', form.confirmPassword)
        }));
      }
    }
  };

  /**
   * 약관 동의 체크박스 변경 핸들러
   * @param {Event} e - 체크박스 이벤트 객체
   */
  const handleCheckboxChange = (e) => {
    const { name, checked } = e.target;

    if (name === 'agreeAll') {
      // 전체 동의 처리
      setForm((prevForm) => ({
        ...prevForm,
        agreeAll: checked,
        agreeTerms: checked,
        agreePrivacy: checked,
      }));
    } else {
      // 개별 약관 동의 처리
      setForm((prevForm) => ({
        ...prevForm,
        [name]: checked,
        agreeAll: name !== 'agreeAll' && checked && 
          (name === 'agreeTerms' ? form.agreePrivacy : form.agreeTerms),
      }));
    }
  };

  /**
   * 전체 폼 유효성 검사
   * @returns {boolean} 폼이 유효한지 여부
   */
  const validateForm = () => {
    const newErrors = {};
    Object.keys(form).forEach(key => {
      if (key in errors) {
        newErrors[key] = validateField(key, form[key]);
      }
    });
    setErrors(newErrors);

    return !Object.values(newErrors).some(error => error !== '');
  };

  /**
   * 폼 제출 핸들러
   * @param {Event} e - 폼 제출 이벤트 객체
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    // 폼 유효성 검사
    if (!validateForm()) {
      return;
    }

    // 구/부서/팀 선택 검증
    if (!selectedDistrict || !selectedDepartment || !selectedTeam) {
      alert('구, 부서, 팀을 모두 선택해주세요.');
      return;
    }

    // 비밀번호 일치 확인
    if (form.userPassword !== form.confirmPassword) {
      alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
      return;
    }

    // 필수 약관 동의 확인
    if (!form.agreeTerms || !form.agreePrivacy ) {
      alert('필수 약관에 동의해주세요.');
      return;
    }

    setLoading(true);
    setServerError('');

    try {
      // API 제출 데이터 구성
      const userData = {
        userName: form.userName,
        userEmail: form.userEmail,
        userId: form.userId,
        userPassword: form.userPassword,
        userNumber: form.userNumber,
        userRole: 'USER',
        teamSeq: selectedTeam.value,
      };
              
      // 회원가입 API 호출
      const response = await signApi.register(userData);

      if (response.status === 201) {
        console.log('API Response:', response.data);
        alert('회원가입이 성공적으로 완료되었습니다!');
        navigate('/login');
      } else {
        alert('예상치 못한 오류가 발생했습니다.');
      }
    } catch (error) {
      // 에러 처리
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

  // JSX 렌더링
  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      {serverError && <p className={styles.serverError}>{serverError}</p>}
      {/* 사용자 정보 입력 필드들 */}
      <Input
        type="text"
        name="userName"
        placeholder="예) 홍길동"
        label="이름*"
        value={form.userName}
        onChange={handleInputChange}
        error={errors.userName}
      />
      <Input
        type="email"
        name="userEmail"
        placeholder="예) abc@gmail.com"
        label="이메일*"
        value={form.userEmail}
        onChange={handleInputChange}
        error={errors.userEmail}
      />
      <Input
        type="text"
        name="userId"
        placeholder="영문,숫자 조합 8-20자"
        label="아이디*"
        value={form.userId}
        onChange={handleInputChange}
        error={errors.userId}
      />
      <Input
        type="password"
        name="userPassword"
        placeholder="영문,숫자,특수문자 조합 8-20자"
        label="비밀번호*"
        value={form.userPassword}
        onChange={handleInputChange}
        error={errors.userPassword}
      />
      <Input
        type="password"
        name="confirmPassword"
        placeholder="비밀번호를 한 번 더 입력해주세요"
        label="비밀번호 확인*"
        value={form.confirmPassword}
        onChange={handleInputChange}
        error={errors.confirmPassword}
      />
      <Input
        type="tel"  // type을 tel로 변경
        name="userNumber"
        placeholder="예) 010-1234-5678"  // placeholder 수정
        label="휴대폰번호*"
        value={form.userNumber}
        onChange={handleInputChange}
        maxLength={13}  // 최대 길이 지정 (하이픈 포함)
        error={errors.userNumber}
      />
      {/* ... 나머지 Input 컴포넌트들 ... */}

      {/* 구/부서/팀 선택 드롭다운 */}
      <div>
        <label>구 선택*</label>
        <Select
          value={selectedDistrict}
          onChange={handleDistrictChange}
          options={districts}
          placeholder="구를 선택하세요"
        />
      </div>
      <div>
        <label>부서 선택*</label>
        <Select
          value={selectedDepartment}
          onChange={handleDepartmentChange}
          options={departments}
          placeholder="부서를 선택하세요"
          isDisabled={!selectedDistrict}
        />
      </div>

      <div>
        <label>팀 선택*</label>
        <Select
          value={selectedTeam}
          onChange={setSelectedTeam}
          options={teams}
          placeholder="팀을 선택하세요"
          isDisabled={!selectedDepartment}
        />
      </div>

      {/* 약관 동의 체크박스 */}
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
        {/* ... 나머지 Checkbox 컴포넌트들 ... */}
      </div>
      <button 
          type="button" 
          className={styles.linkButton} 
          onClick={() => navigate('/terms')}
        >
          이용약관 확인하기
        </button>
      <button 
          type="button" 
          className={styles.linkButton} 
          onClick={() => navigate('/privacypolicy')}
        >
          개인정보 처리방침 확인하기
        </button>


      {/* 제출 버튼 */}
      <button
        type="submit"
        className={styles.submitButton} 
        onClick={loading ? null : handleSubmit}
      >
        {loading ? '처리 중...' : '회원가입'}
      </button>
    </form>
      );
    };
export default SignupForm;
