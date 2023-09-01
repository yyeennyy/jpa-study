package jpastudy.jpashop.service;

import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor
@RequiredArgsConstructor // final이 있는 필드의 생성자를 만들어준다. 이것이 All보다 나은 선택임
public class MemberService {

    // 이런건 변경할 일 없는 필드 -> final 권장
    // 장점: 컴파일시점에 주입 체크도 가능해진다.
    private final MemberRepository memberRepository;

    // 권장: 생성자 injection
    // 장점: 생성시점에 끝나고, 테스트시 직접 주입을 가능함. Mock 주입 등 가능해짐. 주입이 명확해짐.
    // 참고: Spring은, 생성자가 단 하나만 있는 경우 자동으로 Autowired 해준다. 어노테이션이 없어도!
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // Setter Injection
    // 장점: 테스트할 때 등 Mock 같은 걸 직접 주입 가능
    // 단점: 정말 Runtime에 누군가 memberRepository를 바꿀 수도 있음. 보통 조립시점(애플리케이션 실행시점)에 조립이 다 끝나는데, 중간에 바꿀 일이 없긴 함. 그래서 Setter injection이 좋지 않음.
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional
    // 클래스차원에 설정한 것 말고 이렇게 Transactional 따로 준거가 우선임
    public Long join(Member member){
        validateDuplicateMember(member);  // 문제있긴함: 동시에 중복검증에 통과하게되면 아래 save 로직을 동시에 호출가능할 가능성이 있다
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원.");
        }
    }

    // 회원 전체 조회
    // readOnly true면 jpa가 조회할 때 좀 최적화되는 방식으로 조회함
    // 영속성컨텍스트에서 플러시안하고 더티체크 안하는 이점, 데이터베이스에 따라서 단순 읽기용으로 리소스 덜쓰는 등 (<- db마다 다르고, 확인해볼 수 있음)
//    @Transactional(readOnly = true)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    // 단건 조회
//    @Transactional(readOnly = true)
     public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
     }

}
