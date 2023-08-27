package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext
//    @Autowired // @PersistenceContext말고 @Autowired해도 됨. 그치만 근본적으로 EM은 @Per.. 가 있어야 injection이 되는데, SpringBoot는 @Autowired도 가능함. by Spring Data JPA
//    private EntityManager em;

    private final EntityManager em; // 생성자 injection

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Long save(Member member) {
        em.persist(member); // persist시점엔 insert 쿼리 안 나간다. 원래 commit될 때 flush되면서 insert 쿼리 쫙 나가는 거임.
        return member.getId();
    }
}
