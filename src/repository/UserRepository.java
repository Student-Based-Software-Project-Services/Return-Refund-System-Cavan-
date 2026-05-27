package repository;

import model.User;
import java.sql.*;

public class UserRepository {
    private final Connection conn;

    public UserRepository(Connection conn) {
        this.conn = conn;
    }

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("role"), rs.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.util.List<User> getAllUsers() {
        java.util.List<User> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id DESC";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("role"), rs.getString("full_name"));
                u.setPassword(rs.getString("password"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, role, full_name) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getFullName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}