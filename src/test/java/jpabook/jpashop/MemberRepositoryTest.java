package jpabook.jpashop;

import org.assertj.core.api.Assertions;
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
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
//    @Rollback(value = false)
    public void testMember() throws Exception {
        // Given
        Member member = new Member();
        member.setUsername("A");
        
        // When
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // Then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        // Note
        // Object is same because it is in a single transaction.
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}