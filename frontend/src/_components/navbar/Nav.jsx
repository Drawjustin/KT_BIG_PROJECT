import { NavLink } from "react-router-dom";
import styled from "styled-components";

const NavContainer = styled.nav`
  width: 210px;
  display: flex;
  flex-direction: column;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
`;

const NavHeader = styled.div`
  background-color: #f8f9fa;
  border-bottom: 1px solid #2a5c96;
  padding: 20px;
  text-align: center;
`;

const NavTitle = styled.h2`
  color: #004a8f;
  font-size: 1.5rem;
  margin: 0;
  font-weight: 500;
`;

const NavList = styled.div`
  display: flex;
  flex-direction: column;
`;

const StyledNavLink = styled(NavLink)`
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
  text-decoration: none;
  transition: background-color 0.3s ease;

  &.active {
    background-color: #2a5c96;
    color: #ffffff;

    &:hover {
      background-color: #1f4a7b;
    }
  }

  &:not(.active) {
    background-color: #ffffff;
    border-bottom: 1px solid #dedede;
    color: #666666;

    &:hover {
      background-color: #f0f0f0;
    }
  }
`;

const Nav = () => {
  return (
    <NavContainer>
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
    </NavContainer>
  );
};

export default Nav;