package jpastudy.jpashop.controller;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());  // 빈 껍데기 들고가게 하자 (validation 관련 얘기!)
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    // 1. javax.validation 기능을 어노테이션을 통해 굉장 편하게 할 수 O
    // 2. Spring 공부 하셨으면 알텐데, BindingResult 라는 기능 있음. 원래 오류있는 form 넘어오면 Controller 에서 튕겨버림.
    //    그러나 validate 뒤에 BindingResult 가 있으면, 오류가 거기로 넘어온 다음에, 코드가 쭉 실행됨.
    //    즉 앞부분에 result.hasError() 등으로 조건문 넣어줄 수 O
    public String create(@Valid MemberForm form, BindingResult result) {  // javax.validation 을 쓰는구나~ 해서 MemberForm 에 있는 @NotEmpty 같은 것에 대해 스프링이 validation 해줌

        if (result.hasErrors()) {
            return "members/createMemberForm";  // 스프링이 BindingResult 를 폼까지 끌고가줘서, 이 화면에 뿌릴 수 있다.
            // (참고) thymeleaf 코드 보면
            // <input .... th:class="${#fields.hasErrors('name')}? 'form-control fieldError' : 'form-control'">
            // <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
            // 요런 렌더링 됨

            // 참고로 에러가 있더라도 form 데이터를 그대로 다시 가져감 --> 입력된 정보가 사라지지 않음. 다시 뿌림.
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";  // 저장이 되고나서 재로딩되면 안좋기 때문에 redirect 를 많이 씁니다. redirect 로 첫번째 페이지에 보내버릴게요.
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);  // Entity 를 그대로 뿌리고 있다? 보통은 안돼! DTO 를 사용하자.
        // 주의:::: 이런 템플릿 엔진으로 던질 때는 괜찮지만...

        // API 일 경우에는 "절대 Entity 를 반환하면 안된다" 스펙이 변해버릴 수 있음... ex: Member Entity 에다가 필드를 하나 추가해버린다면... ㄷ
        // Entity 로직 추가로 API 스펙이 변동되는 상황... 그래서 절대 XXXXX
        return "members/memberList";
    }
}