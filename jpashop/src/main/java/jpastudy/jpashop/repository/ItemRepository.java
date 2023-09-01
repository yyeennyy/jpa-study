package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em; // 지금은 Spring Data JPA가 @Autowired로 주입을 가능케해주지만, 곧(이미?) Spring이 기본적으로 해 줄 예정이다.

    public void save(Item item){
        if(item.getId() == null){
            em.persist(item); // 신규로 등록! (item이 한번 등록된다면 id가 존재한다.)
        } else {
            em.merge(item); // 대충 update랑 비슷한 것. jpa에 저장하기 전까지 id값이 없다. (자세한 건 뒤에서 더)
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
