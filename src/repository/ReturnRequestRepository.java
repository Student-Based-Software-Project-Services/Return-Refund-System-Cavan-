package repository;

import model.ReturnRequest;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class ReturnRequestRepository {
    private final Connection conn;

    public ReturnRequestRepository(Connection conn) {
        this.conn = conn;
    }

    public List<ReturnRequest> getAllRequests() {
        List<ReturnRequest> list = new ArrayList<>();
        String sql = "SELECT rr.*, c.name AS customer_name, u.full_name AS handled_by_name " +
                "FROM return_requests rr " +
                "JOIN customers c ON rr.customer_id = c.id " +
                "LEFT JOIN users u ON rr.handled_by = u.id " +
                "ORDER BY rr.created_at DESC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ReturnRequest> searchRequests(String keyword) {
        List<ReturnRequest> list = new ArrayList<>();
        String sql = "SELECT rr.*, c.name AS customer_name, u.full_name AS handled_by_name " +
                "FROM return_requests rr " +
                "JOIN customers c ON rr.customer_id = c.id " +
                "LEFT JOIN users u ON rr.handled_by = u.id " +
                "WHERE rr.order_id LIKE ? OR c.name LIKE ? OR rr.product_name LIKE ? " +
                "ORDER BY rr.created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addRequest(ReturnRequest r) {
        String sql = "INSERT INTO return_requests (order_id, customer_id, product_name, return_reason, return_date, handled_by) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getOrderId());
            ps.setInt(2, r.getCustomerId());
            ps.setString(3, r.getProductName());
            ps.setString(4, r.getReturnReason());
            ps.setDate(5, r.getReturnDate());
            ps.setInt(6, r.getHandledBy());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatuses(int id, String returnStatus, String refundStatus, BigDecimal refundAmount) {
        String sql = "UPDATE return_requests SET return_status=?, refund_status=?, refund_amount=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, returnStatus);
            ps.setString(2, refundStatus);
            ps.setBigDecimal(3, refundAmount);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRequest(int id) {
        String sql = "DELETE FROM return_requests WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ReturnRequest mapRow(ResultSet rs) throws SQLException {
        ReturnRequest r = new ReturnRequest();
        r.setId(rs.getInt("id"));
        r.setOrderId(rs.getString("order_id"));
        r.setCustomerId(rs.getInt("customer_id"));
        r.setCustomerName(rs.getString("customer_name"));
        r.setProductName(rs.getString("product_name"));
        r.setReturnReason(rs.getString("return_reason"));
        r.setReturnDate(rs.getDate("return_date"));
        r.setReturnStatus(rs.getString("return_status"));
        r.setRefundStatus(rs.getString("refund_status"));
        r.setRefundAmount(rs.getBigDecimal("refund_amount"));
        r.setHandledBy(rs.getInt("handled_by"));
        r.setHandledByName(rs.getString("handled_by_name"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        r.setUpdatedAt(rs.getTimestamp("updated_at"));
        return r;
    }
}