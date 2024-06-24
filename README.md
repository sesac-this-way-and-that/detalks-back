![image](https://github.com/sesac-this-way-and-that/detalks-back/assets/133750746/211ffa1b-e0a7-4a25-8c88-66fa5a7f3581)# back
이쪽저쪽 백

# Detalks

<div align="center">
<img width="500" alt="image" src="https://github.com/sesac-laters-team/tetrist/assets/133750746/bff47754-e08f-4755-a0f0-15572e10f5ef">
</div>


# Tetrist
> **새싹(Seoul Software Academy, SeSAC) 청년취업사관학교** <br/> **개발기간: 2024.05.31 ~ 2024.06.21** <br/> **팀명: 이쪽저쪽**

<br/>

## 📬 배포 주소

> **배포 버전** : [((http://www.detalks.store/))](http://www.detalks.store/) <br>

<br/>

## 👨‍👩‍👧‍👦 팀 소개

|      김성민       |         이기혁         |       임학민         |      김예지       |      강혜인       |
|---|---|---|---|---|
|      Back-end         |        Front-end         |       Front-end            |     Front-end       |      Back-end          | 
|      배포 & Git 관리, Member API, Image Upload ,Oauth2, Deploy, Swagger |        Main page, Mypage, Header, Footer |      Header, Question Page, Question Detail, Question List
          |     Register, Login, FindPw, Questions |      Q & A API, BookMark & Tag, Pagination, TextEditor, Answers | 
| <a href="https://github.com/jarajiri"> 🔗GitHub</a> | <a href="[https://github.com/1ee-dw](https://github.com/leekihyeok)">🔗GitHub </a> | <a href="[https://github.com/jaeminjeon123](https://github.com/sabb12)">🔗GitHub</a>  | <a href="https://github.com/yzlybe">🔗GitHub</a> | <a href="https://github.com/hyein310">🔗GitHub</a> | 

<br/>

## ✍️ 프로젝트 소개

국내 개발자들을 위해 코드 공유가 가능하고 개발자들과의 실시간 질답이 가능한 Q&A 커뮤니티



---
## 📒 시작 가이드
### .env
DB_USERNAME=로컬DB계정 </br>
DB_PASSWORD=로컬DB비밀번호 </br>
DB_DATABASE=tetris </br>
PORT=8080 </br>
PORT_SOCKET=8081 </br>
PORT_SOCKET_GAME=8082 </br>

### Installation
``` bash
$ git clone https://github.com/sesac-laters-team/tetrist.git
$ cd tetrist
```
#### Backend
```
$ cd server
$ npm install
$ npm run dev
```

#### Frontend
```
$ cd client
$ npm install 
$ npm run start
```

---

## 🤖 사용 스택 Stacks 

### Environment
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)             
---

### Config
![npm](https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white)        
---

### Development
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Socket.io](https://img.shields.io/badge/Socket.io-010101?style=for-the-badge&logo=Socket.io&logoColor=white)
![express](https://img.shields.io/badge/express-000000?style=for-the-badge&logo=express&logoColor=white)
![Sass](https://img.shields.io/badge/Sass-CC6699?style=for-the-badge&logo=Sass&logoColor=white)
![Mysql](https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=Mysql&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-5FA04E?style=for-the-badge&logo=Node.js&logoColor=white)
![EC2](https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![RDS](https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)
![env](https://img.shields.io/badge/dotenv-ECD53F?style=for-the-badge&logo=dotenv&logoColor=white)
![sequelize](https://img.shields.io/badge/sequelize-52B0E7?style=for-the-badge&logo=sequelize&logoColor=white)
![Redux](https://img.shields.io/badge/Redux-764ABC?style=for-the-badge&logo=Redux&logoColor=white)

---

### Communication
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white)

<br/>

---
## 📺 화면 구성 
#### - 메인 페이지  

  
#### - 로그인 페이지



#### - 질문 리스트 페이지



#### - 질문 작성 페이지 


#### - 마이페이지


---
## ⭐ 주요 기능 
### 1. 질문 기능


---
## 📑 아키텍쳐
### Swagger
http://52.78.163.112:8080/api-docs

### 디렉토리 구조
#### - Back-end
```bash
server
│
│
├──admin
│
├── config
│
├── controller
│
├── models
│
├── routes
│
├── sockets
│
├── sql
│
├── utils
└── App.js
```

#### - Front-end
```bash
client
│
│
├──public
│	   └──images
│
│
├── components/
│   ├── auth/                        #인증 관련 컴포넌트
│   │
│   ├── chat/                        #채팅 관련 컴포넌트
│   │
│   ├── common/                  # 공통 컴포넌트
│   │
│   ├── waitingRoom/            # 대기방 컴포넌트
│   │
│   ├── game/                       # 게임방 관련 컴포넌트
│   │
│   ├── page/                        # 페이지 관련 컴포넌트
│   │
│   └── App.js
├── hooks                             #  관련 훅들
├── styles/
│        ├── scss/                    # 페이지 별로 조정
├── redux/                            # Redux 관련 파일
│       └── store.                    # 스토어 설정
│	    │	  ├── module
│        │	  └── index.js
│        └── store.js
│
├── index.js                     # 진입점, 여기에서 React 앱을 DOM에 렌더링

```



