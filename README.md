# Fitmate 프로젝트 - 서버
운동 메이트가 필요한 운동 종목들에 대하여, 운동 메이트를 찾을 수 있는 소셜 플랫폼 애플리케이션을 위한 백엔드 서버입니다.

지속적으로 업데이트 중입니다.

- **Framework** : Spring Boot 2.7.11
- **Language** : Java 17
- **Database** : Oracle, Redis, MongoDB
- **Deployment** : Jenkins, Docker, Oracle Cloud Infrastructure
- **API Docs**
  - [Mate Service](http://43.202.192.116/mate/swagger-ui)
  - [Chat Service](http://43.202.192.116/chat/swagger-ui)
- **ERD** : [ERD Cloud](https://www.erdcloud.com/d/r9JFJmrqtqDoWyJPJ)

</br>

# 실행 방법
- **requirement**
  - Jdk 17+
  - database install && connect (RDBMS : Oracle recommend, Redis, MongoDB)
  - coolsms join
  - make property or yml files on each modules (total : 7)

- **Running Application**
```
git clone https://github.com/mirea70/fitmate-back.git
chmod +x gradlew

# mate_service
./gradlew :app-mate:clean :app-mate:build -x test
cd modules/app/app-mate/build/libs
java -jar app-mate-0.0.1-SNAPSHOT.jar

# chat_service
./gradlew :app-chat:clean :app-chat:build -x test
cd modules/app/app-chat/build/libs
java -jar app-chat-0.0.1-SNAPSHOT.jar
```

</br>

# 멀티모듈 구성

## 1. 멀티모듈 구성 목적
- 서비스 설계 시, 구현하고자 했던 기능이 크게 채팅 기능과 그 외 기능으로 나뉘었습니다.
- DB 또한 채팅 도메인 데이터는 NoSql, 타 도메인 데이터들은 RDBMS를 사용하고자 하였기에
- 멀티모듈을 활용하여 계층을 보다 명료화하고 의존성을 분리하여 관리하는 것이 유지보수 측면에서 보다 효과적일 것이라 판단하였으며
- 또한 서비스가 추가되면 MSA로 리펙토링도 계획에 있어 이러한 상황에 멀티모듈로 초기에 구성하는 것이 효율적이라 판단했습니다.

## 2. 모듈 계층 구성
계층 별 상세정보가 필요하면 각 계층 디렉토리를 열면 계층 별로 README-{계층}.MD 파일이 존재하니
참고해주시면 감사하겠습니다.

<h3> * 계층 의존 관계도 </h3>

![image](https://github.com/mirea70/fitmate-back/assets/101246806/52fa32e3-6224-4adc-a9b4-9727b0ecc99f)


<h3> * 계층 구성 </h3>

- **App** : 타 계층의 모듈들을 활용하여 응답 데이터를 조합하고, 외부와의 통신 역할을 하는 모듈들을 모아놓은 계층입니다.
- **Domain** : 비즈니스의 핵심 가치를 지닌 도메인을 모아놓은 계층으로, DB에 액세스하며 App 계층에서 활용될 수 있습니다.
- **Core** : 공통 exception, Config 등 다른 모듈에서 공통적으로 사용할 수 있는 유틸이나 설정 클래스들을 모아두었습니다.
- **System** : 시스템에서 기능 구현 시, 마치 라이브러리와 같이 사용될 수 있는 모듈을 모아놓았습니다.

## 3. 프로젝트 공통 외부 의존성
프로젝트 전체적으로 사용될만한 Util 외부 의존성을 공통적으로 설정하였습니다.
- SpringBoot-Starter
- Lombok

</br>

# CI/CD 구성
![image](https://github.com/mirea70/fitmate-back/assets/101246806/d7d15f27-89a0-4d11-95b6-363ecb6b7c47)
- 현재 Oracle Cloud 측 이슈로 Production Server가 정상동작되지 않고 있습니다.
- 이에 따라 현재 Jenkins Server까지만 구축되어있습니다. (Jenkins Server에서 Docker push하지않고 바로 Run)
