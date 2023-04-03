# back-setup

## CI/CD 구축
![image](https://user-images.githubusercontent.com/111469930/229506681-aa8ec884-ce90-43f4-b8e4-c418db1842da.png)

1. main branch에 push 혹은 pull request 시 Github Actions 작동
2. deploy.yml에 따라 빌드된 jar는 AWS의 S3에 저장
3. AWS Code Deploy에게 S3에 저장된 jar 파일을 EC2 배포 명령
4. AWS EC2 서버에 배포
