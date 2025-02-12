import styled from 'styled-components';

const StyledButton = styled.button`
  position: relative;
  width: 190px;
  height: 65px;
  background:#2a5c96;
  color: white;
  font-family: "Pretendard-Light", sans-serif;
  font-size: 25px;
  border-radius: 10px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease-in-out;
  white-space: nowrap;
  padding: 0 60px;

  &:hover {
    background:#2196f3;
    box-shadow: 0 0 80px rgba(31, 107, 221, 0.5);

    .star-1 { top: -80%; left: -30%; opacity: 1; }
    .star-2 { top: -25%; left: 10%; opacity: 1; }
    .star-3 { top: 55%; left: 25%; opacity: 1; }
    .star-4 { top: 30%; left: 80%; opacity: 1; }
    .star-5 { top: 25%; left: 115%; opacity: 1; }
    .star-6 { top: 5%; left: 60%; opacity: 1; }
  }
`;

const Star = styled.div`
  position: absolute;
  opacity: 0;
  width: 20px;
  height: 20px;
  transition: all 0.3s ease-in-out;

  &.star-1 { top: -50%; left: -30%; }
  &.star-2 { top: 45%; left: 45%; }
  &.star-3 { top: 40%; left: 40%; }
  &.star-4 { top: 20%; left: 40%; }
  &.star-5 { top: 25%; left: 45%; }
  &.star-6 { top: 5%; left: 50%; }

  svg {
    width: 20px;
    height: 20px;
    fill: yellow;
  }
`;

const AiBtn = ({ onClick }) => {
  return (
    <StyledButton onClick={onClick}>
      AI에게 질문하기
      {[1, 2, 3, 4, 5, 6].map((num) => (
        <Star key={`star-${num}`} className={`star-${num}`}>
          <svg viewBox="0 0 784.11 815.53">
            <path d="M392.05 0c-20.9,210.08 -184.06,378.41 -392.05,407.78 207.96,29.37 371.12,197.68 392.05,407.74 20.93,-210.06 184.09,-378.37 392.05,-407.74 -207.98,-29.38 -371.16,-197.69 -392.06,-407.78z" />
          </svg>
        </Star>
      ))}
    </StyledButton>
  );
};

export default AiBtn;

// import styled from 'styled-components';
// const StyledButton = styled.button`
//   position: relative;
//   width: 190px;
//   height: 65px;
//   background: #2A5C96;
//   color: white;
//   font-family: "Pretendard-Light", sans-serif;
//   font-size: 25px;
//   border-radius: 30px;
//   border: none;
//   display: flex;
//   align-items: center;
//   justify-content: center;
//   cursor: pointer;
//   transition: all 0.3s ease-in-out;
//   white-space: nowrap;
//   padding: 0 20px;

  
//   &:hover {
//     background: black;
//     box-shadow: 0 0 25px rgba(128, 215, 223, 0.5);

//     .star-1 {
//       opacity: 1;
//       transform: translate(30px, -30px);
//     }
//     .star-2 {
//       opacity: 1;
//       transform: translate(-30px, -30px);
//     }
//     .star-3 {
//       opacity: 1;
//       transform: translate(40px, 0);
//     }
//     .star-4 {
//       opacity: 1;
//       transform: translate(-40px, 0);
//     }
//     .star-5 {
//       opacity: 1;
//       transform: translate(30px, 30px);
//     }
//     .star-6 {
//       opacity: 1;
//       transform: translate(-30px, 30px);
//     }
//   }
// `;

// const Star = styled.div`
//   position: absolute;
//   opacity: 0;
//   transition: all 1s cubic-bezier(0.05, 0.83, 0.43, 0.96);

//   &.star-1 { top: 20%; left: 20%; }
//   &.star-2 { top: 20%; right: 20%; }
//   &.star-3 { top: 45%; left: 15%; }
//   &.star-4 { top: 45%; right: 15%; }
//   &.star-5 { bottom: 20%; left: 20%; }
//   &.star-6 { bottom: 20%; right: 20%; }

//   svg {
//     width: 15px;
//     height: auto;
//     fill: yellow;
//   }
// `;

// const AiBtn = ({ onClick }) => {
//   return (
//     <StyledButton onClick={onClick}>
//       AI에게 질문하기
//       {[1, 2, 3, 4, 5, 6].map((num) => (
//         <Star key={`star-${num}`} className={`star-${num}`}>
//           <svg viewBox="0 0 784.11 815.53">
//             <path d="M392.05 0c-20.9,210.08 -184.06,378.41 -392.05,407.78 207.96,29.37 371.12,197.68 392.05,407.74 20.93,-210.06 184.09,-378.37 392.05,-407.74 -207.98,-29.38 -371.16,-197.69 -392.06,-407.78z" />
//           </svg>
//         </Star>
//       ))}
//     </StyledButton>
//   );
// };

// export default AiBtn;