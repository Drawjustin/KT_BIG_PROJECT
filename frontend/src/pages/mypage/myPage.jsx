import { useSelector } from 'react-redux';
import styled from 'styled-components';
import MyPageLayout from '../../_components/layout/MyPageLayout';

const MyPage = () => {
  // const { userEmail, isLoggedIn } = useSelector(state => state.loginSlice);
  const { userEmail, isLoggedIn } = useSelector(state => state.login || {});
  return (
    <MyPageLayout>
      <h1>마이페이지</h1>
      {isLoggedIn ? (
        <WelcomeCard>
          <WelcomeText>환영합니다, <UserEmail>{userEmail}</UserEmail> 님</WelcomeText>
          <UserInfoSection>
            <InfoItem>
              <InfoLabel>이메일</InfoLabel>
              <InfoValue>{userEmail}</InfoValue>
            </InfoItem>
            {/* 추가 정보 항목들을 여기에 넣을 수 있습니다 */}
          </UserInfoSection>
        </WelcomeCard>
      ) : (
        <NotLoggedInMessage>로그인이 필요합니다.</NotLoggedInMessage>
      )}
    </MyPageLayout>
  );
};


const WelcomeCard = styled.div`
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  }
`;

const WelcomeText = styled.p`
  font-size: 1.5rem;
  color: #555;
  margin-bottom: 1.5rem;
`;

const UserEmail = styled.span`
  font-weight: bold;
  color: #3498db;
`;

const UserInfoSection = styled.div`
  display: grid;
  gap: 1rem;
`;

const InfoItem = styled.div`
  display: flex;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
`;

const InfoLabel = styled.span`
  font-weight: 600;
  color: #7f8c8d;
  width: 100px;
`;

const InfoValue = styled.span`
  color: #2c3e50;
  flex: 1;
`;

const NotLoggedInMessage = styled.p`
  font-size: 1.2rem;
  color: #e74c3c;
  text-align: center;
`;

export default MyPage;