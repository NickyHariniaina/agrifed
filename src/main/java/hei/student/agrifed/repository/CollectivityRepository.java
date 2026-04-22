package hei.student.agrifed.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Collectivity> findById(Integer id){
        String sql = """
                 SELECT c.id, c.federation_number, c.name, c.location,
                       c.president_id, c.vice_president_id, c.treasurer_id, c.secretary_id
                FROM collectivity c
                WHERE c.id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();

            Collectivity c = new Collectivity();
            c.setId(rs.getString("id"));
            c.setFederationNumber(rs.getObject("federation_number") != null
                    ? rs.getInt("federation_number") : null);
            c.setName(rs.getString("name"));
            c.setLocation(rs.getString("location"));

            // Structure
            CollectivityStructure structure = new CollectivityStructure(
                    fetchMember(rs.getString("president_id"),      "Président"),
                    fetchMember(rs.getString("vice_president_id"), "Vice-président"),
                    fetchMember(rs.getString("treasurer_id"),      "Trésorier"),
                    fetchMember(rs.getString("secretary_id"),      "Secrétaire")
            );
            c.setStructure(structure);

            // Membres
            c.setMembers(findMembersByCollectivityId(id));
            return Optional.of(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM collectivity WHERE name = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collectivity assignIdentity(Integer id, Integer federationNumber, String name) {
        String sql = """
                UPDATE collectivity
                SET federation_number = COALESCE(?, federation_number),
                    name              = COALESCE(?, name)
                WHERE id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (federationNumber != null) ps.setInt(1, federationNumber);
            else ps.setNull(1, java.sql.Types.INTEGER);
            if (name != null) ps.setString(2, name);
            else ps.setNull(2, java.sql.Types.VARCHAR);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findById(id).orElseThrow(() ->
                new NotFoundException("Collectivity not found with id : " + id));
    }


    private List<Member> findMembersByCollectivityId(Integer collectivityId) {
        String sql = """
                SELECT m.id FROM member_collectivity mc
                JOIN member m ON m.id = mc.id_member
                WHERE mc.id_collectivity = ?
                """;
        List<Member> members = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                memberRepository.findById(rs.getString("id")).ifPresent(members::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return members;
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

    public Boolean existsById(String id) {
        String collectivitySql = """
                SELECT COUNT(id) as c FROM collectivity
                WHERE id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(collectivitySql);
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("c") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
