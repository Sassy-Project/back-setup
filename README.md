# back-setup

## Git Convention
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
  
## Git Branch 전략
- release : 배포를 위한 branch
- develop(dev) : 개발 branch
  - dev의 상태에서 feature branch 생성
- feature : 개인 작업 branch

## CI/CD 구축
![image](https://user-images.githubusercontent.com/111469930/229506681-aa8ec884-ce90-43f4-b8e4-c418db1842da.png)

1. main branch에 push 혹은 pull request 시 Github Actions 작동
2. deploy.yml에 따라 빌드된 jar는 AWS의 S3에 저장
3. AWS Code Deploy에게 S3에 저장된 jar 파일을 EC2 배포 명령
4. AWS EC2 서버에 배포

돼라 된다 됐다 이번에 진짜 찐막
