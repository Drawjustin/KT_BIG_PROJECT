import { useSelector } from 'react-redux';
import styled from 'styled-components';
import MyPageLayout from '../../_components/layout/MyPageLayout';
import profileDefaultImage from '../../assets/images/logo.png';


const MyPage = () => {
  const { userEmail } = useSelector(state => state.login || {});
  return (
    <MyPageLayout>
      <Container>
        {/* 내 정보 확인 제목 */}
        <Title>내 정보 확인</Title>
        <ProfileCard>
          {/* 프로필 이미지 */}
          <ProfileImage src={profileDefaultImage} alt="프로필 이미지" />

          {/* 유저 정보 */}
          <UserInfo>
            <WelcomeText>
              환영합니다, <UserEmail>{userEmail}</UserEmail> 님
            </WelcomeText>
            <Divider />
            <InfoRow>
              <InfoLabel>이메일</InfoLabel>
              <InfoValue>{userEmail}</InfoValue>
            </InfoRow>
          </UserInfo>
        </ProfileCard>
      </Container>
    </MyPageLayout>
  );
};

/* Styled Components */
const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin-top: 50px;  /* 위쪽 여백 추가 */
`;

const Title = styled.h2`
  font-size: 2rem;
  font-weight: bold;
  color: #2A5C96;
  margin-bottom: 20px;
`;

const ProfileCard = styled.div`
  width: 380px;  
  height: 450px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  }
`;

const ProfileImage = styled.img`
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  margin-bottom: 25px;
`;

const UserInfo = styled.div`
  width: 100%;
  text-align: center;
`;

const WelcomeText = styled.p`
  font-size: 1.3rem;
  color: #2A5C96;
  font-weight: bold;
  margin-bottom: 20px;
`;

const UserEmail = styled.span`
  font-weight: bold;
  color: #2A5C96;
`;

const Divider = styled.hr`
  width: 80%;
  border: 0;
  border-top: 1px solid #e0e0e0;
  margin: 20px auto;
`;

const InfoRow = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 12px 0;
  width: 100%;
`;

const InfoLabel = styled.span`
  font-weight: bold;
  font-size: 0.95rem;
  color: #7f8c8d;
  margin-right: 10px;
`;

const InfoValue = styled.span`
  font-size: 1rem;
  color: #2c3e50;
`;



export default MyPage;
