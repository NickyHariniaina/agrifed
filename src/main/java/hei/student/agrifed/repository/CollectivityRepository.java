package hei.student.agrifed.repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hei.student.agrifed.entity.*;
import hei.student.agrifed.entity.dto.CreateCollectivityDto;
import hei.student.agrifed.entity.dto.CreateCollectivityStructureDto;
import hei.student.agrifed.entity.dto.CreateMembershipFeeDto;
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
                ps.setInt(i + 1, Integer.parseInt(memberIds.get(i)));
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public MembershipFee saveMembershipFee(Integer id_collectivity, MembershipFee membershipFee) {
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
            ps.setInt(6, id_collectivity);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MembershipFee membershipFeeToSave = new MembershipFee();
                membershipFeeToSave.setId(rs.getInt("id"));
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

    public List<MembershipFee> findMembershipFeesById(Integer id) {
        String sql = """
                SELECT id, eligible_from, frequency, amount, label, status
                FROM membership_fee
                WHERE id_collectivity = ?;
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<MembershipFee> fees = new ArrayList<>();
            while (rs.next()) {
                fees.add(MembershipFee.builder()
                        .id(rs.getInt("id"))
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

    public List<CollectivityTransaction> findTransactions(Integer collectivityId,
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
            ps.setInt(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                // Financial account credited
                FinancialAccount fa = new FinancialAccount();
                fa.setId(rs.getInt("id_financial_account"));
                fa.setAccountType(rs.getString("account_type"));
                fa.setAmount(rs.getDouble("fa_amount"));
                fa.setHolderName(rs.getString("holder_name"));
                String mobileSvc = rs.getString("mobile_banking_service");
                if (mobileSvc != null) fa.setMobileBankingService(MobileBankingService.valueOf(mobileSvc));

                long mobileNum = rs.getLong("mobile_number");
                if (!rs.wasNull()) fa.setMobileNumber(mobileNum);
                String bank = rs.getString("bank_name");
                if (bank != null) fa.setBankName(Bank.valueOf(bank));
                int bankCode = rs.getInt("bank_code");
                if (!rs.wasNull()) fa.setBankCode(bankCode);
                int branchCode = rs.getInt("bank_branch_code");
                if (!rs.wasNull()) fa.setBankBranchCode(branchCode);
                long bankAccNum = rs.getLong("bank_account_number");
                if (!rs.wasNull()) fa.setBankAccountNumber(bankAccNum);
                int bankAccKey = rs.getInt("bank_account_key");
                if (!rs.wasNull()) fa.setBankAccountKey(bankAccKey);

                // Member debited
                Member member = memberRepository.findById(rs.getString("id_member")).orElse(null);

                results.add(CollectivityTransaction.builder()
                        .id(rs.getInt("id"))
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


}
