# Domain 계층 모듈 명세서
## 1. 계층 소개
DataBase에 액세스하여 도메인 기준으로 DB 테이블을 정의하고 APP 계층 모듈에서 데이터를 활용 가능하도록 명세하는 역할을 하는 모듈들을 모아놓았습니다. DB 종류에 따라 모듈들이 별도로 구성됩니다. 

## 2. 모듈 구성
- Rdbms
- Redis
- Mongo(추가 예정)

## 3. 모듈 의존성
- Core

## 4. 외부 의존성
- mysql
- Spring-Data-Jpa
- QueryDsl
- mapstruct
- h2
