import React, { useEffect } from 'react';
import axios from 'axios';

const About = () => {
  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await axios.get('https://8c21-122-37-19-2.ngrok-free.app/complaints/1', {
          headers: {
            'ngrok-skip-browser-warning': 'true',
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          }
        });
        console.log('응답:', response.data);
      } catch (error) {
        console.error("에러:", error);
      }
    };
    
    fetchPosts();
  }, []);

  return (
    <>
      <h1>국민신문고 이용안내</h1>
      <p>국민 신문고 이용 안내 페이지 입니다.</p>
    </>
  );
};

export default About;


// const About = () => {
//   return (
//     <>
//         <h1>국민신문고 이용안내</h1>
//         <p>국민 신문고 이용 안내 페이지 입니다.</p>
//     </>
//   );
// };

// export default About;