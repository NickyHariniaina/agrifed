package hei.student.agrifed.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.dto.CreateMemberDto;
import hei.student.agrifed.repository.CollectivityRepository;
import hei.student.agrifed.repository.MemberRepository;
import hei.student.agrifed.utils.MemberValidator;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
    private CollectivityRepository collectivityRepository;
    private MemberValidator memberValidator;

    public List<Member >save(List<CreateMemberDto> createMemberDtos) {
        List<Member> memberCreated = new ArrayList<>();
        for (CreateMemberDto createMemberDto : createMemberDtos) {
            Member member = createMemberDto.toMember();
            memberValidator.checkCreateMemberDto(createMemberDto);
            memberValidator.checkReferees(createMemberDto, memberRepository);
            memberValidator.checkCollectivity(createMemberDto, collectivityRepository);
            memberCreated.add(memberRepository.save(member));
        }
        return memberCreated;
    }

}
