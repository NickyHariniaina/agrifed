package hei.student.agrifed.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hei.student.agrifed.entity.Collectivity;
import hei.student.agrifed.entity.MembershipFee;
import hei.student.agrifed.entity.dto.AssignIdentityDto;
import hei.student.agrifed.entity.dto.CreateCollectivityDto;
import hei.student.agrifed.entity.dto.CreateCollectivityStructureDto;
import hei.student.agrifed.entity.dto.CreateMembershipFeeDto;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.ConflictException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.repository.CollectivityRepository;
import hei.student.agrifed.repository.MemberRepository;

import org.springframework.stereotype.Service;

@Service
public class CollectivityService {

    private static final int MINIMUM_MEMBERS        = 10;
    private static final int MINIMUM_SENIOR_MEMBERS = 5;

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public CollectivityService(CollectivityRepository collectivityRepository,
                               MemberRepository memberRepository) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public List<Collectivity> createCollectivities(List<CreateCollectivityDto> dtos) {
        List<Collectivity> created = new ArrayList<>();
        for (CreateCollectivityDto dto : dtos) {
            created.add(createOne(dto));
        }
        return created;
    }

    private Collectivity createOne(CreateCollectivityDto dto) {
        if (!Boolean.TRUE.equals(dto.getFederationApproval())) {
            throw new BadRequestException(
                    "Approval from federation is required");
        }

        CreateCollectivityStructureDto s = dto.getStructure();
        if (s == null || s.getPresident() == null || s.getVicePresident() == null
                || s.getTreasurer() == null || s.getSecretary() == null) {
            throw new BadRequestException(
                    "Structure incomplete : president, vice-president, treasury and secretary is obligatory.");
        }

        // Fusion members + structure (role count in the 10 effectif
        List<String> allMemberIds = mergeWithStructureMembers(dto);
        dto.setMembers(allMemberIds);

        for (String id : allMemberIds) {
            memberRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Membre introuvable avec l'id : " + id));
        }

        if (allMemberIds.size() < MINIMUM_MEMBERS) {
            throw new BadRequestException(
                    "A collectivity got to hava at least : " + MINIMUM_MEMBERS + " members.");
        }

        long seniorCount = collectivityRepository.countSeniorMembers(allMemberIds);
        if (seniorCount < MINIMUM_SENIOR_MEMBERS) {
            throw new BadRequestException(
                    "At leat " + MINIMUM_SENIOR_MEMBERS
                            + " members got to have ≥ 6 months ancientness (actual : " + seniorCount + ").");
        }

        return collectivityRepository.save(dto);
    }

    private List<String> mergeWithStructureMembers(CreateCollectivityDto dto) {
        Set<String> merged = new LinkedHashSet<>();
        if (dto.getMembers() != null) merged.addAll(dto.getMembers());
        CreateCollectivityStructureDto s = dto.getStructure();
        merged.add(s.getPresident());
        merged.add(s.getVicePresident());
        merged.add(s.getTreasurer());
        merged.add(s.getSecretary());
        return new ArrayList<>(merged);
    }


    public Collectivity assignIdentity(Integer id, AssignIdentityDto dto) {
        if (dto == null || (dto.getFederationNumber() == null && dto.getName() == null)) {
            throw new BadRequestException(
                    "At least federationNumber or name must be provided.");

        }

        Collectivity existing = collectivityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Community not found with ID : " + id));

        if (dto.getFederationNumber() != null && existing.getFederationNumber() != null) {
            throw new ConflictException(
                    "The federal number is already defined and cannot be changed.");
        }
        if (dto.getName() != null && existing.getName() != null) {
            throw new ConflictException(
                    "The name is already defined and cannot be changed.");
        }

        if (dto.getName() != null && collectivityRepository.existsByName(dto.getName())) {
            throw new ConflictException(
                    "Name '" + dto.getName() + "' is already in use by another collectivity.");
        }

        return collectivityRepository.assignIdentity(id, dto.getFederationNumber(), dto.getName());
    }

    public List<MembershipFee> findMembershipFeesById(Integer id) {
        if (!collectivityRepository.existsById(id.toString())) {
            throw new NotFoundException("Collectivity not found with ID : " + id);
        }
        return collectivityRepository.findMembershipFeesById(id);
    }

    public List<MembershipFee> saveMembershipFees(List<CreateMembershipFeeDto> createMembershipFeeDtos) {
        List<MembershipFee> memberFeesCreated = new ArrayList<>();
        for (CreateMembershipFeeDto createMembershipFeeDto : createMembershipFeeDtos) {
            MembershipFee memberFee = createMembershipFeeDto.toMembershipFee();
            memberFeesCreated.add(collectivityRepository.saveMembershipFee(memberFee));

        }
        return memberFeesCreated;
    }
}
