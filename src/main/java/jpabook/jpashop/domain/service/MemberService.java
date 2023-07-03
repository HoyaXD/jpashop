package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)//트랜잭션 안에서 변경하는거면 필요하다
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    } Lombok 참조

    /*
    *setter injeciton 테스트 코드를 작성하기에 적합하다. 단점 : 런타임에 이걸 바꿀리가 있음
    * @Autowired //위의 autoworied는 지우고
    * public void setMemberRopository(MemberRepository memberRepository) {
    *   this.memberRepository = memberRepository;
    * }
    * */

    /*
    *요즘은 생성 트랜젝션
     * @Autowired //위의 autoworied는 지우고
     * public void MemberRopository(MemberRepository memberRepository) {
     *   this.memberRepository = memberRepository;
     * }
     *
     * public static void main(String[] args) {
     *      MemberService memberService = new MemberService(Mock());
     * }
     */

    //회원가입
    @Transactional(readOnly = false)
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //중복회원 감정
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //@Transactional(readOnly = true) jpa가 조회하는 것에는 성능이 좀 더 높다. 읽기전용 트랜젝션 = 리소스 덜씀
    //회원 전체 조회
    public List<Member> findMembers () {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void update(Long id, String name) {

        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }
}
