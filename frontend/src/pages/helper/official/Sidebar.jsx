import PropTypes from 'prop-types';
import styled from 'styled-components';

const SidebarContainer = styled.div`
  height: calc(100vh - 90px);
  width: ${props => props.isOpen ? '250px' : '0'};
  position: fixed;
  top: 90;
  left: 0;
  background-color: #f8f8f8;
  transition: width 0.3s ease-in-out;
  border-right: ${props => props.isOpen ? '1px solid #e0e0e0' : 'none'};
  overflow-x: hidden;
`;

const SidebarContent = styled.div`
  padding: 20px;
  width: 250px;
`;

const ToggleButton = styled.button`
  position: fixed;
  left: ${props => props.isOpen ? '250px' : '0'};
  top: 110px;
  background-color: #2A5C96;
  color: white;
  border: none;
  padding: 10px;
  cursor: pointer;
  transition: left 0.3s ease-in-out;
  border-radius: 0 5px 5px 0;
  z-index: 1000;
`;

const HistoryTitle = styled.h2`
  color: #2A5C96;
  font-size: 20px;
  margin-bottom: 20px;
`;

const HistoryList = styled.ul`
  list-style: none;
  padding: 0;
`;

const HistoryItem = styled.li`
  padding: 10px;
  border-bottom: 1px solid #e0e0e0;
  cursor: pointer;
  &:hover {
    background-color: #f0f0f0;
  }
`;

/**
 * @typedef {Object} SidebarProps
 * @property {boolean} isOpen - 사이드바 열림/닫힘 상태
 * @property {() => void} toggleSidebar - 사이드바 토글 함수
 */

/**
 * 검색 기록을 보여주는 사이드바 컴포넌트
 * @param {SidebarProps} props
 * @returns {JSX.Element}
 */
const Sidebar = ({ isOpen, toggleSidebar }) => {
  return (
    <>
      <SidebarContainer isOpen={isOpen}>
        <SidebarContent>
          <HistoryTitle>검색 기록</HistoryTitle>
          <HistoryList>
            <HistoryItem>최근 검색어 1</HistoryItem>
            <HistoryItem>최근 검색어 2</HistoryItem>
            <HistoryItem>최근 검색어 3</HistoryItem>
          </HistoryList>
        </SidebarContent>
      </SidebarContainer>
      <ToggleButton isOpen={isOpen} onClick={toggleSidebar}>
        {isOpen ? '←' : '→'}
      </ToggleButton>
    </>
  );
};

Sidebar.propTypes = {
  isOpen: PropTypes.bool.isRequired,
  toggleSidebar: PropTypes.func.isRequired
};

export default Sidebar;