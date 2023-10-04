package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 이렇게 기본생성자 다루기도 가능!
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;
    private int count; // 주문 수량

//    // new연산자를 이렇게 방지할 수 있구나!
//    // cf. JPA는 기본생성자를 protected까지 허용해준다.
//    protected OrderItem() {
//    }

    // == 생성 메서드 ==
    // : 참고 - 주문상품 가격은 고정해놓으면 되는 거 아닌가요? 아닙니다. 주문시마다 할인 등으로 인해 가격 변동 O
    public static OrderItem createOrderItem(Item item, int orderPrice,  int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }



    // == 비즈니스 로직 ==
    public void cancel() {
        getItem().addStock(count);
    }

    // == 조회 로직 ==
    /**
     * 주문 상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
