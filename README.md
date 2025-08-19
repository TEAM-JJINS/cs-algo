# CS-ALGO: 자동화 기반 CS 면접 준비 도우미

## 📖 프로젝트 소개

CS-ALGO는 **이메일 기반의 자동화된 면접 문제 전송 및 답변 관리 서비스**입니다.

구독을 하면 매일 오전 8시에 CS 면접 문제를 이메일로 받아 답변할 수 있으며,  
시스템은 제출된 답변을 분석해 모범 답안과 비교하고 피드백을 제공합니다.

사용자가 답변을 직접 작성하는 과정에서 **능동적 회상**이 이루어지고,  
피드백을 반복적으로 받으며 학습 효과를 극대화할 수 있습니다.

이를 통해 단순한 문제 제공을 넘어, **체계적인 반복 학습과 개인화된 피드백으로 자신감 있는 면접 준비**를 지원합니다.

---

## 📌 프로젝트 목표

### 🎯 핵심 목표

| 주요 항목          | 설명                             | 기대 효과                |
|----------------|--------------------------------|----------------------|
| **면접 준비 자동화**  | 이메일 기반 문제 출제 및 자동 평가           | 학습 시간 효율화, 일관된 학습 관리 |
| **즉각적인 피드백**   | 제출 답변을 분석하여 실시간 피드백 및 모범 답안 제공 | 학습 효과 향상, 개선점 파악 용이  |
| **데이터 기반 학습**  | 응답 이력 관리 및 패턴 분석               | 취약점 파악, 맞춤형 학습 제공    |
| **지속적인 성장 관리** | 반복 연습을 통한 장기 학습 루틴 구축          | 자신감 있는 면접 준비 경험 제공   |

## 🧩 핵심 기능

<p align="center">
 <img width="800" alt="image" src="https://github.com/user-attachments/assets/0ffdc538-4780-43ee-bc49-febaf7029eba" />
</p>

### 1. 이메일 기반 자동 문제 출제

- 구독자에게 정해진 주기로 CS 문제를 발송
- 발송 내역을 DB에 저장

### 2. 답변 수신 및 분석

- 사용자가 메일로 답변 제출 시 수신 및 파싱
- Lucene 기반 유사도 계산으로 모범 답안과 비교

### 3. 자동 피드백 제공

- 유사도 점수와 개선 문구가 포함된 피드백 메일 발송

### 4. 구독 및 학습 이력 관리

- 구독/해지, 발송 내역, 답변 기록, 피드백 기록 관리
- 향후 오답 노트 및 맞춤형 피드백 확장 예정

## ⚙️ 기술 스택

| 구분         | 사용 기술                                    |
|------------|------------------------------------------|
| Language   | Java 21                                  |
| Framework  | Spring Boot 3.4.5, Spring Mail           |
| Build Tool | Gradle                                   |
| Database   | MySQL 9.3                                |
| Infra      | Ubuntu 24.04.5 LTS, Naver Cloud Platform |
| Container  | Docker, Kubernetes (NKS)                 |
| IaC        | Terraform                                |
| Cache      | Redis                                    |
| CI/CD      | GitHub Actions                           |
| Monitoring | Prometheus                               |
| 협업 도구      | GitHub Issue/PR/Projects, Slack, Figma   |
| 품질 관리      | Checkstyle, SonarQube, Jacoco            |

## 🗄️ 데이터베이스 구조 (ERD)

<p align="center">
<img width="800"  alt="Image" src="https://github.com/user-attachments/assets/ca85deb6-2cb3-4dee-9d3c-183a161dd988" />
<p>

- **User**: 계정, 구독 상태, 생성일
- **Question**: 문제 내용, 난이도, 생성일
- **Question_Sending_History**: 문제 발송 내역
- **Question_Response**: 사용자 답변 기록
- **Response_Feedback**: 답변 분석 및 피드백 결과

## 🚀 실행 방법

### 1. 환경 변수 (.env)

프로젝트 실행 전 `.env` 파일에 아래 항목을 설정해야 합니다.

| 변수명               | 설명                                    |
|-------------------|---------------------------------------|
| `GITHUB_PAT`      | GitHub Personal Access Token (CI/CD용) |
| `DB_HOST`         | 데이터베이스 접속 주소                          |
| `DB_NAME`         | 데이터베이스 스키마명                           |
| `DB_USERNAME`     | DB 사용자 계정명                            |
| `DB_PASSWORD`     | DB 사용자 비밀번호                           |
| `MAIL_USERNAME`   | 이메일 발신 계정 (SMTP 사용자)                  |
| `MAIL_PASSWORD`   | 이메일 발신 계정 비밀번호 (앱 비밀번호)               |
| `SENTRY_DSN`      | Sentry 에러 로깅 및 모니터링용 DSN              |
| `REDIS_HOST`      | Redis 서버 호스트                          |
| `REDIS_PORT`      | Redis 서버 포트                           |
| `JWT_SECRET`      | JWT 서명 키 (토큰 발급 및 검증에 사용)             |
| `TERRAFORM_TOKEN` | Terraform Cloud/CLI 인증 토큰             |

### 2. 로컬 실행

```bash
# 서버 실행
./gradlew :csalgo-server:bootRun

# 프론트엔드 실행
./gradlew :csalgo-web:bootRun
```

## 👥 팀원 소개 및 역할

| 이름       | 역할                               |
|----------|----------------------------------|
| 이진우 (팀장) | 프로젝트 기획, 백엔드 & 프론트엔드 개발, 클라우드 배포 |
| 진서연      | 백엔드 & 프론트엔드 개발                   |

## 📦 모듈 아키텍처

<p align="center">
  <img  width="500" alt="Image" src="https://github.com/user-attachments/assets/3e2e9764-f6a2-4d5d-b7a6-cbb78088a0f9" />
</p>

- **csalgo-iac** : Terraform 기반 인프라 자동화 (IaC)
- **csalgo-web** : 프론트엔드 웹 애플리케이션
- **csalgo-server** : HTTP 요청 처리, Controller 등록
- **csalgo-application** : 서비스 로직 및 유스케이스 구현
- **csalgo-infrastructure** : DB, 이메일, 스케줄러 등 외부 연동
- **csalgo-common** : 공통 유틸, 상수, 전역 예외 처리

## ☁️ 클라우드 아키텍처

<p align="center">
    <img width="800"  alt="Image" src="https://github.com/user-attachments/assets/cbed295b-28bf-4b73-87e6-300a45cf3f9e" />
</p>

- **GitHub Actions** : 코드 변경 시 자동 배포
- **Registry & Object Storage** : 애플리케이션 이미지와 정적 리소스 저장
- **NKS(Kubernetes)** : 워커 노드에서 서비스 Pod 구동, HPA로 자동 확장
- **Load Balancer + Cloudflare** : 트래픽 분산, HTTPS 제공
- **Prometheus** : 애플리케이션 및 인프라 모니터링

## 🔄 CI/CD 파이프라인

<p align="center">
 <img width="800" alt="Image" src="https://github.com/user-attachments/assets/85bc3e66-cf9c-4d51-8945-7425bb397cb5" />
</p>

- **GitHub Actions** : main 브랜치에 PR merge 시 워크플로우 실행
- **Gradle** : 빌드 및 테스트 수행
- **Jacoco & SonarQube** : 테스트 커버리지 측정 및 정적 분석
- **Docker** : 이미지 빌드 후 Container Registry 업로드
- **Terraform** : Kubernetes 클러스터에 자동 배포

---

## 📄 라이선스

This project is licensed under the MIT License – see the [LICENSE](./LICENSE) file for details.
