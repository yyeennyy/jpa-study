package jpastudy.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext // SpringBoot가 EM을 주입해준다. SpringBoot에서 해준다. spring-boot-starter-data-jpa에 EntityManager 생성을 해주는 부분이 있기 때문에, SpringBoot에서는 우리가 직접 EntityManager를 생성하지 않아도 된다.
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
