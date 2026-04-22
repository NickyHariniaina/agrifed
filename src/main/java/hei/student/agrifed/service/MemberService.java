package hei.student.agrifed.service;

import org.springframework.stereotype.Service;

import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.dto.CreateMemberDto;
import hei.student.agrifed.repository.MemberRepository;
import hei.student.agrifed.utils.MemberValidator;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
    private MemberValidator memberValidator;

    public void save(CreateMemberDto createMemberDto) {
        Member member = createMemberDto.toMember();
        memberValidator.checkCreateMemberDto(createMemberDto);
        for (String referee : member.getReferees()) {
            if (!memberRepository.existsById(Integer.parseInt(referee))) {
                throw new RuntimeException("Referee not found");
            }
        }
        memberRepository.save(member);
    }

}
