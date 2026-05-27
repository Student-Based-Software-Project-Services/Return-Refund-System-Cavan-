package repository;

import model.Customer;
import java.sql.*;
import java.util.*;

public class CustomerRepository {
    private final Connection conn;

    public CustomerRepository(Connection conn) {
        this.conn = conn;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY name ASC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Customer(rs.getInt("id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("phone"), rs.getString("address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO customers (name, email, phone, address) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE customers SET name=?, email=?, phone=?, address=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getAddress());
            ps.setInt(5, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}