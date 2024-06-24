# Detalks Back

<div>
<img width="200" alt="image" src="https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/2211464e-faf4-429b-ae0f-6bd3d5be5068">
</div>
<br />

> **새싹(Seoul Software Academy, SeSAC) 청년취업사관학교** <br/> **개발기간: 2024.05.31 ~ 2024.06.21** <br/> **팀명: 이쪽저쪽**

<br/>

## 👨‍👩‍👧‍👦 팀 소개

|      김성민       |         이기혁         |       임학민         |      김예지       |      강혜인       |
|---|---|---|---|---|
|![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/9d9d9e4f-007f-418f-b4d5-ccfec40e7fa3) |![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/7a5c61ee-4996-42eb-8c3a-fb90ff5911e1) | ![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/fb75dbc1-480f-4e0d-a9b1-211590fed587) | ![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/d7ba3172-607f-4366-8245-be540e7d60da) | ![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/5479869d-d57a-44bd-b49c-da15911a5195) |
|      Back-end         |        Front-end         |       Front-end            |     Front-end       |      Back-end          | 
|      배포 & Git 관리, Member API, Image Upload ,Oauth2, Deploy, Swagger |        Main page, Mypage, Header, Footer |      Header, Question Page, Question Detail, Question List |     Register, Login, FindPw, Question List |      Q & A API, BookMark & Tag, Pagination, TextEditor, Answer List | 
| <a href="https://github.com/jarajiri"> 🔗GitHub</a> | <a href="https://github.com/leekihyeok">🔗GitHub </a> | <a href="https://github.com/sabb12">🔗GitHub</a>  | <a href="https://github.com/yzlybe">🔗GitHub</a> | <a href="https://github.com/hyein310">🔗GitHub</a> | 

<br />

## ✍️ 프로젝트 소개

국내 개발자들을 위한 코드 공유가 가능하고 개발자들과의 실시간 질답이 가능한 Q & A 커뮤니티
<br />

### 🗓️ 프로젝트 일정
- 프로젝트 기획 및 피그마/ERD 설계 : 24.05.31 - 24.06.03
- 개발 : 24.06.04 - 24.06.13 
- 배포 및 문제 수정 : 24.06.14 - 24.06.19
- 발표 준비 및 테스트 : 24.06.20 - 24.06.21

<br />

## 📬 배포 주소

