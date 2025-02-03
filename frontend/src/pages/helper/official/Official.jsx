// import { useState } from 'react';
import DocumentAssistantComponent from './DocumentAssistantComponent';
// import Sidebar from './Sidebar';
import styled from 'styled-components';

const PageContainer = styled.div`
  display: flex;
`;

const MainContent = styled.div`
  margin-left: ${props => props.sidebarOpen ? '250px' : '0'};
  width: 100%;
  transition: margin-left 0.3s ease-in-out;
  padding: 90px;
`;

const Title = styled.h1`
  color: #2A5C96;
  font-size: 24px;
  margin-bottom: 20px;
`;

const Official = () => {
  // const [isSidebarOpen, setIsSidebarOpen] = useState(() => {
  //   const saved = localStorage.getItem('sidebarOpen');
  //   return saved ? JSON.parse(saved) : true;
  // });

  // const toggleSidebar = () => {
  //   setIsSidebarOpen(prev => {
  //     const newState = !prev;
  //     localStorage.setItem('sidebarOpen', JSON.stringify(newState));
  //     return newState;
  //   });
  // };

  return (
    <PageContainer>
      {/* <Sidebar isOpen={isSidebarOpen} toggleSidebar={toggleSidebar} /> */}
      {/* <MainContent sidebarOpen={isSidebarOpen}> */}
      <MainContent>
        <Title>공문서 도우미</Title>
        <DocumentAssistantComponent />
      </MainContent>
    </PageContainer>
  );
};

export default Official;