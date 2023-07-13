# Fitmate 프로젝트 - 서버 <br/> <br/> 멀티모듈 관리방식

## 1. 멀티모듈 구성 목적
- 서비스 설계 시, 구현하고자 했던 기능이 크게 채팅 기능과 그 외 기능으로 나뉘었습니다.
- DB 또한 채팅 도메인 데이터는 NoSql, 타 도메인 데이터들은 RDBMS를 사용하고자 하였기에
- 멀티모듈을 활용하여 계층을 보다 명료화하고 의존성을 분리하여 관리하는 것이
- 유지보수 측면에서 보다 효과적일 것이라 판단하여 적용하게 되었습니다.
- 본래 Presentation, Application, Domain, Infrastructure로 크게 4가지로 계층화하여
- 개발을 진행하려하였지만, DDD 및 TDD 등 학습 목적으로 적용할 다양한 방법론들을 한꺼번에 처음 적용하다보니
- 상당한 러닝커브가 예상되어, 크게 Application / Domain 2 계층으로 구분하여 모듈 구성을 진행했습니다.
- 우선 학습하며 진행하고, 미흡한 부분들은 추후 리펙토링하며 진행하기로 정했습니다.

## 2. 모듈 계층 구성
계층 별 상세정보가 필요하면 각 계층 디렉토리를 열면 계층 별로 README-{계층}.MD 파일이 존재하니
참고해주시면 감사하겠습니다.

<h3> * 계층 의존 관계도 </h3>

![image](https://github.com/mirea70/fitmate-back/assets/101246806/52fa32e3-6224-4adc-a9b4-9727b0ecc99f)


<h3> * 계층 구성 </h3>

- App
- Domain
- Core
- System

## 3. 프로젝트 공통 외부 의존성
프로젝트 전체적으로 사용될만한 Util 외부 의존성을 공통적으로 설정하였습니다.
- SpringBoot-Starter
- Lombok
