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
}
