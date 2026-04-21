package hei.student.agrifed.repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hei.student.agrifed.entity.Gender;
import hei.student.agrifed.entity.Member;
import hei.student.agrifed.entity.MemberOccupation;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class MemberRepository {

    private Connection connection;

    public Optional<Member> findById(String id) {
        String memberSql = """
                    select firstname, lastname, birthdate, gender, address, phone, profession, email, occupation
                    from member where id = ?
                """;
        String referencesSql = """
                    select mc.id_member_refered from member_collectivity mc
                    where mc.id_collectivity = ?";
                """;
        Member member = new Member();
        List<String> referees = new ArrayList<>();
        try {
            PreparedStatement memberPs = connection.prepareStatement(memberSql);
            memberPs.setString(1, id);
            ResultSet memberRs = memberPs.executeQuery();

            PreparedStatement referencesPs = connection.prepareStatement(referencesSql);
            referencesPs.setString(1, id);
            ResultSet referencesRs = referencesPs.executeQuery();

            if (memberRs.next()) {
                member.setId(id);
                member.setFirstName(memberRs.getString("firstname"));
                member.setLastName(memberRs.getString("lastname"));
                member.setBirthDate(memberRs.getString("birthdate"));
                member.setGender(Gender.valueOf(memberRs.getString("gender")));
                member.setAddress(memberRs.getString("address"));
                member.setPhoneNumber(memberRs.getInt("phone"));
                member.setProfession(memberRs.getString("profession"));
                member.setEmail(memberRs.getString("email"));
                member.setOccupation(MemberOccupation.valueOf(memberRs.getString("occupation")));

                while (referencesRs.next()) {
                    referees.add(referencesRs.getString("id_member_refered"));
                }
                member.setReferees(referees);

                return Optional.of(member);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
