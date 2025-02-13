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
         이 약관은 공무원 SOS 서비스(이하 "서비스")를 이용함에 있어 이용자와 서비스 제공자 간의 권리, 의무 및 책임 사항을 규정하는 것을 목적으로 합니다.
       </Paragraph>
     </Section>

     <Section>
       <SectionTitle>제2조 (서비스의 제공)</SectionTitle>
       <Paragraph>
         서비스는 공무원들이 민원 답변을 돕고 공문서를 검색할 수 있는 온라인 플랫폼을 제공합니다. 서비스의 주요 기능은 다음과 같습니다:
       </Paragraph>
       <List>
         <ListItem>민원 처리 지원</ListItem>
         <ListItem>공문서 검색 서비스</ListItem>
       </List>
     </Section>

     <Section>
       <SectionTitle>제3조 (이용자의 의무)</SectionTitle>
       <Paragraph>
         이용자는 서비스를 이용하면서 다음과 같은 의무를 다하여야 합니다:
       </Paragraph>
       <List>
         <ListItem>서비스 이용 시 타인의 권리를 침해하지 않도록 해야 합니다.</ListItem>
         <ListItem>서비스 제공자가 요구하는 절차를 준수해야 합니다.</ListItem>
         <ListItem>허위 정보를 제공하거나 악의적인 목적으로 서비스를 이용해서는 안 됩니다.</ListItem>
       </List>
     </Section>

     <Section>
       <SectionTitle>제4조 (서비스 이용의 제한)</SectionTitle>
       <Paragraph>
         서비스 제공자는 이용자가 본 약관을 위반하거나 불법적인 행동을 할 경우 서비스 이용을 제한할 수 있습니다.
       </Paragraph>
     </Section>

     <Section>
       <SectionTitle>제5조 (개인정보의 보호 및 사용)</SectionTitle>
       <Paragraph>
         서비스는 이용자의 개인정보를 보호하고, 서비스 제공을 위해 필요한 최소한의 정보를 수집합니다. 개인정보 처리에 대한 상세 사항은 개인정보 처리방침을 참조하십시오.
       </Paragraph>
     </Section>

     <Section>
       <SectionTitle>제6조 (저작권 및 지적 재산권)</SectionTitle>
       <Paragraph>
         서비스에서 제공하는 모든 콘텐츠와 자료는 저작권 및 지적 재산권에 의해 보호됩니다. 이용자는 이를 무단으로 복제하거나 배포할 수 없습니다.
       </Paragraph>
     </Section>

     <Section>
       <SectionTitle>제7조 (서비스의 변경 및 중지)</SectionTitle>
       <Paragraph>
         서비스 제공자는 서비스의 운영을 변경하거나 일시적으로 중지할 수 있습니다. 이 경우, 이용자에게 사전 고지합니다.
       </Paragraph>
     </Section>

     <Section>
       <SectionTitle>제8조 (책임의 한계)</SectionTitle>
       <Paragraph>
         서비스 제공자는 서비스 이용 중 발생할 수 있는 모든 문제에 대해 책임을 지지 않으며, 이용자는 서비스 이용에 따른 모든 책임을 부담합니다.
       </Paragraph>
     </Section>

     <Section>
       <SectionTitle>제9조 (약관의 변경)</SectionTitle>
       <Paragraph>
         서비스 제공자는 필요에 따라 본 약관을 변경할 수 있으며, 변경 사항은 서비스 내 공지사항을 통해 이용자에게 알리겠습니다.
       </Paragraph>
     </Section>

     <Section>
       <SectionTitle>제10조 (관할 법원)</SectionTitle>
       <Paragraph>
         본 약관에 관한 분쟁이 발생할 경우, 서비스 제공자의 본사가 위치한 법원을 제1심 법원으로 하여 해결합니다.
       </Paragraph>
     </Section>

     <Section>
       <SectionTitle>제11조 (이용약관의 동의)</SectionTitle>
       <Paragraph>
         이용자는 본 약관에 동의함으로써 서비스를 이용할 수 있습니다. 서비스 이용 시 본 약관에 동의한 것으로 간주됩니다.
       </Paragraph>
     </Section>
   </Container>
 );
};

export default TermsOfService;