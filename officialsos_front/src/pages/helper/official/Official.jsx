// Official.jsx
import styled from 'styled-components';
import styles from '../../filepage/page.module.css';
import SearchPage from './SearchPage';

const MainContent = styled.div`
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px;
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