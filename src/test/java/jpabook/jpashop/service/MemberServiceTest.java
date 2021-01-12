package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(false)
    public void signup() throws Exception {
        // Given
        Member member = new Member();
        member.setName("kim");

        // When
        Long id = memberService.join(member);

        // Then

        // If id is same, then object is also same.
        assertEquals(member, memberRepository.findOne(id));
    }

    @Test(expected = IllegalStateException.class)
    public void exclude_duplicated_member() throws Exception {
        // Given
        Member member1 = new Member();
        member1.setName("lee");

        Member member2 = new Member();
        member2.setName("lee");

        // When
        memberService.join(member1);
        memberService.join(member2);

        // Then
        fail("No exception occurred.");
    }

}