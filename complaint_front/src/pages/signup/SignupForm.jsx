import { useState } from "react";
import Input from "../../_components/button/Input";
import Button from "../../_components/button/Button";
import Checkbox from "../../_components/checkbox/Checkbox";
import styles from "./SignUpForm.module.css";
import { useNavigate } from "react-router-dom";
import { signApi } from "../../api";

const SignupForm = () => {
  const [form, setForm] = useState({
    memberName: "",
    memberEmail: "",
    memberId: "",
    memberPassword: "",
    confirmPassword: "",
    memberNumber: "",
    agreeAll: false,
    agreeTerms: false,
    agreePrivacy: false,
  });
  const [errors, setErrors] = useState({
    memberName: "",
    memberEmail: "",
    memberId: "",
    memberPassword: "",
    confirmPassword: "",
    memberNumber: "",
  });

  const [loading, setLoading] = useState(false);
  const [serverError, setServerError] = useState("");
  const navigate = useNavigate();

  const validateField = (name, value) => {
    switch (name) {
      case "memberName":
        return value.length < 2 ? "이름은 2자 이상이어야 합니다." : "";
      case "memberEmail":
        return !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)
          ? "올바른 이메일 형식이 아닙니다."
          : "";
      case "memberId":
        return !/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/.test(value)
          ? "아이디는 영문, 숫자 조합 8-20자로 입력해주세요."
          : "";
      case "memberPassword":
        if (value.length < 8 || value.length > 20) {
          return "비밀번호는 8-20자로 입력해주세요.";
        }
        if (!/[A-Za-z]/.test(value)) {
          return "비밀번호에 영문이 포함되어야 합니다.";
        }
        if (!/\d/.test(value)) {
          return "비밀번호에 숫자가 포함되어야 합니다.";
        }
        if (!/[@$!%*#?&]/.test(value)) {
          return "비밀번호에 특수문자(@$!%*#?&)가 포함되어야 합니다.";
        }
        if (/\s/.test(value)) {
          return "비밀번호에 공백이 포함될 수 없습니다.";
        }
        // 연속된 문자/숫자 체크 (예: 123, abc)
        if (
          /012|123|234|345|456|567|678|789/.test(value) ||
          /abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz/.test(
            value.toLowerCase()
          )
        ) {
          return "연속된 문자나 숫자는 사용할 수 없습니다.";
        }
        return "";
      case "confirmPassword":
        return value !== form.memberPassword
          ? "비밀번호가 일치하지 않습니다."
          : "";
      case "memberNumber":
        return !/^\d{2,3}-\d{3,4}-\d{4}$/.test(value)
          ? "올바른 전화번호 형식이 아닙니다."
          : "";
      default:
        return "";
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    if (name === "memberNumber") {
      // 숫자만 추출
      const numbers = value.replace(/[^0-9]/g, "");

      // 전화번호 형식으로 변환
      let formattedNumber = "";
      if (numbers.length <= 3) {
        formattedNumber = numbers;
      } else if (numbers.length <= 7) {
        formattedNumber = `${numbers.slice(0, 3)}-${numbers.slice(3)}`;
      } else {
        formattedNumber = `${numbers.slice(0, 3)}-${numbers.slice(
          3,
          7
        )}-${numbers.slice(7, 11)}`;
      }

      setForm((prevForm) => ({
        ...prevForm,
        [name]: formattedNumber,
      }));

      setErrors((prevErrors) => ({
        ...prevErrors,
        [name]: validateField(name, formattedNumber),
      }));
    } else {
      setForm((prevForm) => ({
        ...prevForm,
        [name]: value,
      }));

      setErrors((prevErrors) => ({
        ...prevErrors,
        [name]: validateField(name, value),
      }));
      // 비밀번호 확인 필드 에러 업데이트
      if (name === "memberPassword" && form.confirmPassword) {
        setErrors((prevErrors) => ({
          ...prevErrors,
          confirmPassword: validateField(
            "confirmPassword",
            form.confirmPassword
          ),
        }));
      }
    }
  };

  const handleCheckboxChange = (e) => {
    const { name, checked } = e.target;

    if (name === "agreeAll") {
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
        agreeAll:
          name !== "agreeAll" &&
          checked &&
          (name === "agreeTerms" ? form.agreePrivacy : form.agreeTerms),
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};
    Object.keys(form).forEach((key) => {
      if (key in errors) {
        newErrors[key] = validateField(key, form[key]);
      }
    });
    setErrors(newErrors);

    return !Object.values(newErrors).some((error) => error !== "");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate form
    if (!validateForm()) {
      return;
    }

    if (form.memberPassword !== form.confirmPassword) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      return;
    }

    if (!form.agreeTerms || !form.agreePrivacy) {
      alert("필수 약관에 동의해주세요.");
      return;
    }

    setLoading(true);
    setServerError(""); // 서버 에러 초기화

    try {
      const memberData = {
        memberName: form.memberName,
        memberEmail: form.memberEmail,
        memberId: form.memberId,
        memberPassword: form.memberPassword,
        memberNumber: form.memberNumber,
        memberRole: "USER",
        teamSeq: 1,
      };

      const response = await signApi.register(memberData);
      if (response.status === 201) {
        console.log("API Response:", response.data);
        alert("회원가입이 성공적으로 완료되었습니다!");
        navigate("/login");
      } else {
        alert("예상치 못한 오류가 발생했습니다.");
      }
    } catch (error) {
      if (error.response) {
        switch (error.response.status) {
          case 400:
            alert(error.response.data.message || "입력값이 올바르지 않습니다.");
            break;
          case 409:
            alert("이미 사용 중인 이메일입니다.");
            break;
          case 500:
            alert("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
            break;
          default:
            alert("오류가 발생했습니다. 다시 시도해주세요.");
        }
        console.error("API Error:", error.response.data);
      } else {
        console.error("API Error:", error);
        alert("네트워크 오류가 발생했습니다. 인터넷 연결을 확인해주세요.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      {serverError && <p className={styles.serverError}>{serverError}</p>}
      <Input
        type="text"
        name="memberName"
        placeholder="예) 홍길동"
        label="이름*"
        value={form.memberName}
        onChange={handleInputChange}
        error={errors.memberName}
      />
      <Input
        type="email"
        name="memberEmail"
        placeholder="예) abc@gmail.com"
        label="이메일*"
        value={form.memberEmail}
        onChange={handleInputChange}
        error={errors.memberEmail}
      />
      <Input
        type="text"
        name="memberId"
        placeholder="영문,숫자 조합 8-20자"
        label="아이디*"
        value={form.memberId}
        onChange={handleInputChange}
        error={errors.memberId}
      />
      <Input
        type="password"
        name="memberPassword"
        placeholder="영문,숫자,특수문자 조합 8-20자"
        label="비밀번호*"
        value={form.memberPassword}
        onChange={handleInputChange}
        error={errors.memberPassword}
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
        type="tel" // type을 tel로 변경
        name="memberNumber"
        placeholder="예) 010-1234-5678" // placeholder 수정
        label="휴대폰번호*"
        value={form.memberNumber}
        onChange={handleInputChange}
        maxLength={13} // 최대 길이 지정 (하이픈 포함)
        error={errors.memberNumber}
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

      <Button type="submit" onClick={loading ? null : handleSubmit}
      >
        {loading ? "처리 중..." : "회원가입"}
      </Button>
    </form>
  );
};

export default SignupForm;
