package jpabook.jpashop.domain.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext
    private final EntityManager em;

    /*
    * 내가 직접 Entity factory를 주입하고 싶다.
    * @PersistenceUnit
    * private EntityManagerFactory emf;
    */

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
        //이게 member를 찾아서 반환해준다.
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
        //jpa 쿼리문
        //sql entity 객체를 조회함
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name", name).getResultList();
        //바인딩
    }
}
