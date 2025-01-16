import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Button, Typography, Grid, Divider, IconButton, InputAdornment, MenuItem } from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import CustomTextField from '../../_components/input/CustomTextField.jsx';
import Checkbox from '../../_components/checkbox/Checkbox';
import styles from './SignupForm.module.css';

const SignupFormT = () => {
    const navigate = useNavigate();
    const { register, handleSubmit, formState: { errors }, watch } = useForm({
        mode: 'onChange',
    });

    const [form, setForm] = useState({
        start_date: null,
        end_date: null,
        isEmailVerified: false,
        showPassword: false,
        showConfirmPassword: false,
    });

    const togglePasswordVisibility = (field) => {
        setForm((prev) => ({
            ...prev,
            [field]: !prev[field],
        }));
    };

    const handleEmailVerification = async () => {
        const email = watch('email');
        try {
            await axios.post('/signup/email', { email });
            setForm((prev) => ({ ...prev, isEmailVerified: true }));
        } catch (error) {
            console.error('Email verification failed:', error);
        }
    };

    const onSubmit = async (data) => {
        const { confirmPassword, ...formData } = data;
        formData.start_date = form.start_date ? form.start_date.toISOString() : null;
        formData.end_date = form.end_date ? form.end_date.toISOString() : null;

        try {
            await axios.post('/signup', formData);
            navigate('/login');
        } catch (error) {
            console.error('Signup failed:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className={styles.form}>
            <Typography variant="h5" align="center" gutterBottom>
                회원가입
            </Typography>
            <Divider sx={{ mb: 3 }} />

            <CustomTextField
                label="이름"
                {...register('name', { required: '이름을 입력해주세요.' })}
                error={!!errors.name}
                helperText={errors.name?.message}
            />

            <CustomTextField
                label="이메일"
                {...register('email', {
                    required: '이메일을 입력해주세요.',
                    pattern: {
                        value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                        message: '올바른 이메일 형식을 입력해주세요.',
                    },
                })}
                error={!!errors.email}
                helperText={errors.email?.message}
            />

            <Button
                variant="contained"
                color="primary"
                onClick={handleEmailVerification}
                disabled={form.isEmailVerified}
                sx={{ mt: 2 }}
            >
                {form.isEmailVerified ? '인증 완료' : '인증번호 받기'}
            </Button>

            <CustomTextField
                label="아이디"
                {...register('username', {
                    required: '아이디를 입력해주세요.',
                    minLength: { value: 4, message: '아이디는 4글자 이상이어야 합니다.' },
                    maxLength: { value: 16, message: '아이디는 16글자 이하이어야 합니다.' },
                })}
                error={!!errors.username}
                helperText={errors.username?.message}
            />

            <CustomTextField
                label="비밀번호"
                type={form.showPassword ? 'text' : 'password'}
                {...register('password', {
                    required: '비밀번호를 입력해주세요.',
                    pattern: {
                        value: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/,
                        message: '비밀번호는 영어, 숫자, 특수문자를 포함해야 합니다.',
                    },
                })}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <IconButton
                                onClick={() => togglePasswordVisibility('showPassword')}
                                edge="end"
                            >
                                {form.showPassword ? <VisibilityOff /> : <Visibility />}
                            </IconButton>
                        </InputAdornment>
                    ),
                }}
                error={!!errors.password}
                helperText={errors.password?.message}
            />

            <CustomTextField
                label="비밀번호 확인"
                type={form.showConfirmPassword ? 'text' : 'password'}
                {...register('confirmPassword', {
                    required: '비밀번호를 다시 입력해주세요.',
                    validate: (value) => value === watch('password') || '비밀번호가 일치하지 않습니다.',
                })}
                InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                            <IconButton
                                onClick={() => togglePasswordVisibility('showConfirmPassword')}
                                edge="end"
                            >
                                {form.showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                            </IconButton>
                        </InputAdornment>
                    ),
                }}
                error={!!errors.confirmPassword}
                helperText={errors.confirmPassword?.message}
            />

            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker
                    label="시작 날짜"
                    value={form.start_date}
                    onChange={(newValue) => setForm((prev) => ({ ...prev, start_date: newValue }))}
                    renderInput={(params) => <CustomTextField {...params} />}
                />
                <DatePicker
                    label="종료 날짜"
                    value={form.end_date}
                    onChange={(newValue) => setForm((prev) => ({ ...prev, end_date: newValue }))}
                    renderInput={(params) => <CustomTextField {...params} />}
                />
            </LocalizationProvider>

            <Checkbox
                label="아래 약관에 모두 동의합니다."
                name="agreeAll"
                onChange={(e) => {
                    const checked = e.target.checked;
                    setForm((prev) => ({
                        ...prev,
                        agreeAll: checked,
                        agreeTerms: checked,
                        agreePrivacy: checked,
                        agreeMarketing: checked,
                    }));
                }}
            />

            <Checkbox
                label="이용약관 필수 동의"
                name="agreeTerms"
                checked={form.agreeTerms}
                onChange={(e) => setForm((prev) => ({ ...prev, agreeTerms: e.target.checked }))}
            />

            <Checkbox
                label="개인정보 처리방침 필수 동의"
                name="agreePrivacy"
                checked={form.agreePrivacy}
                onChange={(e) => setForm((prev) => ({ ...prev, agreePrivacy: e.target.checked }))}
            />

            <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                sx={{ mt: 3 }}
            >
                회원가입
            </Button>
        </form>
    );
};

export default SignupFormT;
