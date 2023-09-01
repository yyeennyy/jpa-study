package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="orders")
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // cascade all 설정으로 persist를 전파한다.
    // 원래 orderItemA, B, C 각각 persist대상이 된 후 persist(order)를 해야 한다.
    // ㄴ> 각 Entity는 persist 대상이라서.
    // 그런데 이렇게 옵션을 주면, persist(order)만 해도 된다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // --- 연관관계 편의 메서드 ---
    // 양방향인 경우 객체상에서 이렇게 한번에 넣어줘야 다루기가 훨씬 편하겠다.
    // DB 관점이랑 다른 얘기다. DB는 이미 연관관계의 주인을 설정함으로써 다 해결된 부분이라 여기서 생각할 개념이 아님.

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 == ★ 중요!
    // : Order(주문) 과정은 복잡하다! OrderItem, Delivery 다 연관관계도 있고.
    // : 그래서 "별도의 생성 메서드"가 있으면 편하다.
    // ++ 이게 그거야 밖에서 set set set 하는 게 아니라, 생성할 때 값 완결시키는 방법을 적용.
    // ++ 주문 생성에 변경사항 있으면 여기만 수정
    // ++ createOrder시점에, orderItems를 넘긴다. 이때, 각각의 orderItem를 생성할 때, 거기서 이미 '재고를 까는 로직'이 포함되어 있다. 오호.. 그렇게 설계 오케이
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){ // ...라는 문법이 있네? 여러개 넘길 수 O
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems)
            order.addOrderItem(orderItem);

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // == 비즈니스 로직 ==
    /**
     * 주문 취소
     */
    public void cancel() {
        // 이런 체크로직도 Entity 내부에 있네!
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소 불가!");
        }
        this.setStatus(OrderStatus.CANCEL); // 상태도 바꾸고!
        for (OrderItem orderItem : orderItems) { // 재고 원복도 시키고!
            orderItem.cancel();
        }
    }

    // == 조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
