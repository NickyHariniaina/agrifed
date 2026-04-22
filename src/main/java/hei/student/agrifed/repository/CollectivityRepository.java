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

import org.springframework.stereotype.Repository;

@Repository
public class CollectivityRepository {

    private final Connection connection;
    private final MemberRepository memberRepository;

    public CollectivityRepository(Connection connection, MemberRepository memberRepository) {
        this.connection = connection;
        this.memberRepository = memberRepository;
    }


    public long countSeniorMembers(List<String> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) return 0;

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
            CreateCollectivityStructureDto s = dto.getStructure();
            PreparedStatement cPs = connection.prepareStatement(insertCollectivitySql);
            cPs.setString(1, dto.getLocation());
            cPs.setInt(2, Integer.parseInt(s.getPresident()));
            cPs.setInt(3, Integer.parseInt(s.getTreasurer()));
            cPs.setInt(4, Integer.parseInt(s.getVicePresident()));
            cPs.setInt(5, Integer.parseInt(s.getSecretary()));

            ResultSet rs = cPs.executeQuery();
            if (!rs.next()) throw new RuntimeException("Fail insertion collectitivy");
            String collectivityId = rs.getString("id");

            PreparedStatement mcPs = connection.prepareStatement(insertMemberCollectivitySql);
            for (String memberId : dto.getMembers()) {
                mcPs.setInt(1, Integer.parseInt(memberId));
                mcPs.setInt(2, Integer.parseInt(collectivityId));
                mcPs.executeUpdate();
            }

            return buildResponse(collectivityId, dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Collectivity buildResponse(String collectivityId, CreateCollectivityDto dto) {
        CreateCollectivityStructureDto s = dto.getStructure();
        CollectivityStructure structure = new CollectivityStructure(
                fetchMember(s.getPresident(),     "President"),
                fetchMember(s.getVicePresident(), "Vice-president"),
                fetchMember(s.getTreasurer(),     "Treasurer"),
                fetchMember(s.getSecretary(),     "Secretary")
        );

        List<Member> members = new ArrayList<>();
        for (String memberId : dto.getMembers()) {
            members.add(fetchMember(memberId, "Member"));
        }

        return new Collectivity(collectivityId, dto.getLocation(), structure, members);
    }

    private Member fetchMember(String id, String role) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(role + " not found with the id : " + id));
    }
}
