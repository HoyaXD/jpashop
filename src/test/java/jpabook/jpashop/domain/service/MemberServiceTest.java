package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class) //jnit 실행할 때 스프링이랑 같이 실행할래?
@SpringBootTest // 스프링부트테스트할때 있어야됨 없으면 다 실패함
@Transactional // 트랜잭션을 걸고 테스트하고 되면 롤백(롤백이 기본값이다.)
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    /*
    insert 쿼리문을 보고 싶다면
    @Autoowired EntityManager em; //then 으로 이동
    */

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);
        //then
        //em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
//        try {
            memberService.join(member2); //여기서 예외가 발생하여야 한다. 왜냐하면 중복되기 때문에
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생해야 한다.");
    }

}