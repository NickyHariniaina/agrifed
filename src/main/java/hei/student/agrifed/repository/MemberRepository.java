package hei.student.agrifed.repository;

import java.sql.Connection;
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
        try {
            PreparedStatement ps = connection.prepareStatement("select firstname, lastname, birthdate, gender, address, phone, profession, email, occupation from member where id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setId(id);
                member.setFirstName(rs.getString("firstname"));
                member.setLastName(rs.getString("lastname"));
                member.setBirthDate(rs.getString("birthdate"));
                member.setGender(Gender.valueOf(rs.getString("gender")));
                member.setAddress(rs.getString("address"));
                member.setPhoneNumber(rs.getInt("phone"));
                member.setProfession(rs.getString("profession"));
                member.setEmail(rs.getString("email"));
                member.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));
                return Optional.of(member);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
