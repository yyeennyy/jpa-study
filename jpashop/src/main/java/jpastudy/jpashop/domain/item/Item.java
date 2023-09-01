package jpastudy.jpashop.domain.item;

import jpastudy.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
//@Setter  밖에서 set하는게 아니라, 아래와 같이 비즈니스로직을 통해 변경하도록 하자. 객체지향답게..
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // == 비즈니스 로직 ==
    // 데이터를 가지고 있는 주체가 Entity이므로,
    // Entity에서 해결할 수 있는 건 Entity안에 비즈니스로직 나가는 게 응집도있다.
    // Setter의 불필요성이 보임: 변경 필요가 있으면 Setter가 아니라 아래와 같이 비즈니스 로직을 통해 변경을 해야함
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
