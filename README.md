# MBTI-CHAT
![image](https://user-images.githubusercontent.com/111469930/235820575-9e9d843b-9683-4986-a28b-da608543d2eb.png)

---

## Description
프로젝트 정보, 기간, 인원, 소개 등


### 기술스택 
Environment
<div>
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>

Development
<div>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
</div>

Infra
<div>
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
  <img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
  <img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
</div>

Communication
<div>
  <img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
  <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
</div>

---

## 주요기능
>Release
#### 원하는 MBTI가 서로 일치하는 유저 간 1대1 매칭 기능
- 원하는 MBTI를 선택 시 자바의 ConcurrentHashMap을 사용해 waiting 기능 구현
- 원하는 MBTI를 선택해 자신이 원하는 MBTI와 상대방의 MBTI가 일치하는 유저 간 매칭 성공

#### 유저 간 1대1 채팅 기능
- 매칭이 성공적으로 진행된 유저끼리 1대1 채팅 가능

>Dev
#### 게시판 및 댓글 기능 추가
- 카테고리 별 게시판 및 댓글 기능

#### 추천 MBTI와 매칭 기능 추가
- 자신의 MBTI와 가장 많이 매칭된 MBTI를 추천해주는 기능


---

## 구조 및 설계
### DB 설계
>Release
![erd 초안 이미지](https://user-images.githubusercontent.com/123151812/235838685-51281492-aff6-42d2-8a6b-f3d4f6fe4ee9.png)

>Dev
![KakaoTalk_20230503_151540018](https://user-images.githubusercontent.com/111469930/236074182-d82047af-9fa9-4c1f-b61f-1320494ad895.png)

---

### CI/CD 구축
![image](https://user-images.githubusercontent.com/111469930/229506681-aa8ec884-ce90-43f4-b8e4-c418db1842da.png)

1. main branch에 push 혹은 pull request 시 Github Actions 작동
2. deploy.yml에 따라 빌드된 jar는 AWS의 S3에 저장
3. AWS Code Deploy에게 S3에 저장된 jar 파일을 EC2 배포 명령
4. AWS EC2 서버에 배포


## Git 
### Git Convention
- commit 메시지
  - feat : 새로운 기능의 추가
  - fix : 버그 수정
  - docs : 문서 수정
  - style : 스타일 변경 (포매팅 수정, 들여쓰기 추가, 코드 자체의 변경이 없는 경우)
  - refactor : 코드 리팩토링
  - test : 테스트 관련 코드
  - chore : 그 외 자잘한 수정
  
- pull request 
  - 한글로 작성
  
### Git Branch 전략
- release : 배포를 위한 branch
- develop(dev) : 개발 branch
  - dev의 상태에서 feature branch 생성
- feature : 개인 작업 branch

