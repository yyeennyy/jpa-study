package jpastudy.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

// @Embeddable 이다
// jpa의 내장타입 표시.. 어딘가에 내장될 수 있다.
// 사용할 때 @Embedded라고 명시 가능
// 둘 중 하나만 명시해도 되는데, 보통 둘 다 명시한다고 함.

@Embeddable
@Getter
public class Address { // Address같은 값 타입은 변경 불가능하게 설계해야 한다.
    private String city;
    private String street;
    private String zipcode;

    // JPA가 객체를 생성할 때 replection이나 proxy같은 기술을 써야 하기 때문에
    // 기본 생성자가 필요하다.
    // jpa스펙상 @Entity나 @Embeddable은 기본생성자를 pubilc or protected로 해야 함
    protected Address() {
    }

    // 값타입인 경우, 변경할 일이 없기에, 딱 생성시점에만 값 할당하는 것이 적절
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
