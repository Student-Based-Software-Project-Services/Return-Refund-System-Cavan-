package frames;

import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageUsersPanel extends JPanel {

    private final User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;

    public ManageUsersPanel(User user) {
        this.currentUser = user;
        setLayout(null);
        setBackground(new Color(15, 17, 26));
        initComponents();
        loadData();
    }

    private void initComponents() {
        JLabel title = new JLabel("Manage Users");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 18, 300, 30);
        add(title);

        JButton addBtn = createBtn("+ Add User", new Color(22, 163, 74));
        addBtn.setBounds(20, 62, 120, 32);
        addBtn.addActionListener(e -> showAddUserDialog());
        add(addBtn);

        JButton deleteBtn = createBtn("Delete User", new Color(127, 29, 29));
        deleteBtn.setBounds(148, 62, 120, 32);
        deleteBtn.addActionListener(e -> deleteUser());
        add(deleteBtn);

        String[] cols = {"ID", "Full Name", "Username", "Role"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        DashboardFrame.styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 108, 570, 345);
        DashboardFrame.styleScrollPane(scroll);
        add(scroll);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<User> users = RepoManager.getInstance().getUserRepository().getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getFullName(), u.getUsername(), u.getRole()});
        }
    }

    private void showAddUserDialog() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add User", true);
        dlg.setSize(380, 400);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(new Color(22, 26, 42));

        JLabel t = new JLabel("Add New User");
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(Color.WHITE);
        t.setBounds(20, 15, 250, 28);
        dlg.add(t);

        String[] labels = {"Full Name", "Username", "Password"};
        JTextField[] fields = new JTextField[3];
        int y = 55;
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(new Color(148, 163, 184));
            lbl.setBounds(20, y, 200, 18);
            dlg.add(lbl);
            fields[i] = (i == 2) ? new JPasswordField() : new JTextField();
            fields[i].setBackground(new Color(30, 35, 52));
            fields[i].setForeground(Color.WHITE);
            fields[i].setCaretColor(Color.WHITE);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(55, 65, 95), 1),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            fields[i].setBounds(20, y + 20, 335, 32);
            dlg.add(fields[i]);
            y += 62;
        }

        JLabel roleLbl = new JLabel("Role");
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLbl.setForeground(new Color(148, 163, 184));
        roleLbl.setBounds(20, y, 200, 18);
        dlg.add(roleLbl);

        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"STAFF", "ADMIN"});
        roleCombo.setBackground(new Color(30, 35, 52));
        roleCombo.setForeground(Color.WHITE);
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleCombo.setBounds(20, y + 20, 335, 32);
        dlg.add(roleCombo);
        y += 62;

        JButton saveBtn = createBtn("Save", new Color(22, 163, 74));
        saveBtn.setBounds(20, y, 100, 35);
        saveBtn.addActionListener(e -> {
            if (fields[0].getText().trim().isEmpty() || fields[1].getText().trim().isEmpty() || fields[2].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.");
                return;
            }
            User u = new User();
            u.setFullName(fields[0].getText().trim());
            u.setUsername(fields[1].getText().trim());
            u.setPassword(fields[2].getText().trim());
            u.setRole(roleCombo.getSelectedItem().toString());
            if (RepoManager.getInstance().getUserRepository().addUser(u)) {
                RepoManager.getInstance().getTransactionLogRepository().log(currentUser.getId(), "ADD_USER", "Added user: " + u.getUsername());
                loadData();
                dlg.dispose();
            } else {
                JOptionPane.showMessageDialog(dlg, "Failed. Username may already exist.");
            }
        });
        dlg.add(saveBtn);

        JButton cancelBtn = createBtn("Cancel", new Color(127, 29, 29));
        cancelBtn.setBounds(130, y, 90, 35);
        cancelBtn.addActionListener(e -> dlg.dispose());
        dlg.add(cancelBtn);

        dlg.setVisible(true);
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a user to delete."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        if (id == currentUser.getId()) { JOptionPane.showMessageDialog(this, "You cannot delete your own account."); return; }
        String username = tableModel.getValueAt(row, 2).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Delete user: " + username + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (RepoManager.getInstance().getUserRepository().deleteUser(id)) {
                RepoManager.getInstance().getTransactionLogRepository().log(currentUser.getId(), "DELETE_USER", "Deleted user: " + username);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed.");
            }
        }
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}