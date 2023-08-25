//package jpastudy.jpashop;
//
//import jpastudy.jpashop.domain.Member;
//import org.assertj.core.api.Assertions;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//@RunWith(SpringRunner.class) //Junit에게 알려줘야 함. Spring관련된 테스트 할 거라고.
//@SpringBootTest
//@Rollback(false) // 이런 설정 가능
//public class MemberRepositoryTest {
//
//    @Autowired MemberRepository memberRepository;
//
//    // @Transactional
//    // em을 통한 데이터 접근은 꼭 Transaction 하에서 이루어져야 한다.
//    // Transactional이 Test에 있으면 롤백을 한다는 것!
//
//    @Test
//    @Transactional
//    public void testMember() throws Exception {
//        //given
//        Member member = new Member();
//        member.setUsername("memberA");
//
//        //when
//        Long saveId = memberRepository.save(member);       // insert 쿼리가 나가는 것을 로그에서 확인 가능.
//        Member findMember = memberRepository.find(saveId); // 데이터가 영속성 컨텍스트에 있으므로, select 쿼리는 나갈 필요가 없다. 1차캐시에 있는 것을 꺼내옴.
//
//        //then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(member);
//
//        System.out.println("findMember == member: " + (findMember == member));
//        // true! 동일한 영속성 컨텍스트 내에서는 id(식별자)가 같으면 같은 Entity 이다. (cf. 1차캐시)
//
//    }
//}