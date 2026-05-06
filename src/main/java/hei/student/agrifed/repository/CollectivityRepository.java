package hei.student.agrifed.repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hei.student.agrifed.entity.*;
import hei.student.agrifed.entity.dto.CollectivityLocalStatisticsDto;
import hei.student.agrifed.entity.dto.CreateCollectivityDto;
import hei.student.agrifed.entity.dto.CreateCollectivityStructureDto;
import hei.student.agrifed.entity.dto.MemberDescriptionDto;
import hei.student.agrifed.entity.enums.*;
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

    public MembershipFee saveMembershipFee(String id_collectivity, MembershipFee membershipFee) {
        String insertMembershipFeeSql = """
                INSERT INTO membership_fee (eligible_from, frequency, amount, label, status, id_collectivity)
                VALUES (?, ?::frequency, ?, ?, ?::status, ?)
                RETURNING id, eligible_from, frequency, amount, label, status;
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(insertMembershipFeeSql);
            ps.setObject(1, membershipFee.getEligibleFrom());
            ps.setString(2, membershipFee.getFrequency().toString());
            ps.setDouble(3, membershipFee.getAmount());
            ps.setString(4, membershipFee.getLabel());
            ps.setString(5, membershipFee.getStatus().toString());
            ps.setString(6, id_collectivity);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MembershipFee membershipFeeToSave = new MembershipFee();
                membershipFeeToSave.setId(rs.getString("id"));
                membershipFeeToSave.setEligibleFrom(rs.getDate("eligible_from").toLocalDate());
                membershipFeeToSave.setFrequency(Frequency.valueOf(rs.getString("frequency")));
                membershipFeeToSave.setAmount(rs.getDouble("amount"));
                membershipFeeToSave.setLabel(rs.getString("label"));
                membershipFeeToSave.setStatus(ActivityStatus.valueOf(rs.getString("status")));
                return membershipFeeToSave;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            cPs.setString(2, s.getPresident());
            cPs.setString(3, s.getTreasurer());
            cPs.setString(4, s.getVicePresident());
            cPs.setString(5, s.getSecretary());

            ResultSet rs = cPs.executeQuery();
            if (!rs.next()) throw new RuntimeException("Fail insertion collectitivy");
            String collectivityId = rs.getString("id");

            PreparedStatement mcPs = connection.prepareStatement(insertMemberCollectivitySql);
            for (String memberId : dto.getMembers()) {
                mcPs.setString(1, memberId);
                mcPs.setString(2, collectivityId);
                mcPs.executeUpdate();
            }

            return buildResponse(collectivityId, dto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Collectivity> findById(String id){
        String sql = """
                 SELECT c.id, c.federation_number, c.name, c.location,
                       c.president_id, c.vice_president_id, c.treasurer_id, c.secretary_id
                FROM collectivity c
                WHERE c.id = ?
                """;
        try {
PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();

            Collectivity c = new Collectivity();
            c.setId(rs.getString("id"));
            c.setNumber(rs.getObject("federation_number") != null
                    ? rs.getInt("federation_number") : null);
            c.setName(rs.getString("name"));
            c.setLocation(rs.getString("location"));

            // Structure
            CollectivityStructure structure = new CollectivityStructure(
                    fetchMember(rs.getString("president_id"),      "Président"),
                    fetchMember(rs.getString("vice_president_id"), "Vice-president"),
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

    public Collectivity assignIdentity(String id, Integer number, String name) {
        String sql = """
                UPDATE collectivity
                SET federation_number = COALESCE(?, federation_number),
                    name              = COALESCE(?, name)
                WHERE id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (number != null) ps.setInt(1, number);
            else ps.setNull(1, java.sql.Types.INTEGER);
            if (name != null) ps.setString(2, name);
            else ps.setNull(2, java.sql.Types.VARCHAR);
            ps.setString(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findById(id).orElseThrow(() ->
                new NotFoundException("Collectivity not found with id : " + id));
    }


    private List<Member> findMembersByCollectivityId(String collectivityId) {
        String sql = """
                SELECT m.id FROM member_collectivity mc
                JOIN member m ON m.id = mc.id_member
                WHERE mc.id_collectivity = ?
                """;
        List<Member> members = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, collectivityId);
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

        return Collectivity.builder()
                .id(collectivityId)
                .location(dto.getLocation())
                .structure(structure)
                .members(members)
                .build();
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
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("c") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<MembershipFee> findMembershipFeesById(String id) {
        String sql = """
                SELECT id, eligible_from, frequency, amount, label, status
                FROM membership_fee
                WHERE id_collectivity = ?;
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            List<MembershipFee> fees = new ArrayList<>();
            while (rs.next()) {
                fees.add(MembershipFee.builder()
                        .id(rs.getString("id"))
                        .eligibleFrom(rs.getDate("eligible_from").toLocalDate())
                        .frequency(Frequency.valueOf(rs.getString("frequency")))
                        .amount(rs.getDouble("amount"))
                        .label(rs.getString("label"))
                        .status(ActivityStatus.valueOf(rs.getString("status")))
                        .build());
            }
            return fees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CollectivityTransaction> findTransactions(String collectivityId,
                                                          LocalDate from, LocalDate to) {
        String sql = """
                SELECT ct.id, ct.creation_date, ct.amount, ct.payment_mode,
                       ct.id_member, ct.id_financial_account,
                       fa.account_type, fa.amount AS fa_amount,
                       fa.holder_name, fa.mobile_banking_service, fa.mobile_number,
                       fa.bank_name, fa.bank_code, fa.bank_branch_code,
                       fa.bank_account_number, fa.bank_account_key
                FROM collectivity_transaction ct
                JOIN financial_account fa ON fa.id = ct.id_financial_account
                WHERE ct.id_collectivity = ?
                  AND ct.creation_date >= ?
                  AND ct.creation_date <= ?
                ORDER BY ct.creation_date
                """;

        List<CollectivityTransaction> results = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                FinancialAccount fa = mapFinancialAccount(rs, "id_financial_account", "fa_amount");

                // Member debited
                Member member = memberRepository.findById(rs.getString("id_member")).orElse(null);

                results.add(CollectivityTransaction.builder()
                        .id(rs.getString("id"))
                        .creationDate(rs.getDate("creation_date").toLocalDate())
                        .amount(rs.getDouble("amount"))
                        .paymentMode(PaymentMode.valueOf(rs.getString("payment_mode")))
                        .accountCredited(fa)
                        .memberDebited(member)
                        .build());
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return results;
    }

    public List<String> findDistinctAccountIdsByCollectivity(String collectivityId) {
        String sql = """
                SELECT DISTINCT id_financial_account
                FROM collectivity_transaction
                WHERE id_collectivity = ?
                """;
        List<String> ids = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) ids.add(rs.getString("id_financial_account"));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return ids;
    }

    public Optional<FinancialAccount> findFinancialAccountById(String accountId) {
        String sql = """
                SELECT id, account_type, amount,
                       holder_name, mobile_banking_service, mobile_number,
                       bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key
                FROM financial_account WHERE id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapFinancialAccount(rs, "id", "amount"));
            return Optional.empty();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public Double sumTransactionAmountByAccountAt(String collectivityId, String accountId, LocalDate at) {
        String sql = """
                SELECT COALESCE(SUM(amount), 0) AS balance
                FROM collectivity_transaction
                WHERE id_collectivity = ?
                  AND id_financial_account = ?
                  AND creation_date <= ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, collectivityId);
            ps.setString(2, accountId);
            ps.setDate(3, Date.valueOf(at));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0.0;
    }

    private FinancialAccount mapFinancialAccount(ResultSet rs, String idCol, String amountCol)
            throws SQLException {
        String accountType = rs.getString("account_type");
        String id = rs.getString(idCol);

        if ("CASH".equals(accountType)) {
            return CashAccount.builder()
                    .id(id)
                    .amount(rs.getObject(amountCol, Integer.class))
                    .build();
        } else if ("MOBILE_BANKING".equals(accountType)) {
            return MobileBankingAccount.builder()
                    .id(id)
                    .holderName(rs.getString("holder_name"))
                    .mobileBankingService(rs.getString("mobile_banking_service") != null
                            ? MobileBankingService.valueOf(rs.getString("mobile_banking_service")) : null)
                    .mobileNumber(rs.getObject("mobile_number", Integer.class))
                    .amount(rs.getDouble(amountCol))
                    .build();
        } else if ("BANK".equals(accountType)) {
            return BankAccount.builder()
                    .id(id)
                    .holderName(rs.getString("holder_name"))
                    .bankName(rs.getString("bank_name") != null
                            ? Bank.valueOf(rs.getString("bank_name")) : null)
                    .bankCode(rs.getObject("bank_code", Integer.class))
                    .bankBranchCode(rs.getObject("bank_branch_code", Integer.class))
                    .bankAccountNumber(rs.getObject("bank_account_number", Integer.class))
                    .bankAccountKey(rs.getObject("bank_account_key", Integer.class))
                    .amount(rs.getDouble(amountCol))
                    .build();
        }
        throw new RuntimeException("Unknown account type: " + accountType);
    }

    public List<CollectivityLocalStatisticsDto> findStatistics(String collectivityId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                    m.id,
                    m.firstname,
                    m.lastname,
                    m.email,
                    m.occupation,
                    COALESCE(SUM(mp.amount), 0) as earned_amount,
                    COALESCE(
                        SUM(
                            CASE
                                WHEN mf.frequency = 'WEEKLY' THEN mf.amount * ((?::date - ?::date) / 7.0)
                                WHEN mf.frequency = 'MONTHLY' THEN mf.amount * ((?::date - ?::date) / 30.0)
                                WHEN mf.frequency = 'ANNUALLY' THEN mf.amount * ((?::date - ?::date) / 365.0)
                                WHEN mf.frequency = 'PUNCTUALLY' AND mf.eligible_from BETWEEN ? AND ? THEN mf.amount
                                ELSE 0
                            END
                        ) - COALESCE(SUM(mp.amount), 0),
                        0
                    ) as unpaid_amount
                FROM member m
                INNER JOIN member_collectivity mc ON m.id = mc.id_member AND mc.id_collectivity = ?
                LEFT JOIN member_payment mp ON m.id = mp.id_member
                    AND mp.creation_date BETWEEN ? AND ?
                LEFT JOIN membership_fee mf ON mf.id_collectivity = ? AND mf.status = 'ACTIVE'
                GROUP BY m.id, m.firstname, m.lastname, m.email, m.occupation
                """;

        List<CollectivityLocalStatisticsDto> results = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setDate(1, Date.valueOf(to));
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ps.setDate(4, Date.valueOf(from));
            ps.setDate(5, Date.valueOf(to));
            ps.setDate(6, Date.valueOf(from));
            ps.setDate(7, Date.valueOf(from));
            ps.setDate(8, Date.valueOf(to));
            ps.setString(9, collectivityId);
            ps.setDate(10, Date.valueOf(from));
            ps.setDate(11, Date.valueOf(to));
            ps.setString(12, collectivityId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MemberDescriptionDto memberDesc = new MemberDescriptionDto();
                memberDesc.setId(rs.getString("id"));
                memberDesc.setFirstName(rs.getString("firstname"));
                memberDesc.setLastName(rs.getString("lastname"));
                memberDesc.setEmail(rs.getString("email"));
                memberDesc.setOccupation(rs.getString("occupation") != null
                        ? MemberOccupation.valueOf(rs.getString("occupation")) : null);

                CollectivityLocalStatisticsDto stat = new CollectivityLocalStatisticsDto();
                stat.setMemberDescription(memberDesc);
                stat.setEarnedAmount(rs.getDouble("earned_amount"));
                stat.setUnpaidAmount(rs.getDouble("unpaid_amount"));

                results.add(stat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

}
