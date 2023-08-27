package jpastudy.jpashop.service;

import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class) // junit 실행할 때 Spring 엮어서 실행할래
@SpringBootTest
@Transactional  // 끝에 commit안하고 rollback을 함 (영속성 컨텍스트에 있는 것을 flush 안 함)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em; // 실제로는 롤백이지만 db에 날리는 쿼리문을 보고싶어

    @Test
//    @Rollback(false) // rollback 안하고 commit을 하기 때문에 이제 insert쿼리 나가는거 볼 수 있다.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        em.flush(); // 실제로는 롤백이지만 db에 날리는 쿼리문을 보고싶어 (일단 flush를 한다면 db에 강제로 쿼리 나감)
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외    () throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //When
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 한다!
//        try{
//            memberService.join(member2);
//        } catch(IllegalStateException e){
//            return;
//        }

        //Then
        fail("예외가 발생해야 한다.");
    }

}