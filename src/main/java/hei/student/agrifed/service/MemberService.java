package hei.student.agrifed.service;

import org.springframework.stereotype.Service;

import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.dto.CreateMemberDto;
import hei.student.agrifed.repository.MemberRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;

    public void save(CreateMemberDto createMemberDto) {
        Member member = createMemberDto.toMember();
        // check create member validity
        memberRepository.save(member);
    }
}
