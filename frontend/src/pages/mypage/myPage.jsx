import { useSelector } from 'react-redux';
import styled from 'styled-components';
import MyPageLayout from '../../_components/layout/MyPageLayout';
import profileDefaultImage from '../../assets/images/logo.png';


const MyPage = () => {
  const { userEmail, isLoggedIn } = useSelector(state => state.login || {});
  return (
    <MyPageLayout>
      <h1>마이페이지</h1>
      {isLoggedIn ? (
        <WelcomeCard>
          <ProfileSection>
            <ProfileImageContainer>
              <ProfileImage 
                src={profileDefaultImage} // import한 이미지 사용
                alt="프로필 이미지" 
              />
            </ProfileImageContainer>
          </ProfileSection>
          <WelcomeText>환영합니다, <UserEmail>{userEmail}</UserEmail> 님</WelcomeText>
          <UserInfoSection>
            <InfoItem>
              <InfoLabel>이메일</InfoLabel>
              <InfoValue>{userEmail}</InfoValue>
            </InfoItem>
            {/* 추가 정보 항목들 */}
          </UserInfoSection>
        </WelcomeCard>
      ) : (
        <NotLoggedInMessage>로그인이 필요합니다.</NotLoggedInMessage>
      )}
    </MyPageLayout>
  );
};


const ProfileSection = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 2rem;
`;

const ProfileImageContainer = styled.div`
  position: relative;
  margin-bottom: 1rem;
`;

const ProfileImage = styled.img`
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease;

  &:hover {
    transform: scale(1.05);
  }
`;

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
  color: #2A5C96;
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
