package jpastudy.jpashop.controller;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    // 와 신기하다 이거 검증실패하면 BindingResult 도 같이 넘어와서 (MemberController 참고) 저 message 를 뜨게 할 수 있다.
    @NotEmpty(message = "회원 이름은 필수입니다.")   // javax.validation 을 통해 스프링부트가 이 값은 필수록 들어가도록 validate 해준다.
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
