import styled from 'styled-components';
import Nav from '../navbar/Nav';

const LayoutContainer = styled.div`
  display: flex;
  height: 100vh;
  width: 100%;
`;

const SidebarContainer = styled.div`
  width: 210px;
  border-right: 1px solid #e0e0e0;
`;

const MainContentContainer = styled.div`
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
  h1 {
    font-weight: normal;
    color : #2A5C96;
  }
`;

const MyPageLayout = ({ children }) => {
  return (
    <LayoutContainer>
      <SidebarContainer>
        <Nav />
      </SidebarContainer>
      <MainContentContainer>
        {children}
      </MainContentContainer>
    </LayoutContainer>
  );
};

export default MyPageLayout;