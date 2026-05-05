package hei.student.agrifed.repository;

import hei.student.agrifed.entity.*;
import hei.student.agrifed.entity.enums.Bank;
import hei.student.agrifed.entity.enums.MobileBankingService;
import hei.student.agrifed.entity.enums.PaymentMode;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class MemberPaymentRepository {
    private final Connection connection;

    public MemberPaymentRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<String> findCollectivityIdByMembershipFee(String membershipFeeId) {
        String sql = "SELECT id_collectivity FROM membership_fee WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, membershipFeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(rs.getString("id_collectivity"));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<FinancialAccount> findFinancialAccount(String accountId) {
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
            if (rs.next()) return Optional.of(mapFinancialAccount(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MemberPayment save(String memberId, String membershipFeeId,
                              String accountId, String collectivityId,
                              Integer amount, PaymentMode paymentMode) {
        String insertPaymentSql = """
                INSERT INTO member_payment
                    (id_member, id_membership_fee, id_financial_account, amount, payment_mode, creation_date)
                VALUES (?, ?, ?, ?, ?::payment_mode, current_date)
                RETURNING id, amount, payment_mode, creation_date
                """;

        String insertTransactionSql = """
                INSERT INTO collectivity_transaction
                    (id_collectivity, id_member, id_financial_account, amount, payment_mode, creation_date)
                VALUES (?, ?, ?, ?, ?::payment_mode, current_date)
                """;
        try {
            PreparedStatement pPs = connection.prepareStatement(insertPaymentSql);
            pPs.setString(1, memberId);
            pPs.setString(2, membershipFeeId);
            pPs.setString(3, accountId);
            pPs.setInt(4, amount);
            pPs.setString(5, paymentMode.name());
            ResultSet rs = pPs.executeQuery();
            if (!rs.next()) throw new RuntimeException("Failed to insert member_payment");

            String paymentId    = rs.getString("id");
            Integer savedAmount  = rs.getInt("amount");
            PaymentMode savedMode = PaymentMode.valueOf(rs.getString("payment_mode"));
            LocalDate savedDate = rs.getDate("creation_date").toLocalDate();

            PreparedStatement tPs = connection.prepareStatement(insertTransactionSql);
            tPs.setString(1, collectivityId);
            tPs.setString(2, memberId);
            tPs.setString(3, accountId);
            tPs.setInt(4, amount);
            tPs.setString(5, paymentMode.name());
            tPs.executeUpdate();

            FinancialAccount account = findFinancialAccount(accountId).orElse(null);
            return MemberPayment.builder()
                    .id(paymentId)
                    .amount(savedAmount)
                    .paymentMode(savedMode)
                    .accountCredited(account)
                    .creationDate(savedDate)
                    .build();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private FinancialAccount mapFinancialAccount(ResultSet rs) throws SQLException {
        String accountType = rs.getString("account_type");
        String id = rs.getString("id");

        if ("CASH".equals(accountType)) {
            return CashAccount.builder()
                    .id(id)
                    .amount(rs.getInt("amount"))
                    .build();
        } else if ("MOBILE_BANKING".equals(accountType)) {
            return MobileBankingAccount.builder()
                    .id(id)
                    .holderName(rs.getString("holder_name"))
                    .mobileBankingService(rs.getString("mobile_banking_service") != null
                            ? MobileBankingService.valueOf(rs.getString("mobile_banking_service")) : null)
                    .mobileNumber(rs.getObject("mobile_number", Integer.class))
                    .amount(rs.getDouble("amount"))
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
                    .amount(rs.getDouble("amount"))
                    .build();
        }
        throw new RuntimeException("Unknown account type: " + accountType);
    }

}
