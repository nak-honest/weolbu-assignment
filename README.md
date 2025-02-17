# weolbu-assignment
## 어플리케이션 실행 방법
자바 버전이나 다른 환경으로 인한 문제가 발생할 수 있기에 도커를 통해 실행합니다.   
이를 위해서는 [도커 데스크탑](https://www.docker.com/products/docker-desktop/) 설치가 필요하니 참고해주세요.   
호스트에서 이미 8080 포트를 사용 중이라면, 다른 포트를 사용해 주세요.

- gradle 버전 : 8.12.1
- Java 버전 : 17
- Spring Boot 버전 : 3.4.2

```sh
./gradlew clean build
docker build -t spring-boot-app .
docker run -p 8080:8080 spring-boot-app
```
위와 같이 실행하면 http://localhost:8080/ 을 통해 API 호출이 가능합니다.

## API 스펙 문서
http://localhost:8080/swagger-ui/index.html

## 참고 사항
비밀번호와 같은 민감한 정보를 안전하게 통신하기 위해서는 HTTPS 통신이 필요합니다.
현재 스프링 애플리케이션은 HTTP로 통신을 하는데, 앞단에 Ngnix와 같은 웹 서버를 두어 SSL Termination을 하는 것을 생각하였습니다.

`강의는 여러 개를 선택하여 한 번에 신청할 수 있습니다.` 라는 요구사항은 클라이언트 단에서 API 여러 개를 한번에 호출하면 된다고 생각하였습니다. 
그리고 서버에서는 하나의 API 호출에 대해 하나의 강의만 신청할 수 있도록 생각하였습니다.   
강의 신청이 폭주하는 상황에서 여러 개의 강의 중 일부만 성공한 경우, 전체가 실패되는 것보다 부분이라도 성공하는 것이 낫다고 생각하였기 때문입니다.   
이를 하나의 API로 구현하려면 트랜잭션 관리가 복잡해지기 때문에 하나의 API 당 하나의 신청으로 구현하는 것이 적절하다고 판단하였습니다.
