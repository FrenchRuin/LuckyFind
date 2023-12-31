# LuckyFind [ 개발자 사이드 프로젝트 모집 서비스 ]
**코틀린을 기반으로 한 사이드 프로젝트 모집 서비스** 
<br>
개인프로젝트 | 진행중
<br>


## 시작하게된 계기
> 평소에 직장에 다니면서 제일 힘들었던 점은 프로젝트를 함께 진행할 팀원을 구하는 것입니다. <br>
> 일과 병행하다보면 서로 시간을 맞추기 어렵고, 적당한 프로젝트를 찾기 어렵기 마련입니다. <br>
> 그렇다면 개발에 진심인 사람들을 한곳에 모아 서로 소통하고 도움이 될 수 있는 서비스가 있다면 얼마나 재밌을지 생각해보았습니다. <br>
> 생각만해도 신나더군요 ^^ <br>
> LuckyFind 라는 이름으로 개발을 진행하고있습니다.
<br>


## 개발환경 및 Skill
- Kotlin | JAVA 17
- Spring Boot 3.1.5
- Spring MVC 
- JPA 
- Spring Security 
- Thymeleaf
- H2database 
- BootStrap 
- WebSocket 
- Git

## ORM 구조 
<img src="src/main/resources/images/readme/erd.png"  height="500"/>

- User Entity를 기준으로 마스터성 구조를 가지고있습니다.
- 업데이트 일자 - 2023.11.26

---

### 로그인 및 회원가입 
<img src="src/main/resources/images/readme/login.png"  height="500"/>

- 회원가입시 권한을 선택할 수 있습니다.
- 선택한 권한으로 회원가입후, 로그인할 시 권한에 따라 사용할 수 있는 서비스만 사용가능합니다.
- 현재 권한은 ROLE_ADMIN (관리자) / ROLE_USER (사용자) 가 존재합니다.
---
### 메인페이지
<img src="src/main/resources/images/readme/main.png"  height="500"/>


- 메인페이지로 접근시, LuckyFind의 소개와 각 서비스들과 서비스개발 진행정도를 확인 할 수 있습니다.
- 서비스별 링크 클릭시, 해당 서비스를 사용할 수 있는 페이지로 이동합니다.
---
### 공지사항 
<img src="src/main/resources/images/readme/notice.png"  height="500"/>

- 서비스를 사용하면서 사용자에게 제공할 공지사항들을 관리하는 서비스 입니다.
- 제목과 등록일시를 나타내는 테이블과, 해당 공지사항을 클릭하면 보여지는 Form이 존재합니다.
- 수정, 등록, 삭제의 경우에는 `ROLE_ADMIN` 만 가능하며 `ROLE_USER`는 읽기만 가능합니다.
  - Thymeleaf Security 라이브러리를 사용하여 visible 처리 하였습니다.
  - `hasRole("ROLE_USER")` || `hasRole("ROLE_ADMIN")`

---

### 팀원 모집 서비스
<img src="src/main/resources/images/readme/recruit.png"  height="500"/>

- 현재는 `JAVA`와 `KOTLIN`만 되어있지만 추후 추가할 예정입니다.
- 프로젝트 명, 스킬, 현재 프로젝트 진행상태, 프로젝트 소개글, 프로젝트 모집기간등을 설정하여 등록합니다.
- 프로젝트를 등록하면 `RecruitList`에 보여지게 되며, 하단에는 언어별로 통계를 나타내는 그래프를 보여줍니다.

---

### 채팅 서비스 

---

## 이력
2023.11.26 ReadMe Update



