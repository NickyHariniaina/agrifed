package hei.student.agrifed.repository;

import hei.student.agrifed.entity.Bank;
import hei.student.agrifed.entity.FinancialAccount;
import hei.student.agrifed.entity.MobileBankingService;
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

    public Optional<FinancialAccount> findFinancialAccount(Integer accountId) {
        String sql = """
                SELECT id, account_type, amount,
                       holder_name, mobile_banking_service, mobile_number,
                       bank_name, bank_code, bank_branch_code, bank_account_number, bank_account_key
                FROM financial_account WHERE id = ?
                """;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapFinancialAccount(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private FinancialAccount mapFinancialAccount(ResultSet rs) throws SQLException {
        FinancialAccount fa = new FinancialAccount();
        fa.setId(rs.getInt("id"));
        fa.setAccountType(rs.getString("account_type"));
        fa.setAmount(rs.getDouble("amount"));

        String mobileSvc = rs.getString("mobile_banking_service");
        if (mobileSvc != null) fa.setMobileBankingService(MobileBankingService.valueOf(mobileSvc));

        fa.setHolderName(rs.getString("holder_name"));
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

        return fa;
    }

}
