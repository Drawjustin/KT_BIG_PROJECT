// import { useState } from 'react';
import DocumentAssistantComponent from './DocumentAssistantComponent';
// import Sidebar from './Sidebar';
import styled from 'styled-components';
import styles from '../../filepage/page.module.css'

const MainContent = styled.div`
  margin-left: ${props => props.sidebarOpen ? '250px' : '0'};
  width: 100%;
  transition: margin-left 0.3s ease-in-out;
  padding: 90px;
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
    <div className={styles.pageContainer}>
      <div className={styles.titleArea}>
        <h1>공문서 도우미</h1>
          </div>
          <MainContent>
            <DocumentAssistantComponent />
          </MainContent>
      </div>
  );
};

export default Official;