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
                referees.add(rs.getString("id_member_refered"));
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
        Member member = new Member();
        try {
            PreparedStatement memberPs = connection.prepareStatement(memberSql);
            memberPs.setString(1, id);
            ResultSet memberRs = memberPs.executeQuery();
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
                return Optional.of(member);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Member member) {
        String memberSql = """
                    insert into member (firstname, lastname, birthdate, gender, address, phone, profession, email, occupation)
                    values (?,?,?,?,?,?,?,?,?);
                """;
        String refereesSql = """
                    insert into reference (id_member_refered, id_member_referer)
                    values (?,?);
                """;
        try {
            PreparedStatement memberPs = connection.prepareStatement(memberSql);
            memberPs.setString(1, member.getFirstName());
            memberPs.setString(2, member.getLastName());
            memberPs.setString(3, member.getBirthDate().toString());
            memberPs.setString(4, member.getGender().toString());
            memberPs.setString(5, member.getAddress());
            memberPs.setInt(6, member.getPhoneNumber());
            memberPs.setString(7, member.getProfession());
            memberPs.setString(8, member.getEmail());
            memberPs.setString(9, member.getOccupation().toString());
            memberPs.executeUpdate();

            PreparedStatement refereesPs = connection.prepareStatement(refereesSql);
            for (String referee : member.getReferees()) {
                refereesPs.setString(1, member.getId());
                refereesPs.setString(2, referee);
                refereesPs.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Boolean existsById(Integer id) {
        String memberSql = """
                    select count(id) from member where id = ?
                """;
        try {
            PreparedStatement memberPs = connection.prepareStatement(memberSql);
            memberPs.setInt(1, id);
            ResultSet memberRs = memberPs.executeQuery();
            if (memberRs.next()) {
                return memberRs.getInt("count(*)") > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
