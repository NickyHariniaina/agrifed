package hei.student.agrifed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hei.student.agrifed.entity.Collectivity;
import hei.student.agrifed.entity.CollectivityStructure;
import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.dto.CreateCollectivityDto;
import hei.student.agrifed.entity.dto.CreateCollectivityStructureDto;
import hei.student.agrifed.exception.NotFoundException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CollectivityRepository {

    private Connection connection;
    private MemberRepository memberRepository;

    /**
     * Compte le nombre de membres de la liste ayant rejoint la fédération
     * il y a au moins 6 mois (consigne A).
     *
     * PRÉREQUIS SCHEMA : colonne `joined_at TIMESTAMP DEFAULT NOW()` dans `member`.
     * Voir SCHEMA_CHANGES.sql.
     */
    public long countSeniorMembers(List<String> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) return 0;

        // Construit les placeholders dynamiquement : ?,?,?,...
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < memberIds.size(); i++) {
            if (i > 0) placeholders.append(",");
            placeholders.append("?");
        }

        String sql = """
                SELECT COUNT(*) FROM member
                WHERE id IN (%s)
                  AND joined_at <= NOW() - INTERVAL '6 months'
                """.formatted(placeholders);

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < memberIds.size(); i++) {
                ps.setString(i + 1, memberIds.get(i));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * Persiste la collectivité et toutes ses associations membres,
     * puis retourne l'objet réponse hydraté (Member complets, pas juste des IDs).
     */
    public Collectivity save(CreateCollectivityDto dto) {
        String insertCollectivitySql = """
                INSERT INTO collectivity (location, president_id, treasurer_id, vice_president_id, secretary_id)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;
        String insertMemberCollectivitySql = """
                INSERT INTO member_collectivity (id_member, id_collectivity)
                VALUES (?, ?)
                """;

        try {
            // 1. Insérer la collectivité, récupérer l'id généré
            CreateCollectivityStructureDto s = dto.getStructure();
            PreparedStatement cPs = connection.prepareStatement(insertCollectivitySql);
            cPs.setString(1, dto.getLocation());
            cPs.setInt(2, Integer.parseInt(s.getPresident()));
            cPs.setInt(3, Integer.parseInt(s.getTreasurer()));
            cPs.setInt(4, Integer.parseInt(s.getVicePresident()));
            cPs.setInt(5, Integer.parseInt(s.getSecretary()));

            ResultSet rs = cPs.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Échec de l'insertion de la collectivité.");
            }
            String collectivityId = rs.getString("id");

            // 2. Associer chaque membre à la collectivité
            PreparedStatement mcPs = connection.prepareStatement(insertMemberCollectivitySql);
            for (String memberId : dto.getMembers()) {
                mcPs.setInt(1, Integer.parseInt(memberId));
                mcPs.setInt(2, Integer.parseInt(collectivityId));
                mcPs.executeUpdate();
            }

            // 3. Construire la réponse hydratée
            return buildResponse(collectivityId, dto);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // -------------------------------------------------------------------------
    // Méthodes privées
    // -------------------------------------------------------------------------

    private Collectivity buildResponse(String collectivityId, CreateCollectivityDto dto) {
        CreateCollectivityStructureDto s = dto.getStructure();

        CollectivityStructure structure = new CollectivityStructure(
                fetchMember(s.getPresident(),      "Président"),
                fetchMember(s.getVicePresident(),  "Vice-président"),
                fetchMember(s.getTreasurer(),      "Trésorier"),
                fetchMember(s.getSecretary(),      "Secrétaire")
        );

        List<Member> members = new ArrayList<>();
        for (String memberId : dto.getMembers()) {
            members.add(fetchMember(memberId, "Membre"));
        }

        return new Collectivity(collectivityId, dto.getLocation(), structure, members);
    }

    private Member fetchMember(String id, String role) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(role + " introuvable avec l'id : " + id));
    }
}