> **배포 버전** : [http://www.detalks.store/](http://www.detalks.store/)

<br />

## 📜 API 명세
> **Swagger** : [http://www.detalks.store:8080/api-docs](http://www.detalks.store/api-docs) 

<br/>

## 🧾 ERD
![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/ca27417e-7329-4114-960e-8d4ad4a46b8c)

<br/>

## ⚙️ 아키텍쳐
![아키텍쳐](https://github.com/sesac-this-way-and-that/detalks-back/assets/140472588/858d2cdc-5b58-45b8-983d-e61fb1b01a69)


<br/>


## ⭐ 주요 기능 
### 1. 회원
- 로그인 방식: 스프링 시큐리티와 JWT를 사용한 토큰 기반 인증 / 인가 사용
- 비밀번호 암호화: BcryptPasswordEncoder 클래스 사용
### 2. 이메일 인증
- 이메일 인증: JavaMailSender 인터페이스와 구글 SMTP 서버 사용
- 보안 개선: Redis를 활용한 인증코드 검증 로직 추가
### 3. 소셜 로그인
- 구현 방식: OAuth 2.0 프로토콜의 Authorization Code Grant 방식
- 일반 로그인과 통합 관리: 쿠키를 통한 토큰 발급 후 헤더로 토큰 전달
### 4. 파일 업로드
- 프로필 이미지 업로드: UUID로 파일 이름 중복 방지
- 업로드 경로 개선: 외부 경로로 변경하여 이미지 지연 로딩 문제 해결
### 5. 회원 탈퇴
- 논리 삭제 방식: 탈퇴 여부 컬럼 추가, 일정 기간 후 물리적 삭제
### 6. 검색
- 검색 기능 구현: JPA Specification 사용
- 동적 쿼리: 코드 가독성과 유지보수성 향상
### 7. 페이지 네이션
- 페이지네이션: JPA의 Pageable 기능 사용
- PageRequest 객체: 정렬 정보와 함께 페이지네이션 구현
### 8. Hash Tag
- 중간 테이블 사용: 1:N 관계 테이블로 구현
- 검색 및 정보 관리: 정규화를 통해 중복 최소화, 해시태그를 이용한 검색이나 추가 정보 필요시 확장 가능

<br />

## 📒 시작 가이드
<details>
<summary>Front-end</summary>

### 1. git clone
```
$ git clone https://github.com/sesac-this-way-and-that/detalks-front.git
```
### 2. npm
```
$ cd detalks-front
$ npm i
```

### 3. 환경변수 설정(.env.development)
```
REACT_APP_API_SERVER=http://localhost:8080/api
REACT_APP_STATIC_SERVER=http://localhost:8080/upload
REACT_APP_GOOGLE_OAUTH_API_SERVER=http://localhost:8080/oauth2/authorization/google
REACT_APP_GOOGLE_OAUTH_REDIRECT=http://localhost:8080/api/member/auth/header
REACT_APP_MODE=development
```
### 4. 실행
```
npm start
```
</details>

<details>
<summary>Back-End</summary>

### 1. git clone
```
$ https://github.com/sesac-this-way-and-that/detalks-back.git
```
### 2. 환경변수 설정(application.properties)
```
# DB
spring.datasource.url=jdbc:mysql://localhost:3306/{db_name}?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
spring.datasource.username={db_username}
spring.datasource.password={db_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=de129a885ee97baee6bf174c9f5f59865a5f215c62ab5621848c0be3be2ab4e9

# MULTIPART
file.upload.path=/upload/**
file.resource.path.win=file:///C:\\\\upload\\\\
file.resource.path.mac=file:///Users/{username}/upload/
file.resource.path.nix=file:///home/ubuntu/detalks/server/upload/

spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=2MB
spring.servlet.multipart.max-request-size=2MB

# MAIL
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username={google_app_email}
spring.mail.password={google_app_password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.properties.auth-code-expiration-millis =1800000

# REDIS
# spring.data.redis.host=localhost
# spring.data.redis.port=6379
# spring.data.redis.password=4014

# PROPERTIES
mail.sender.email={sender_email}
redirect.server.header-uri=http://localhost:3000/oauth2/google/redirect/header

# OAUTH
spring.security.oauth2.client.registration.google.client-name=google
spring.security.oauth2.client.registration.google.client-id={oauth_client_id}
spring.security.oauth2.client.registration.google.client-secret={oauth_client_secret}
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
spring.security.oauth2.client.registration.google.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.google.scope=profile,email

# SWAGGER
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/api-docs
```
### 3. 빌드 후 실행
```
$ ./gradlew build
$ java -jar detalks-0.0.1-SNAPSHOT.jar
```
</details>

<br />

## 🤖 사용 기술

### Environment
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)             
### Frameworks, Platforms and Libraries
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/spring%20security-%236DB33F.svg?style=for-the-badge&logo=spring%20security&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Redux](https://img.shields.io/badge/Redux-764ABC?style=for-the-badge&logo=Redux&logoColor=white)
![env](https://img.shields.io/badge/dotenv-ECD53F?style=for-the-badge&logo=dotenv&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Sass](https://img.shields.io/badge/Sass-CC6699?style=for-the-badge&logo=Sass&logoColor=white)
### Language
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=Typescript&logoColor=white)
### Hosting
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![EC2](https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![RDS](https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)
### Design
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)
![Adobe Photoshop](https://img.shields.io/badge/adobe%20photoshop-%2331A8FF.svg?style=for-the-badge&logo=adobe%20photoshop&logoColor=white)
### Database
![Mysql](https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=Mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
### Communication
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white)
<br/>

<br />

## 📺 화면 구성 
#### 메인 페이지  
![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/ee3961ac-c6ce-4c84-b783-3f18aaaf13c1)
<br />
  
#### 로그인 & 회원가입 페이지
![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/d99583ec-3061-4c0e-af52-5b26914ae44d)
<br />


#### 질문 리스트 페이지
![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/d6661039-2fc6-4b09-8862-9892fb2d0bdb)
<br />


#### 질문 작성 페이지 
![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/afcfca7b-3605-4506-8720-d335d8b7a2c1)
<br />

#### 마이페이지
![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/c24d5da1-efbf-4e18-9ebe-532744d1ccb3)
<br />

## 📁 디렉토리 구조
```bash
com.twat.detalks
│
│
├── answer  # 답변 도메인
│     ├── controller
│     ├── dto
│     ├── entity
│     ├── repository
│     └── service
├── config  # 설정 파일
│
├── email   # 이메일 도메인
│     ├── controller
│     ├── dto
│     ├── entity
│     ├── repository
│     ├── util
│     └── service
├── member  # 회원 도메인
│     ├── controller
│     ├── dto
│     ├── entity
│     ├── repository
│     ├── exception
│     ├── utils
│     ├── vo
│     ├── validation
│     └── service
├── mypage  # 마이페이지
│     ├── controller
│     ├── dto
│     └── service
├── oauth2  # google
│     ├── dto
│     ├── jwt
│     └── service
├── question  # 질문 도메인
│     ├── controller
│     ├── dto
│     ├── entity
│     ├── repository
│     └── service
├── tag  # 태그 도메인
│     ├── dto
│     ├── entity
│     ├── repository
│     └── service
│
└── DetalksApplication
```
