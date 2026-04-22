package hei.student.agrifed.repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import hei.student.agrifed.entity.Gender;
import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.MemberOccupation;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class MemberRepository {

    private Connection connection;

    public List<String> findAllReferees(String id) {
        String refereesSql = """
                    select mc.id_member_refered from member_collectivity mc
                    where mc.id_collectivity = ?
                """;
        List<String> referees = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(refereesSql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                referees.add(String.valueOf(rs.getInt("id_member_refered")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return referees;
    }

    public Optional<Member> findById(String id) {
        String memberSql = """
                    select firstname, lastname, birthdate, gender, address, phone, profession, email, occupation, joined_at
                    from member where id = ?
                """;
        String collectivitySql = """
                    select id_collectivity from member_collectivity where id_member = ?
                """;
        Member member = new Member();
        try {
            PreparedStatement memberPs = connection.prepareStatement(memberSql);
            memberPs.setInt(1, Integer.parseInt(id));
            ResultSet memberRs = memberPs.executeQuery();

            PreparedStatement collectivityPs = connection.prepareStatement(collectivitySql);
            collectivityPs.setInt(1, Integer.parseInt(id));
            ResultSet collectivityRs = collectivityPs.executeQuery();
            if (memberRs.next()) {
                member.setId(id);
                member.setFirstName(memberRs.getString("firstname"));
                member.setLastName(memberRs.getString("lastname"));
                member.setBirthDate(LocalDate.parse(memberRs.getString("birthdate")));
                member.setGender(Gender.valueOf(memberRs.getString("gender")));
                member.setAddress(memberRs.getString("address"));
                member.setPhoneNumber(memberRs.getInt("phone"));
                member.setProfession(memberRs.getString("profession"));
                member.setEmail(memberRs.getString("email"));
                member.setOccupation(MemberOccupation.valueOf(memberRs.getString("occupation")));
                if (collectivityRs.next()) {
                    member.setCollectivityIdentifier(collectivityRs.getString("id_collectivity"));
                }
                return Optional.of(member);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Member save(Member member) {
        String memberSql = """
                    insert into member (firstname, lastname, birthdate, gender, address, phone, profession, email, occupation)
                    values (?,?,?,?::gender,?,?,?,?,?::occupation) returning id, firstname, lastname, birthdate, gender, address, phone, profession, email, occupation, joined_at;
                """;
        String refereesSql = """
                    insert into reference (id_member_refered, id_member_referer)
                    values (?,?) returning id_member_referer;
                """;
        String collectivitySql = """
                    insert into member_collectivity (id_member, id_collectivity)
                    values (?,?) returning id_member, id_collectivity;
                """;
        Member memberToReturn = new Member();

        try {
            PreparedStatement memberPs = connection.prepareStatement(memberSql);
            memberPs.setString(1, member.getFirstName());
            memberPs.setString(2, member.getLastName());
            memberPs.setObject(3, member.getBirthDate());
            memberPs.setString(4, member.getGender().toString());
            memberPs.setString(5, member.getAddress());
            memberPs.setInt(6, member.getPhoneNumber());
            memberPs.setString(7, member.getProfession());
            memberPs.setString(8, member.getEmail());
            memberPs.setString(9, member.getOccupation().toString());
            ResultSet memberRs = memberPs.executeQuery();

            if (memberRs.next()) {
                memberToReturn.setId(memberRs.getString("id"));
                memberToReturn.setFirstName(memberRs.getString("firstname"));
                memberToReturn.setLastName(memberRs.getString("lastname"));
                memberToReturn.setBirthDate(LocalDate.parse(memberRs.getString("birthdate")));
                memberToReturn.setGender(Gender.valueOf(memberRs.getString("gender")));
                memberToReturn.setAddress(memberRs.getString("address"));
                memberToReturn.setPhoneNumber(memberRs.getInt("phone"));
                memberToReturn.setProfession(memberRs.getString("profession"));
                memberToReturn.setEmail(memberRs.getString("email"));
                memberToReturn.setOccupation(MemberOccupation.valueOf(memberRs.getString("occupation")));
            }

            PreparedStatement refereesPs = connection.prepareStatement(refereesSql);
            for (String referee : member.getReferees()) {
                refereesPs.setInt(1, Integer.parseInt(memberToReturn.getId()));
                refereesPs.setInt(2, Integer.parseInt(referee));
                memberRs = refereesPs.executeQuery();
                if (memberRs.next()) {
                    memberToReturn.setReferees(new ArrayList<>());
                    memberToReturn.getReferees().add(String.valueOf(memberRs.getInt("id_member_referer")));
                }
            }

            PreparedStatement collectivityPs = connection.prepareStatement(collectivitySql);
            collectivityPs.setInt(1, Integer.parseInt(memberToReturn.getId()));
            collectivityPs.setInt(2, Integer.parseInt(member.getCollectivityIdentifier()));
            ResultSet collectivityRs = collectivityPs.executeQuery();
            if (collectivityRs.next()) {
                memberToReturn.setCollectivityIdentifier(collectivityRs.getString("id_collectivity"));
            }
            return memberToReturn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean existsById(String id) {
        String memberSql = """
                    select count(id) as c from member where id = ?
                """;
        try {
            PreparedStatement memberPs = connection.prepareStatement(memberSql);
            memberPs.setInt(1, Integer.parseInt(id));
            ResultSet memberRs = memberPs.executeQuery();
            if (memberRs.next()) {
                return memberRs.getInt("c") > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
