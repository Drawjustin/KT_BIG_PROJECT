// import { useState } from 'react';
// import DocumentAssistantComponent from './DocumentAssistantComponent';
// import Sidebar from './Sidebar';
import styled from 'styled-components';
import styles from '../../filepage/page.module.css'
import SearchPage from './SearchPage';
// import SearchResults from './Test';
// import Docu from './Docu';
// import Sample from './Sample';
// import Chat from './Sample';

const MainContent = styled.div`
  margin-left: ${props => props.sidebarOpen ? '250px' : '0'};
  width: 100%;
  transition: margin-left 0.3s ease-in-out;
  padding: 90px;
`;


const Official = () => {

  return (
    <div className={styles.pageContainer}>
      <div className={styles.titleArea}>
        <h1>공문서 도우미</h1>
          </div>
          <MainContent>
            {/* <Docu/> */}
            {/* <DocumentAssistantComponent /> */}
            {/* <SearchResults/> */}
            <SearchPage/>
            {/* <Sample/> */}
            {/* <Chat/> */}
          </MainContent>
      </div>
  );
};

export default Official;