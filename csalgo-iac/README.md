# csalgo-iac

CS-ALGO 프로젝트의 클라우드 인프라를 코드로 관리하기 위한 Terraform 구성을 담고 있는 저장소입니다.
현재는 Naver Cloud Platform(NCP)을 기반으로 인프라를 구축하고 있으며, 향후 AWS로의 이전도 고려하여 모듈화 및 추상화된 구조로 설계되어 있습니다.

## 🧱 프로젝트 구조

```text
csalgo-iac/
├── main.tf                 # 루트 모듈 – 실제 서비스 배포 진입점
├── variables.tf            # 루트 모듈 입력 변수 정의
├── outputs.tf              # 루트 모듈 출력 변수 정의
├── backend.tf              # 상태 저장소 설정 (Object Storage 등)
├── terraform.tfvars        # 환경별 실제 변수 값
│
├── modules/                # 역할 기반 모듈 정의 (provider 독립적)
│   ├── redis/
│   ├── mysql/
│   ├── dns/
│   ├── registry/
│   ├── csalgo-server/
│   └── csalgo-web/
│
├── providers/              # 클라우드 플랫폼 별 실제 구현체 분리
│   ├── ncloud/
│   │   ├── redis/
│   │   ├── mysql/
│   │   └── ...
│   └── aws/
│       ├── redis/
│       ├── mysql/
│       └── ...
│
└── envs/ (optional)        # 환경별 구성 (prod/staging 등)
```

## 🚀 배포 흐름

1. `terraform init`: 백엔드 초기화 및 모듈 다운로드
2. `terraform plan`: 변경 사항 미리보기
3. `terraform apply`: 인프라 배포 실행
4. GitHub Pull Request 생성: 변경 사항을 팀원과 공유 및 검토

## 🎯 주요 구성 요소

| 구성 요소                | 설명                     |
|----------------------|------------------------|
| `Container Registry` | 소스 빌드 및 배포를 위한 이미지 저장소 |
| `DNS`                | 서비스 도메인 연결 및 레코드 관리    |
| `csalgo-server`      | Spring Boot 백엔드 애플리케이션 |
| `csalgo-web`         | 프론트엔드 정적 호스팅 또는 컨테이너 앱 |
| `MySQL`              | 사용자 및 문제 데이터 저장용 RDB   |
| `Redis`              | 인증 코드 등 캐시 데이터 저장      |

## 🔄 멀티 클라우드 전략

- 각 modules/* 디렉토리는 기능 단위의 역할 중심 인터페이스만 유지합니다.
- 실제 클라우드 자원 생성 로직은 providers/ncloud, providers/aws에서 구현됩니다.
- 향후 Ncloud → AWS 전환 시, provider 인자만 변경하면 동일한 인터페이스로 배포 가능하도록 설계되었습니다.

## 📚 참고 자료

- [Terraform 공식 문서](https://www.terraform.io/docs/index.html)
- [Naver Cloud Platform Terraform Provider](https://registry.terraform.io/providers/navercloudplatform/ncloud/latest)
- [AWS Terraform Provider](https://registry.terraform.io/providers/hashicorp/aws/latest)
