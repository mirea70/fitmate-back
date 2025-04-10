# Fitmate 프로젝트 - 서버
운동 메이트가 필요한 운동 종목들에 대하여, 운동 메이트를 찾을 수 있는 소셜 플랫폼 애플리케이션을 위한 백엔드 서버입니다.

지속적으로 업데이트 중입니다.

- **Framework** : Spring Boot 2.7.11
- **Language** : Java 17
- **Database** : Oracle, Redis, MongoDB
- **Deployment** : Jenkins, Docker, Oracle Cloud Infrastructure, Amajon Web Service
- **API Docs** : http://www.fitmate.site/swagger-ui/index.html
- **ERD** : [ERD Cloud](https://www.erdcloud.com/d/r9JFJmrqtqDoWyJPJ)

- **Websocket 통신 사용방법**
  - **protocol** : STOMP
  - **base url** : https://www.fitmate.site
  - **connection endpoint** : /stomp
  - **subscribe endpoint** : /sub/{roomId}
  - **publish endpoint(enter)** : /pub/{roomId}/enter
  - **publish endpoint(chat)** : /pub/{roomId}/chat
  - **message data format** : Json
    - {"senderNickName": "사용자1", "senderId": 1, "message": "테스트1"}
  - **test** : https://apic.app/online/#/tester

</br>

# 실행 방법
- **requirement**
  - Jdk 17+
  - database install && connect (RDBMS : Oracle recommend, Redis, MongoDB)
  - coolsms join
  - make property or yml file

- **Running Application**
```
git clone https://github.com/mirea70/fitmate-back.git
chmod +x gradlew
./gradlew :adapter:clean :adapter:build -x test
cd modules/adapter/build/libs
java -jar adapter-0.0.1-SNAPSHOT.jar
```

</br>

# 시스템 아키텍처
![image](https://github.com/mirea70/fitmate-back/assets/101246806/1c733b6e-4b44-4e07-b7db-3e5b71bd20c3)

- 가용 서버 하나로 무중단 배포를 위해 Docker container를 사용하였습니다.
- Nginx의 리버스 프록시를 통해 구동중인 컨테이너로 요청이 전달됩니다.
- 캐싱 데이터는 Redis에서 관리되며, 채팅 데이터는 MongoDB에서 관리됩니다.
- 나머지 다른 데이터들은 Oracle DB에서 관리되며, Chat Service에서는 로직 구현을 위해 이 DB에서 조회만 하게 됩니다.

</br>

# 내부 아키텍처
유지보수성 향상을 위해 클린아키텍처를 도입하였습니다. 아래 구성도는 편의에 맞게 재정의한 아키텍처이며, 정의된 각 계층들을 멀티모듈로 구성했습니다.
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

</br>

# CI/CD 구성
![image](https://github.com/mirea70/fitmate-back/assets/101246806/5d244622-022f-4ed7-a275-b928eef680f4)
- 배포 Flow는 위와 같습니다.
- Jenkinse의 빌드 과정이 리소스 소모가 많이들어 별도 서버로 분리하였습니다.
- Docker의 컨테이너와 NginX의 Reverse Proxy를 활용해 Blue/Green 전략으로 무중단 배포로 구축하였습니다.
