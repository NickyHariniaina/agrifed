package hei.student.agrifed.repository;

import hei.student.agrifed.entity.CollectivityStatistic;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticsRepository {
    private Connection connection;

    public StatisticsRepository(Connection connection) {
        this.connection = connection;
    }


    public List<CollectivityStatistic> getStatistics(LocalDate from, LocalDate to) {

        String sql = """
        SELECT
            mc.id_collectivity,

            (
                SELECT COUNT(m.id)
                FROM member m
                JOIN member_collectivity mc2 ON mc2.id_member = m.id
                WHERE mc2.id_collectivity = mc.id_collectivity
                  AND m.joined_at BETWEEN ? AND ?
            ) AS newMembersNumber,

            CASE
                WHEN COUNT(DISTINCT mc.id_member) = 0 THEN 0
                ELSE (
                    SUM(
                        CASE
                            WHEN COALESCE(p.paid, 0) >= COALESCE(d.due, 0)
                                 AND COALESCE(d.due, 0) > 0
                            THEN 1
                            ELSE 0
                        END
                    ) * 100.0
                    / COUNT(DISTINCT mc.id_member)
                )
            END AS overallMemberCurrentDuePercentage

        FROM member_collectivity mc

        LEFT JOIN (
            SELECT mf.id, mf.id_collectivity, SUM(mf.amount) AS due
            FROM membership_fee mf
            WHERE mf.status = 'ACTIVE'
              AND mf.eligible_from BETWEEN ? AND ?
            GROUP BY mf.id, mf.id_collectivity
        ) d ON mc.id_member = d.id  -- ⚠️ à améliorer si besoin

        LEFT JOIN (
            SELECT mp.id_member, mf.id_collectivity, SUM(mp.amount) AS paid
            FROM member_payment mp
            JOIN membership_fee mf ON mf.id = mp.id_membership_fee
            WHERE mf.status = 'ACTIVE'
              AND mp.creation_date BETWEEN ? AND ?
            GROUP BY mp.id_member, mf.id_collectivity
        ) p ON mc.id_member = p.id_member

        GROUP BY mc.id_collectivity
    """;

        List<CollectivityStatistic> statisticList = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));

            ps.setDate(3, Date.valueOf(from));
            ps.setDate(4, Date.valueOf(to));

            ps.setDate(5, Date.valueOf(from));
            ps.setDate(6, Date.valueOf(to));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                statisticList.add(new CollectivityStatistic(
                        rs.getString("id_collectivity"),
                        rs.getInt("newMembersNumber"),
                        rs.getDouble("overallMemberCurrentDuePercentage")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return statisticList;
    }

}
