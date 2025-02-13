# KT_BIG_PROJECT
## ⭐ 에이블스쿨 6기 2조 사두용미 팀 ⭐

# 🚀 공무원 SOS: AI 기반 행정 혁신 프로젝트

> 공무원의 업무 효율성을 높이고, 민원 처리와 공문서 검색을 자동화하는 AI 기반 행정 혁신 프로젝트입니다.  
> 반복적인 민원 응대, 악성 민원 처리, 비효율적인 공문서 업무를 혁신하여 공무원의 업무 부담을 줄이고, 국민에게 보다 빠른 서비스를 제공합니다.

---

## 📌 프로젝트 개요

### 🎯 프로젝트 목표
✅ AI 기반 민원 자동 분류 및 부서 배정  
✅ 악성 민원 탐지 및 반복 민원 판단  
✅ 민원 요약 및 답변 생성  
✅ 공문서 검색 및 요약 자동화  
✅ 공무원의 업무 부담 감소 및 업무 효율성 향상  

---

## 🏛️ 필요성 및 배경  

📌 **공무원의 업무 피로도 증가**  
📌 **반복적인 민원 응대로 인한 스트레스**  
📌 **비효율적인 문서 업무로 행정 처리 지연**  
📌 **AI 기술을 활용한 업무 자동화 필요성 대두**  

**→ AI 기반 자동화 솔루션을 통해 공무원의 업무를 혁신하고 국민 서비스 품질을 향상시킵니다.**

---

## 🛠️ 주요 기능 및 AI 모델

| 기능 | AI 모델 | 역할 |
|------|--------|------|
| 📌 **민원 자동 분류** | BERT 기반 분류 모델 | 접수된 민원을 부서 및 팀 단위로 자동 배정 |
| 🛑 **악성 민원 탐지** | BERT Fine-tuning | 욕설, 공격적 표현 포함 여부 판단 |
| 🔄 **반복 민원 판단** | 코사인 유사도 | 동일한 민원이 반복적으로 접수되는지 판단 |
| 📝 **민원 요약** | OpenAI GPT | 긴 민원 내용을 요약하여 효율적 처리 가능 |
| 📩 **민원 답변 추천 및 생성** | RAG 기반 GPT + ChromaDB | 기존 민원 답변 DB 기반으로 적절한 답변 추천 |
| 📑 **공문서 검색** | LangChain RAG + ChromaDB | 공문서 검색 및 요약 제공 |

---

## 🏗️ 시스템 아키텍처

> **[📌 시스템 아키텍처 다이어그램]**  
> *![Image](https://github.com/user-attachments/assets/a704d224-f846-401f-8b38-734dc390fa3a))


🖥 **Frontend** (React, Vue) → 사용자 인터페이스 제공  
📡 **Backend** (FastAPI, Spring Boot) → API Gateway 및 비즈니스 로직 처리  
🤖 **AI 모델 서버** (Docker, Kubernetes) → NLP 기반 민원 처리 및 문서 검색  
💾 **Database & Storage** (RDS, ChromaDB, S3) → 민원 데이터 및 문서 저장  
☁️ **Cloud Infra** (AWS) → 전체 서비스 배포 및 운영  

---

## 🧠 AI 모델 아키텍처

> **[📌 AI 모델 아키텍처 다이어그램]**  
> *<img width="1266" alt="Image" src="https://github.com/user-attachments/assets/30a03972-adeb-4ad8-9845-9982d4b89ea7" />*  

### 🎯 **LLM 기반 AI 모델 구성**
- **BERT**: 민원 자동 분류 및 악성 민원 탐지
- **OpenAI GPT**: 민원 요약 및 자동 응답 생성
- **LangChain RAG**: 공문서 검색 및 요약  
- **ChromaDB**: 문서 검색 최적화  

💡 **모델 파이프라인 구성**
1. **민원 데이터 입력** → 자연어 처리 (BERT) → 부서 자동 배정  
2. **악성 민원 필터링** → 감정 분석 (Fine-tuned BERT)  
3. **RAG 기반 검색** → OpenAI GPT → 민원 답변 자동 생성  
4. **공문서 검색 & 요약** → LangChain 기반 질의응답 시스템  

---

## 🔄 서비스 흐름도

> **[📌 전체 서비스 흐름도]**  
> *<img width="1224" alt="Image" src="https://github.com/user-attachments/assets/e066ecfb-f1e1-4eb2-be28-552888f46f5f" />*  

🔹 **사용자 (공무원/국민)** → 민원 제출  
🔹 **민원 자동 분류** → 적절한 부서 배정  
🔹 **악성 민원 필터링** → 공무원 스트레스 감소  
🔹 **자동 응답 생성** → 공무원의 응대 부담 감소  
🔹 **공문서 검색 & 요약** → 신속한 문서 검색 지원  

---

## 🔹 적용 기술  

### 🧠 **AI & NLP 모델**
- **BERT**: 악성 민원 탐지, 자동 분류
- **OpenAI GPT**: 민원 요약 및 자동 응답 생성
- **LangChain RAG**: 공문서 검색 및 요약
- **ChromaDB**: 데이터 검색 및 저장  

### 🏗 **클라우드 인프라 & 운영**
- **AWS** (S3, RDS, Elastic Load Balancer)
- **Docker, Kubernetes** (컨테이너 기반 운영)
- **FastAPI, Spring Boot, Query DSL** (백엔드)
- **React, Vue, JWT 인증** (프론트엔드)  

---

## 🎯 목표 고객 (B2G)

🏛 **중앙정부 및 지방자치단체 공무원**  
💼 **국민 민원 응대 업무를 담당하는 기관**  

---

## 🔍 활용 데이터  

📄 **공공데이터포탈 공문서 데이터**  
📄 **행정안전부 보도자료 데이터**  
📄 **국민신문고 민원답변사례 데이터**  
📄 **혐오표현 데이터**  
📄 **창원시 민원 데이터 및 조직도**  

---

## 🚀 기대 효과  

✔ **📈 업무 효율성 향상**: 반복적인 민원 응대 부담 감소  
✔ **🏛 스마트 행정 실현**: AI 기반 자동 분류 및 검색으로 행정 서비스 질 향상  
✔ **⚡ 자동화 강화**: 공무원의 수작업을 줄이고, 신속한 민원 대응 가능  
✔ **😊 공무원 만족도 향상**: 악성 민원 감지 및 자동화 시스템 도입으로 스트레스 감소  
✔ **👥 국민 서비스 개선**: 정확한 문서 검색 및 빠른 응답으로 국민 만족도 증대  

---

📌 **이 프로젝트는 AI 기술을 활용한 스마트 행정을 실현하여 공무원과 국민 모두에게 편리한 서비스를 제공하는 것을 목표로 합니다!**  



