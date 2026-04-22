package hei.student.agrifed.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class MemberPaymentRepository {
    private final Connection connection;

    public MemberPaymentRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<Integer> findCollectivityIdByMembershipFee(Integer membershipFeeId) {
        String sql = "SELECT id_collectivity FROM membership_fee WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, membershipFeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rs.getInt("id_collectivity"));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
