package jpastudy.jpashop.service;

import jpastudy.jpashop.domain.Delivery;
import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderItem;
import jpastudy.jpashop.domain.item.Item;
import jpastudy.jpashop.repository.ItemRepository;
import jpastudy.jpashop.repository.MemberRepository;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        // recall: Cascade옵션이 All로 지정되어있다. order를 persist하면, Order의 멤버인 OrderItem도 전부 persist된다.
        // 굉장히 편하다. order만 persist해도, 멤버들이 자동으로 persist되는 cascade옵션을 Order 엔티티에 줬다는 거..
        // 사람들이 cascade의 범위에 대해 고민을 많이 한다.
        // Order가 OrderItem, Delivery를 관리하는데, 이 정도 그림에서만 써야 한다. 오로지 Order만 OrderItem을 참조해서 사용중이다. 다른데서 OrderItem을 참조하는 경우는 없다.
        // 즉 자신 가진 멤버를 다른 것이 참조하지 않는 상태인, private owner인 경우에만 cascade all 지정하면 좋겠다.
        // 하지만 만약에 Delivery가 매우 중요해서, 다른 곳에서 막 참조하고 갖다 쓰는 경우, cascade 막 쓰면 안된다. 그치 함부로 건들면 안되지..
        // 다시 말해서, persist하는 lifecycle이 완전 동일한 경우 쓰면 좋다.
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();

        // JPA의 강점 중요중요!
        // sql을 직접다루는 MyBatis나 jdbctemplate쓰면, 바깥에서 update쿼리를 직접 날려야 한다.
        // 뭐든지 직접.. 로직처리 후 다시 다 끄집어내서 DB쿼리 날려줘야 함.
        // 즉 sql을 직접 다루는 스타일로 갈 때는, 비즈니스 로직에서 그런 트랜잭션 로직을 다 기재할 수밖에 없다.
        // 그런데 JPA에서는 엔티티의 데이터를 바꾸면 JPA가 알아서 바뀐 내용을 감지 (즉 더티체킹) 하여 DB에 업뎃쿼리 날려준다.
    }

    // 검색
//    public List<Order> findOrders(OrderSearch roderSearch){
//        return orderRepository.findAll(orderSearch);
//    }
}

// 참고!!!!!!!!!!!!!!! "도메인 모델 패턴"
//비즈니스 로직 대부분이 Entity에 있다!!!
//현재 서비스 계층은 단순히 엔티티에 역할을 위임한다.
//엔티티가 비즈니스로직을 가지고 객체지향을 특성을 활용하고 있는 것이다.

// 반대로, 엔티티에는 비즈니스로직이 거의 없고 서비스계층에서 대부분의 비즈니스 로직을 처리하는 것을
// 트랜잭션 스크립트 패턴! 이라고 한다.
// 일반적으로 sql다룰 때 많이 했던 그런 것들.

// JPA나, 기타 orm 쓰다 보면, 도메인 모델 패턴. 스타일로 코딩을 많이 하긴 함.

// 뭐가 더 유지보수하기 좋을지를 고민해서 사용하면 된다. 상황에 맞게.
// 한 프로젝트 내에 양립하기도 하고.