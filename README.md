# 무신사 코딩테스트 과제
## 지원자

- 홍세진

## 1.API 목록
1) 보상 데이터 조회 API
2) 보상 지급 API 
3) 보상 조회 API

## 2.API 문서 
http://localhost:8080/swagger-ui/index.html#/

## 3.요구 사항 
1) 빌드/실행 방법 제시 
```
./gradlew build 
java -jar ./build/libs/musinsa-reward-0.0.1-SNAPSHOT.jar
```
- 테스트 빌드 실패시 추가
```
test {
    exclude 'org/boo/**'
    exclude '**/Bar.class'
}
```


## 3.구현 방식 
### 엔티티 관계도
![entitydiagram](https://user-images.githubusercontent.com/40927029/207064052-269e87de-1a25-4db3-8438-e2746b6c2f35.jpg)

### 오브젝트 관계도 
![entity 관계 (1)](https://user-images.githubusercontent.com/40927029/207064864-dd242268-df2e-4567-a68b-0d2b6aac6da6.jpg)

![code descript1](https://user-images.githubusercontent.com/40927029/207068275-dd339bff-a2e1-4f9a-8147-63cf068aed02.jpg)



## 4.가산점 부여 항목 구현 방식
### 보상 지급 시 동시성 제어 처리
#### 레디스를 이용한 동시성(선착순) 제어 
1. 서비스의 레디스 Sorted Set 데이터 구조  
  
     ![무제3](https://user-images.githubusercontent.com/40927029/206919986-03c06678-8e7b-43b5-9436-2f5e226066cd.jpg)
     
2. 싱글 스레드인 레디스에 Score(타임스탬프) 순으로 대기열 적재 -> 선착순 기능 
    
3. Sorted Set 조건으로 대기열에 하나의 동일 유저ID(Key) 만 생성 가능 -> 중복 진입 제한, 동시성 처리
    
4. 스프링 스케줄러로 1초 마다 일정한 수의 대기열 데이터 조회 후 이벤트 처리 -> 동시성에 대한 서버 부하 제거
- DB 접근 단계(보상 지급시)가 아닌 요청 단계(보상 요청 단계)에서 동시성 제어 처리로 서버 부하 감소
- 스프링 스케줄러를 이용해 score(선착순) 순서대로 대기열에서 데이터를 조회하고 일정한 수의 요청(보상 지급 실행)을 처리한다.
    
![동시성](https://user-images.githubusercontent.com/40927029/206918986-985fbb66-a2b1-4fd8-b3ff-df062dcd9dfa.jpg)


## 5. 기타 구현 방식  
### 스프링 비동기 Event 처리로 서비스 영향도 제어 

### 이벤트 처리 흐름 
1. 스케줄러에서 도메인 기능 실행 
2. 도메인 기능에서 발행 처리시 발행 처리에 대한 이벤트를 발생  
  ![비동기](https://user-images.githubusercontent.com/40927029/206918061-db7f1854-58ed-4f3e-9da9-e12b5be24975.jpg)
  
3. 이벤트 실패 시 트랜잭션 처리로 데이터가 처리되지 않으며 유저는 서버에 저장된 데이터를 조회하여 발행 여부를 확인 하기 때문에 동기화 문제 발생하지 않음


      
