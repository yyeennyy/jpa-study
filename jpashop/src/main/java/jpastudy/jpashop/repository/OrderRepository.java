package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch){
        // jpql
        // 일단 orderSearch가 status과 name을 둘다 갖고있으면 아래와 같이 쿼리로 읽어오면 되는데,
        // 만약 아무거나 다 가져와도 되어서 status 또는 name이 없다면
        // 아래처럼 박아둘 수 없다.
        // 즉 동적쿼리가 되어야 한다. (mybatis는 동적쿼리 쓰기 굉장히 편하다는 이점 O)
        // 동적쿼리를 해결하는게 만만치는 않다. (mybatis같은 경우에는 동적쿼리를 xml로 할 수 있음)
        // jpa에서는 그러한 동적쿼리 다루기를 어떻게 해야 할까?

        return em.createQuery("select o from Order o join o.member m" +
                " where o.status = :status " +
                " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setFirstResult(100)  // 참고) 페이징 이런식으로 진행하면 됨
                .setMaxResults(1000)
                .getResultList();
    }

    // jpa가 표준으로 제공하는
    // 근데 권장하는 방법 아니라, 가볍게 듣기만 하시오. 실무에서 쓰지 않으심.
    /**
     * JPA Criteria
     */
//    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
//    }

}
