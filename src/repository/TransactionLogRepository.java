package repository;

import model.TransactionLog;
import java.sql.*;
import java.util.*;

public class TransactionLogRepository {
    private final Connection conn;

    public TransactionLogRepository(Connection conn) {
        this.conn = conn;
    }

    public void log(int userId, String action, String details) {
        String sql = "INSERT INTO transaction_logs (user_id, action, details) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, action);
            ps.setString(3, details);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TransactionLog> getAllLogs() {
        List<TransactionLog> list = new ArrayList<>();
        String sql = "SELECT tl.*, u.username FROM transaction_logs tl LEFT JOIN users u ON tl.user_id = u.id ORDER BY tl.log_time DESC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                TransactionLog t = new TransactionLog();
                t.setId(rs.getInt("id"));
                t.setUserId(rs.getInt("user_id"));
                t.setUsername(rs.getString("username"));
                t.setAction(rs.getString("action"));
                t.setDetails(rs.getString("details"));
                t.setLogTime(rs.getTimestamp("log_time"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}