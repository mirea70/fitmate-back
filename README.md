# FitMate - 백엔드 서버

운동 메이트를 찾을 수 있는 소셜 플랫폼의 백엔드 서버입니다.

백엔드부터 모바일 앱, 스토어 배포까지 1인 개발로 진행하였습니다.

> **모바일 앱 저장소** : [fitmate-app](https://github.com/mirea70/fitmate-app)

<br/>

## 기술 스택

| 구분 | 기술 |
|------|------|
| **Framework** | Spring Boot 2.7.11 |
| **Language** | Java 17 |
| **Database** | Oracle, Redis, MongoDB |
| **인증** | JWT, Kakao OAuth, Firebase Phone Auth |
| **실시간 통신** | STOMP (WebSocket + SockJS) |
| **배포** | Docker, Jenkins, Nginx |
| **클라우드** | AWS, OCI, Cloudflare |
| **API 문서** | [Swagger UI](https://www.fitmate.store/swagger-ui/index.html) |
| **ERD** | [ERD Cloud](https://www.erdcloud.com/d/r9JFJmrqtqDoWyJPJ) |

<br/>

## 주요 기능

| 기능 | 설명 |
|------|------|
| **메이트 모집** | 운동 종목별 메이트 모집 CRUD (선착순/승인제, 참가비, 연령/성별 제한) |
| **참여 신청/승인** | 신청, 승인, 거절, 취소 워크플로우 + 자동 취소 처리 |
| **검색/필터** | QueryDSL 기반 동적 조건 검색 (종목, 지역, 날짜, 인원, 연령 등) |
| **실시간 채팅** | STOMP + MongoDB 기반 그룹/1:1 채팅, 읽음 상태 관리 |
| **인증/인가** | JWT + Kakao OAuth + Firebase Phone Auth |
| **알림** | Redis 기반 실시간 알림 (승인/마감/신청/리마인더 등) |
| **프로필** | 회원 관리, 팔로우/팔로워, 찜 목록 |
| **파일 관리** | 이미지 업로드/다운로드, 파일 크기/확장자 검증 |

<br/>

## WebSocket 통신

| 항목 | 값 |
|------|------|
| **Protocol** | STOMP over SockJS |
| **Base URL** | https://www.fitmate.site |
| **Connection** | `/stomp` |
| **Subscribe** | `/sub/{roomId}` |
| **Publish (입장)** | `/pub/{roomId}/enter` |
| **Publish (채팅)** | `/pub/{roomId}/chat` |
| **Message Format** | `{"senderNickName": "사용자1", "senderId": 1, "message": "테스트"}` |

<br/>

## 실행 방법

### 사전 준비
- JDK 17+
- Database : Oracle, Redis, MongoDB
- Firebase 프로젝트 (Phone Auth)
- Kakao Developers 앱 등록
- Naver Developers 앱 등록 (장소 검색 API)

### 실행
```bash
git clone https://github.com/mirea70/fitmate-back.git
cd fitmate-back
chmod +x gradlew
./gradlew :adapter:clean :adapter:build -x test
cd modules/adapter/build/libs
java -jar adapter-0.0.1-SNAPSHOT.jar
```

<br/>

## 시스템 아키텍처

![image](https://github.com/mirea70/fitmate-back/assets/101246806/1c733b6e-4b44-4e07-b7db-3e5b71bd20c3)

- 가용 서버 하나로 무중단 배포를 위해 Docker Container를 사용하였습니다.
- Cloudflare를 통해 DNS/SSL을 관리하고, Nginx 리버스 프록시를 통해 구동 중인 컨테이너로 요청이 전달됩니다.
- Nginx에서 `/stomp` 경로에 대해 WebSocket Upgrade 헤더를 전달하도록 별도 설정하였습니다.
- 캐싱 데이터 및 알림은 Redis에서 관리되며, 채팅 데이터는 MongoDB에서 관리됩니다.
- 나머지 데이터는 Oracle DB에서 관리됩니다.

<br/>

## 내부 아키텍처

유지보수성 향상을 위해 클린 아키텍처를 도입하였습니다. 아래 구성도는 편의에 맞게 재정의한 아키텍처이며, 정의된 각 계층들을 멀티모듈로 구성했습니다.

## 계층 구성도
![image](https://github.com/mirea70/fitmate-back/assets/101246806/ee296b21-51f9-437e-987e-b4345e6aa4a2)

## 계층 상세 설명
### Domain
- 순수한 도메인 객체들을 가지고 있으며, 외부 프레임워크나 라이브러리를 일체 의존하지 않습니다.
- 도메인 객체에는 비즈니스의 핵심 가치를 지닌 도메인 규칙 및 로직이 존재합니다.

### UseCase
- 도메인 계층에 의존하여 애플리케이션에 특화된 규칙들을 정의하고, 도메인들을 가져와서 활용해 이를 적용하고 실행합니다.
- 반환 데이터가 필요하면 Adapter 계층으로 이를 내보냅니다.

### Adapter
- 웹, 영속성 관련 프레임워크나 라이브러리 등이 위치한 계층입니다.
- 크게 외부의 요청을 받는 InAdapter와
- 내부 처리후 밖으로 반환하거나 영속화를 통해 외부 DB에 결과를 반영하는 역할을 하는 OutAdapter가 존재합니다.

### Port
- Adapter → UseCase 혹은 UseCase → Adapter으로 데이터를 중계하는 역할을 합니다.
- DIP를 활용해 Adapter와 UseCase 계층 사이의 결합도를 더욱 느슨하게 만들어줄 수 있습니다.
- 이러한 느슨한 결합을 통해 보다 명확한 계층 간 독립성을 확보하고 유지보수성 증대를 기대할 수 있습니다.
- 외부 라이브러리 의존성이 존재하지 않습니다.

<br/>

## CI/CD 구성

![image](https://github.com/mirea70/fitmate-back/assets/101246806/5d244622-022f-4ed7-a275-b928eef680f4)

- 배포 Flow는 위와 같습니다.
- Jenkins의 빌드 과정이 리소스 소모가 크기 때문에 별도 서버로 분리하였습니다.
- Docker 컨테이너와 Nginx 리버스 프록시를 활용해 **Blue/Green 전략**으로 무중단 배포를 구축하였습니다.
