import { useState } from 'react';
import styles from './ChatInterface.module.css';

const ChatInterface = () => {
  const [input, setInput] = useState('');  // 검색창 내용
  const [messages, setMessages] = useState([]);  // 메시지 상태
  const [file, setFile] = useState(null);  // 파일 상태

  // 검색 버튼 클릭 시 실행되는 함수
  const handleSubmit = async () => {
    if (!input.trim() && !file) return;  // 입력 값이나 파일이 없으면 실행하지 않음

    // 메시지 초기화 (새로운 검색이 시작되면 이전 메시지 삭제)
    setMessages([]);


    // FormData로 데이터를 전송할 준비
    const formData = new FormData();
    formData.append('prompt', input);  // 검색창의 입력 값 추가
    if (file) formData.append('file', file);  // 파일이 있으면 추가

    // try {
    //   // API로 데이터 전송
    //   const response = await fetch('YOUR_API_ENDPOINT', {
    //     method: 'POST',
    //     body: formData,
    //   });

    //   const data = await response.json();
      
    //   // 응답 메시지만 추가 (사용자 메시지는 추가하지 않음)
    //   setMessages([{ text: data.response, isUser: false }]);
    // } catch (error) {
    //   console.error('API Error:', error);
    // }
       setMessages([{ text: "hello", isUser: false }]);

    // 입력 창 초기화 및 파일 상태 초기화
    setInput('');
    setFile(null);
  };

  return (
    <div className={styles.container}>
      {/* 검색창과 버튼 */}
      <div className={styles.inputArea}>
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          className={styles.input}
          placeholder="메시지를 입력하세요..."
        />
        
        {/* 전송 버튼 */}
        <button onClick={handleSubmit} className={`${styles.button} ${styles.sendButton}`}>
          검색
        </button>

        {/* 파일 업로드 */}
        <div className={styles.fileUploadContainer}>
          <input
            type="file"
            className="hidden"
            id="file-upload"
            onChange={(e) => setFile(e.target.files[0])}
          />
          <label htmlFor="file-upload" className={`${styles.button} ${styles.fileButton}`}>
            파일 업로드
          </label>
        </div>
      </div>

      {/* 메시지 출력 */}
      <div className={styles.messages}>
        {messages.map((msg, idx) => (
          <div key={idx} className={`${styles.message} ${msg.isUser ? styles.userMessage : styles.aiMessage}`}>
            {msg.text}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ChatInterface;