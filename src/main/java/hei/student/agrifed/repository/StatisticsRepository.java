package hei.student.agrifed.repository;

import hei.student.agrifed.entity.CollectivityStatistic;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsRepository {
    private final Connection connection;
    private final CollectivityActivityRepository collectivityActivityRepository;

    public StatisticsRepository(Connection connection, CollectivityActivityRepository collectivityActivityRepository) {
        this.connection = connection;
        this.collectivityActivityRepository = collectivityActivityRepository;
    }

    private List<String> findAllCollectivityIds() {
        String sql = "SELECT DISTINCT id_collectivity FROM member_collectivity";
        List<String> ids = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ids.add(rs.getString("id_collectivity"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ids;
    }

    private int countNewMembers(String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
            SELECT COUNT(m.id)
            FROM member m
            JOIN member_collectivity mc ON mc.id_member = m.id
            WHERE mc.id_collectivity = ?
              AND m.joined_at >= ?
              AND m.joined_at <= ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private int countTotalMembers(String collectivityId) {
        String sql = """
            SELECT COUNT(*) FROM member_collectivity
            WHERE id_collectivity = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private double sumPaidByMember(String memberId, String collectivityId, LocalDate to) {
        String sql = """
        SELECT COALESCE(SUM(mp.amount), 0)
        FROM member_payment mp
        JOIN membership_fee mf ON mf.id = mp.id_membership_fee
        WHERE mp.id_member = ?
          AND mf.id_collectivity = ?
          AND mf.status = 'ACTIVE'
          AND mp.creation_date <= ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.setString(2, collectivityId);
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }

    private double sumDueForCollectivity(String collectivityId,  LocalDate to) {
        String sql = """
    SELECT COALESCE(SUM(
      CASE
        WHEN frequency = 'MONTHLY' THEN
          amount * (EXTRACT(YEAR FROM AGE(?, eligible_from)) * 12
                   + EXTRACT(MONTH FROM AGE(?, eligible_from)) + 1)
        ELSE amount
      END
    ), 0)
    FROM membership_fee
    WHERE id_collectivity = ?
      AND status = 'ACTIVE'
      AND eligible_from <= ?
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(to));
            ps.setDate(2, Date.valueOf(to));
            ps.setString(3, collectivityId);
            ps.setDate(4, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }

    private List<String> findMemberIdsByCollectivity(String collectivityId) {
        String sql = "SELECT id_member FROM member_collectivity WHERE id_collectivity = ?";
        List<String> ids = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getString("id_member"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ids;
    }

    public List<CollectivityStatistic> getStatistics(LocalDate from, LocalDate to) {
        List<String> collectivityIds = findAllCollectivityIds();//Récupérer toutes les collectivités
        List<CollectivityStatistic> result = new ArrayList<>();

        for (String collectivityId : collectivityIds) {
            int newMembers = countNewMembers(collectivityId, from, to);
            int totalMembers = countTotalMembers(collectivityId);

            double due = sumDueForCollectivity(collectivityId, to);

            double percentage = 0.0;
            if (totalMembers > 0 && due > 0) {
                List<String> memberIds = findMemberIdsByCollectivity(collectivityId);
                int upToDateCount = 0;
                for (String memberId : memberIds) {
                    double paid = sumPaidByMember(memberId, collectivityId, to);
                    if (paid >= due) {
                        upToDateCount++;
                    }
                }
                percentage = (upToDateCount * 100.0) / totalMembers;
            }

            double overallAssiduity = collectivityActivityRepository
                    .getOverallAssiduityPercentageForCollectivity(collectivityId, from, to);

            result.add(new CollectivityStatistic(collectivityId, newMembers, percentage, overallAssiduity));
        }

        return result;
    }
}
