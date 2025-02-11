import styled from 'styled-components';
import styles from '../../filepage/page.module.css'
import SearchPage from './SearchPage';

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
            <SearchPage/>
          </MainContent>
      </div>
  );
};

export default Official;