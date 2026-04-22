package hei.student.agrifed.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hei.student.agrifed.entity.Collectivity;
import hei.student.agrifed.entity.dto.CreateCollectivityDto;
import hei.student.agrifed.entity.dto.CreateCollectivityStructureDto;
import hei.student.agrifed.exception.BadRequestException;
import hei.student.agrifed.exception.NotFoundException;
import hei.student.agrifed.repository.CollectivityRepository;
import hei.student.agrifed.repository.MemberRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CollectivityService {

    private static final int MINIMUM_MEMBERS        = 10;
    private static final int MINIMUM_SENIOR_MEMBERS = 5;

    private CollectivityRepository collectivityRepository;
    private MemberRepository memberRepository;

    public List<Collectivity> createCollectivities(List<CreateCollectivityDto> dtos) {
        List<Collectivity> created = new ArrayList<>();
        for (CreateCollectivityDto dto : dtos) {
            created.add(createOne(dto));
        }
        return created;
    }

    // -------------------------------------------------------------------------
    // Orchestration
    // -------------------------------------------------------------------------

    private Collectivity createOne(CreateCollectivityDto dto) {
        // Ordre des validations : 400 d'abord, puis 404
        validateFederationApproval(dto);
        validateStructurePresence(dto.getStructure());

        // Les membres de la structure comptent dans les 10 effectifs (consigne A :
        // "pouvant être inclus dans les 10 effectifs minimum"). On les fusionne
        // pour éviter les doublons et garantir leur présence dans member_collectivity.
        List<String> allMemberIds = mergeWithStructureMembers(dto);
        dto.setMembers(allMemberIds);

        // 404 : vérifier que chaque ID existe
        validateMembersExist(allMemberIds);

        // 400 : règles de comptage
        validateMinimumMemberCount(allMemberIds);
        validateSeniorMemberCount(allMemberIds);

        return collectivityRepository.save(dto);
    }

    // -------------------------------------------------------------------------
    // Validations
    // -------------------------------------------------------------------------

    /**
     * Consigne A : l'autorisation formelle de la fédération est obligatoire.
     * Spec v0.0.1 → 400 "Collectivity without federation approval".
     */
    private void validateFederationApproval(CreateCollectivityDto dto) {
        if (Boolean.TRUE.equals(dto.getFederationApproval())) return;
        throw new BadRequestException(
                "L'approbation formelle de la fédération est requise pour ouvrir une collectivité."
        );
    }

    /**
     * Spec v0.0.1 → 400 "structure missing".
     * Les 4 postes du bureau sont obligatoires.
     */
    private void validateStructurePresence(CreateCollectivityStructureDto structure) {
        if (structure == null
                || structure.getPresident()     == null
                || structure.getVicePresident() == null
                || structure.getTreasurer()     == null
                || structure.getSecretary()     == null) {
            throw new BadRequestException(
                    "La structure de la collectivité est incomplète : "
                    + "président, vice-président, trésorier et secrétaire sont obligatoires."
            );
        }
    }

    /**
     * Spec v0.0.1 → 404 si un membre référencé est introuvable.
     */
    private void validateMembersExist(List<String> memberIds) {
        for (String id : memberIds) {
            memberRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Membre introuvable avec l'id : " + id));
        }
    }

    /**
     * Consigne A : au moins 10 membres (les membres de la structure inclus).
     */
    private void validateMinimumMemberCount(List<String> memberIds) {
        if (memberIds.size() < MINIMUM_MEMBERS) {
            throw new BadRequestException(
                    "Une collectivité doit comporter au moins " + MINIMUM_MEMBERS
                    + " membres. Actuellement : " + memberIds.size() + "."
            );
        }
    }

    /**
     * Consigne A : parmi les membres, au moins 5 ont ≥ 6 mois d'ancienneté
     * dans la fédération.
     */
    private void validateSeniorMemberCount(List<String> memberIds) {
        long seniorCount = collectivityRepository.countSeniorMembers(memberIds);
        if (seniorCount < MINIMUM_SENIOR_MEMBERS) {
            throw new BadRequestException(
                    "Au moins " + MINIMUM_SENIOR_MEMBERS
                    + " membres doivent avoir une ancienneté d'au moins 6 mois dans la fédération. "
                    + "Actuellement : " + seniorCount + "."
            );
        }
    }

    // -------------------------------------------------------------------------
    // Utilitaire
    // -------------------------------------------------------------------------

    /**
     * Fusionne la liste de membres avec les membres de la structure en utilisant
     * un LinkedHashSet pour : (1) éliminer les doublons, (2) conserver l'ordre
     * d'insertion (membres fournis d'abord, puis membres de la structure si absents).
     */
    private List<String> mergeWithStructureMembers(CreateCollectivityDto dto) {
        Set<String> merged = new LinkedHashSet<>();
        if (dto.getMembers() != null) {
            merged.addAll(dto.getMembers());
        }
        CreateCollectivityStructureDto s = dto.getStructure();
        merged.add(s.getPresident());
        merged.add(s.getVicePresident());
        merged.add(s.getTreasurer());
        merged.add(s.getSecretary());
        return new ArrayList<>(merged);
    }
}
