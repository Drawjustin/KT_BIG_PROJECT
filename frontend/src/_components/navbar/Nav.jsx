import { NavLink } from "react-router-dom";
import styled from "styled-components";

const NavContainer = styled.nav`
  width: 240px;
  display: flex;
  flex-direction: column;
  border: 1px solid #e0e0e0;
  border-radius: 12px; /* ✅ 모서리 더 둥글게 */
  overflow: hidden;
  background-color: #ffffff; /* ✅ 배경 흰색 유지 */
  margin-left: -50px; /* ✅ 여백 조정 */
  margin-right: 10px;
  margin-top: 120px; /* ✅ 기존보다 약간 위로 */
  padding: 0;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* ✅ 부드러운 그림자 효과 */
`;

const NavHeader = styled.div`
  background-color: #ffffff; /* ✅ 마이페이지 배경 흰색 유지 */
  border-bottom: 2px solid #2a5c96;
  padding: 20px;
  text-align: center;
`;

const NavTitle = styled.h2`
  color: #2a5c96; /* ✅ 강조 색상 */
  font-size: 1.5rem;
  margin: 0;
  font-weight: 700; /* ✅ 더 두껍게 */
`;

const NavList = styled.div`
  display: flex;
  flex-direction: column;
`;

const StyledNavLink = styled(NavLink)`
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  text-decoration: none;
  font-size: 1.1rem;
  font-weight: 500;
  transition: all 0.3s ease;
  border-bottom: 1px solid #e0e0e0;
  color: #333;
  background: #f8f9fa; /* ✅ 기본 배경 추가 */

  &.active {
    background-color: #2a5c96;
    color: #ffffff;
    font-weight: 600;
    border-radius: 0px 0px 12px 12px; /* ✅ 선택 시 더 부드러운 느낌 */
    &:hover {
      background-color: #1f4a7b;
    }
  }

  &:hover {
    background-color: #e9f0fa; /* ✅ 살짝 더 밝은 색감 */
  }

  &:last-child {
    border-bottom: none; /* ✅ 마지막 요소 구분선 제거 */
  }
`;

/* 반응형 디자인 추가 */
const ResponsiveNav = styled(NavContainer)`
  @media screen and (max-width: 768px) {
    width: 200px;
    margin-left: 10px;
    margin-right: 10px;
  }
`;

const Nav = () => {
  return (
    <ResponsiveNav>
      <NavHeader>
        <NavTitle>마이페이지</NavTitle>
      </NavHeader>
      <NavList>
        <StyledNavLink to="/mypage">
          내 정보 확인
        </StyledNavLink>
        <StyledNavLink to="/dashboard">
          대시보드
        </StyledNavLink>
      </NavList>
    </ResponsiveNav>
  );
};

export default Nav;