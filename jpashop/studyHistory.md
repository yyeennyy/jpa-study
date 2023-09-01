● studyHistory.txt의 목적  
- 기본적인 학습 기록은 여기 말고 코드 주석으로 작성하고 있다.  
- 주석으로 작성하기에 애매한 것들 또는 주석으로 달고 싶지 않은 것은 여기 메모해두고자 한다.  


## 2023-08-24
1. jar 파일  
./gradlew clean build하면 작성한 테스트코드들도 쭉 실행이 된다.  
테스트가 잘 끝나면, build/libs 경로에 jar파일이 생긴다.  
java -jar .... 로 실행시켜보면, Spring 올라온다! 배포할 때 이렇게 던지면 되는 것임  

2. 쿼리 파리미터 로그 남기기 (개발할 때 편리함 떡상)  
jpa를 사용하다보면 query가 어느 타이밍에 나가는지 궁금할 때가 많다.  
그리고 query 파라미터 찍는게 별로라고 한다. ? 로 뜨는 것을 확인할 수가 없다.  
그래서 yml에서 logging 설정 하면 쿼리 파라미터를 좀 편하게 볼 수 있다.  
스부 2.x, hibernate5일 경우  
logging:  
  level:  
    org.hibernate.type: trace  
스부 3.x, hibernate6 이상은 org.hibernate.orm.jdbc.bind: trace이다.  
위 방법 말고 외부 라이브러리를 사용할 수도 있다. (쿼리파라미터 관련 말고도 다양한 옵션 있다!)  
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1'  
참고: 이런 외부라이브러리는 시스템 자원을 사용한다.  
개발환경 말고 운영시스템에 적용하려면 꼭 성능테스트를 하고 사용하는 것이 좋다. 어쩌면 성능 확 저하시킬 수 있어서 유의.  

3. 버전정보 명시? 명시안함?  
버전정보를 명시해야하는 경우: 스프링부트에서 미리 세팅해놓은 버전정보가 없는 라이브러리일 경우, 명시해야 한다.  
아래 두개를 비교해보자.  
implementation 'org.springframework.boot:spring-boot-devtools'  
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1'  


## 2023-08-25
1. Entity 설계 능력 기르기  
여러분도 직접 설계해보고, create table 쿼리 눈으로 확인하면서,  
여러분이 의도한대로 잘 만들어지는지 확인해보는 것이 좋아요.  

2. 모든 연관관계는 지연로딩으로 설정!  
EAGER 절대 사용하지 말자. LAZY 무조건!  
@XXXToOne은 기본값이 즉시로딩이라서 바꿔줘야 함. (@XXXToMany는 기본이 지연로딩이라 괜찮)  
만약 연관된 엔티티를 DB조회시 같이 끌고와야 한다면, fetch join이나 엔티티그래프같은 기능으로 그렇게 하자.    
문제점예시) jpql에서 여러 order를 조회하기 위해 쿼리를 1개 날렸다. 그런데 member가 EAGER로 되어있으면,  
가져오는 order의 개수가 100개일 경우 member를 가져오는 쿼리가 100번 실행된다.  
즉 1(order) + 100(member) 만큼의 쿼리가 날아가게 되는 문제가 발생.  
다시말해, 100건의 order 조회할때 1번의 쿼리만 날리면 될 것을, member가 100번 불러와지는 문제가 발생.  
유의: EAGER는 join개념이 아님.  

3. "컬렉션은 필드수준에서 초기화" 하는 것이 안전!  
- null문제에서 안전
- 하이버네이트 특성 때문: 하이버네이트는 엔티티를 영속화할 때, 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다.  
  컬렉션 변동을 추적할 수 있도록, 하이버네이트만의 어떤 걸로 감싸서 관리를 하는 개념임.  
  getClass로 확인해보면,  
  class java.util.ArrayList  
  class org.hibernate.collection.internal.PersistentBag  
  이렇게 다르다.  
- 코드가 간결

## 2023-08-25
1. 오늘!  
- '주문'할 때 Entity나 JPA가 어떻게 얽혀 돌아가는지 배우는 중요한 시간이다.  

2. 엔티티 모델 패턴  




