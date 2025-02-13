/**
 * PrivacyPolicy.jsx
 * 개인정보 처리방침을 표시하는 페이지 컴포넌트
 */

import styled from 'styled-components';

// 전체 컨테이너 스타일링
const Container = styled.div`
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  line-height: 1.6;
  color: #333;
`;

// 메인 제목 스타일링
const MainTitle = styled.h1`
  font-size: 32px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 40px;
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #2196f3;
`;

// 섹션 스타일링
const Section = styled.section`
  margin-bottom: 40px;
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

  &:last-child {
    margin-bottom: 0;
  }
`;

// 섹션 제목 스타일링
const SectionTitle = styled.h2`
  font-size: 24px;
  font-weight: 600;
  color: #2196f3;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
`;

// 문단 스타일링
const Paragraph = styled.p`
  margin-bottom: 16px;
  color: #444;
  font-size: 16px;
  line-height: 1.7;

  &:last-child {
    margin-bottom: 0;
  }
`;

// 리스트 컨테이너 스타일링
const List = styled.ul`
  margin: 16px 0;
  padding-left: 20px;
`;

// 리스트 아이템 스타일링
const ListItem = styled.li`
  margin-bottom: 12px;
  color: #444;
  line-height: 1.6;
  
  &:last-child {
    margin-bottom: 0;
  }
`;

// 강조 텍스트 스타일링
const Strong = styled.strong`
  font-weight: 600;
  color: #1a1a1a;
`;
const PrivacyPolicy = () => {
    return (
      <Container>
        <MainTitle>개인정보 처리방침</MainTitle>
   
        <Section>
          <SectionTitle>제1조 (목적)</SectionTitle>
          <Paragraph>
            이 개인정보 처리방침은 공무원 SOS 서비스(이하 "서비스")에서 이용자의 개인정보를 어떻게 수집하고, 이용하며, 보호하는지를 안내하는 것을 목적으로 합니다.
          </Paragraph>
        </Section>
   
        <Section>
          <SectionTitle>제2조 (수집하는 개인정보의 항목 및 수집 방법)</SectionTitle>
          <Paragraph>
            서비스는 이용자가 서비스를 이용하는 과정에서 아래의 개인정보를 수집합니다:
          </Paragraph>
          <List>
            <ListItem>이름</ListItem>
            <ListItem>전화번호</ListItem>
            <ListItem>부서</ListItem>
            <ListItem>아이디</ListItem>
            <ListItem>이메일</ListItem>
            <ListItem>비밀번호</ListItem>
          </List>
          <Paragraph>
            <Strong>수집 방법: </Strong>
            서비스 가입 시 직접 입력 및 서비스 이용 과정에서 자동으로 수집되는 정보
          </Paragraph>
        </Section>
   
        <Section>
          <SectionTitle>제3조 (개인정보의 수집 및 이용 목적)</SectionTitle>
          <Paragraph>
            수집된 개인정보는 다음의 목적을 위해 사용됩니다:
          </Paragraph>
          <List>
            <ListItem>서비스 제공 및 민원 처리: 공무원의 민원 답변 및 공문서 검색 서비스 제공</ListItem>
            <ListItem>회원 관리 및 인증: 이용자 인증 및 계정 관리, 비밀번호 복구</ListItem>
            <ListItem>서비스 개선 및 개발: 서비스 개선 및 이용 통계 분석을 위한 데이터 활용</ListItem>
            <ListItem>법적 의무 이행: 법령에 의한 개인정보 제공 요청을 따르기 위한 목적</ListItem>
          </List>
        </Section>
   
        <Section>
          <SectionTitle>제4조 (개인정보의 보유 및 이용 기간)</SectionTitle>
          <Paragraph>
            수집된 개인정보는 서비스 제공에 필요한 기간 동안 보유되며, 이용자가 서비스 탈퇴를 요청하거나 계정이 비활성 상태일 경우 즉시 삭제됩니다.  
            보유 기간이 끝난 개인정보는 관련 법령에 따라 안전하게 파기됩니다.
          </Paragraph>
        </Section>
   
        <Section>
          <SectionTitle>제5조 (개인정보의 제3자 제공)</SectionTitle>
          <Paragraph>
            서비스는 이용자의 개인정보를 제3자에게 제공하지 않습니다. 다만, 법적 의무에 따라 요구될 경우, 해당 정보를 제공할 수 있습니다.
          </Paragraph>
        </Section>
   
        <Section>
          <SectionTitle>제6조 (개인정보의 처리 위탁)</SectionTitle>
          <Paragraph>
            서비스는 개인정보 처리 업무를 일부 외부 업체에 위탁할 수 있습니다. 이 경우, 위탁받은 업체는 개인정보 보호 관련 법령을 준수해야 하며, 개인정보의 처리와 관련된 사항을 계약서에 명시하여 위탁합니다.
          </Paragraph>
        </Section>
   
        <Section>
          <SectionTitle>제7조 (이용자의 권리와 그 행사 방법)</SectionTitle>
          <Paragraph>
            이용자는 언제든지 자신의 개인정보에 대해 열람, 수정, 삭제를 요청할 수 있습니다. 이를 위해 서비스 고객센터나 설정 메뉴에서 개인정보 관리 기능을 이용하거나, 이메일 또는 전화로 요청하실 수 있습니다.
          </Paragraph>
        </Section>
   
        <Section>
          <SectionTitle>제8조 (개인정보의 안전성 확보 조치)</SectionTitle>
          <Paragraph>
            서비스는 이용자의 개인정보를 보호하기 위해 다음과 같은 기술적, 관리적 조치를 취하고 있습니다:
          </Paragraph>
          <List>
            <ListItem>암호화: 비밀번호는 암호화하여 저장하고, 개인정보 전송 시 SSL 암호화를 적용하여 보호합니다.</ListItem>
            <ListItem>접근 통제: 개인정보에 대한 접근을 허가된 관리자만 가능하도록 제한하며, 정기적인 보안 점검을 실시합니다.</ListItem>
            <ListItem>보안 프로그램: 해킹 및 외부 공격에 대비하여 보안 프로그램을 설치하고 주기적으로 업데이트합니다.</ListItem>
          </List>
        </Section>
   
        <Section>
          <SectionTitle>제9조 (개인정보 처리방침의 변경)</SectionTitle>
          <Paragraph>
            이 개인정보 처리방침은 서비스 제공자의 정책에 따라 변경될 수 있습니다. 변경 사항이 있을 경우, 최소 7일 전 웹사이트에 공지하여 이용자에게 알리겠습니다. 중요한 변경 사항은 이메일 등으로 개별 통지할 수 있습니다.
          </Paragraph>
        </Section>
   
        <Section>
          <SectionTitle>제10조 (문의처)</SectionTitle>
          <Paragraph>
            개인정보와 관련된 문의사항은 아래로 문의해주시기 바랍니다:
          </Paragraph>
          <List>
            <ListItem>사두용미</ListItem>
            <ListItem>에이블스쿨 6기 2조</ListItem>
          </List>
        </Section>
      </Container>
    );
   };
   
   export default PrivacyPolicy;