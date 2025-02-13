/**
* TermsOfService.jsx
* 서비스 이용약관을 표시하는 페이지 컴포넌트
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
 transition: transform 0.2s ease;

 &:hover {
   transform: translateY(-2px);
   box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
 }

 &:last-child {
   margin-bottom: 0;
 }
`;

// 섹션 제목 스타일링
const SectionTitle = styled.h2`
 font-size: 22px;
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
 
 &:before {
   content: "•";
   color: #2196f3;
   font-weight: bold;
   display: inline-block;
   width: 1em;
   margin-left: -1em;
 }
 
 &:last-child {
   margin-bottom: 0;
 }
`;

const TermsOfService = () => {
  return (
    <Container>
      <MainTitle>이용약관</MainTitle>

      <Section>
        <SectionTitle>제1조 (목적)</SectionTitle>
        <Paragraph>
          이 약관은 국민신문고 서비스(이하 "서비스")를 이용함에 있어 이용자와 서비스 제공자 간의 권리, 의무 및 책임 사항을 규정하는 것을 목적으로 합니다.
        </Paragraph>
      </Section>

      <Section>
        <SectionTitle>제2조 (서비스의 제공)</SectionTitle>
        <Paragraph>
          서비스는 민원인이 공공기관에 민원을 신청하고, 해당 민원에 대한 답변을 받을 수 있도록 지원하는 온라인 플랫폼입니다. 서비스의 주요 기능은 다음과 같습니다:
        </Paragraph>
        <List>
          <ListItem>민원 접수 및 처리 현황 조회</ListItem>
          <ListItem>공공기관에 대한 민원 등록</ListItem>
          <ListItem>민원 처리 결과 확인 및 추가 문의</ListItem>
        </List>
      </Section>

      <Section>
        <SectionTitle>제3조 (이용자의 의무)</SectionTitle>
        <Paragraph>
          이용자는 서비스를 이용하면서 다음과 같은 의무를 다하여야 합니다:
        </Paragraph>
        <List>
          <ListItem>정확한 개인정보 및 민원 내용을 입력해야 합니다.</ListItem>
          <ListItem>타인의 권리를 침해하거나 허위 정보를 기재해서는 안 됩니다.</ListItem>
          <ListItem>민원 처리 과정에서 공공기관과의 원활한 협조를 유지해야 합니다.</ListItem>
          <ListItem>법령 및 서비스 제공자의 정책을 준수해야 합니다.</ListItem>
        </List>
      </Section>

      <Section>
        <SectionTitle>제4조 (서비스 이용의 제한)</SectionTitle>
        <Paragraph>
          다음과 같은 경우 서비스 이용이 제한될 수 있습니다:
        </Paragraph>
        <List>
          <ListItem>허위 또는 악의적인 민원을 반복적으로 등록하는 경우</ListItem>
          <ListItem>타인의 명의를 도용하여 민원을 신청하는 경우</ListItem>
          <ListItem>민원 처리 과정에서 공공기관 담당자에게 폭언 또는 협박을 하는 경우</ListItem>
          <ListItem>기타 정상적인 서비스 운영을 방해하는 행위를 하는 경우</ListItem>
        </List>
      </Section>

      <Section>
        <SectionTitle>제5조 (개인정보의 보호 및 사용)</SectionTitle>
        <Paragraph>
          서비스는 이용자의 개인정보를 보호하며, 민원 접수 및 처리에 필요한 최소한의 정보를 수집합니다. 개인정보 처리에 대한 상세한 내용은 개인정보 처리방침을 참조하십시오.
        </Paragraph>
      </Section>

      <Section>
        <SectionTitle>제6조 (저작권 및 지적 재산권)</SectionTitle>
        <Paragraph>
          서비스에서 제공하는 모든 자료(민원 양식, 공공기관 정보 등)는 관련 법률에 따라 보호됩니다. 이용자는 이를 무단으로 복제, 배포 또는 상업적으로 이용할 수 없습니다.
        </Paragraph>
      </Section>

      <Section>
        <SectionTitle>제7조 (서비스의 변경 및 중지)</SectionTitle>
        <Paragraph>
          서비스 제공자는 필요한 경우 서비스의 내용을 변경하거나 일시적으로 중지할 수 있습니다. 이 경우 이용자에게 사전 공지합니다.
        </Paragraph>
      </Section>

      <Section>
        <SectionTitle>제8조 (책임의 한계)</SectionTitle>
        <Paragraph>
          서비스는 민원의 접수 및 전달을 지원하는 역할을 하며, 개별 민원의 처리 결과에 대한 책임은 해당 공공기관에 있습니다. 이용자는 서비스 이용 과정에서 발생하는 문제에 대해 스스로 책임져야 합니다.
        </Paragraph>
      </Section>

      <Section>
        <SectionTitle>제9조 (약관의 변경)</SectionTitle>
        <Paragraph>
          서비스 제공자는 필요에 따라 본 약관을 변경할 수 있으며, 변경 사항은 서비스 내 공지사항을 통해 사전 안내합니다.
        </Paragraph>
      </Section>

      <Section>
        <SectionTitle>제10조 (관할 법원)</SectionTitle>
        <Paragraph>
          본 약관과 관련하여 분쟁이 발생할 경우, 서비스 제공자의 본사가 위치한 법원을 관할 법원으로 지정하여 해결합니다.
        </Paragraph>
      </Section>

      <Section>
        <SectionTitle>제11조 (이용약관의 동의)</SectionTitle>
        <Paragraph>
          이용자는 본 약관에 동의함으로써 서비스를 이용할 수 있으며, 서비스 이용 시 본 약관에 동의한 것으로 간주됩니다.
        </Paragraph>
      </Section>
    </Container>
  );
};

export default TermsOfService;