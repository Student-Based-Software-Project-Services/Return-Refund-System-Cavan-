package frames;

import model.Customer;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {

    private final User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;

    public CustomerPanel(User user) {
        this.currentUser = user;
        setLayout(null);
        setBackground(new Color(15, 17, 26));
        initComponents();
        loadData();
    }

    private void initComponents() {
        JLabel title = new JLabel("Customer Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 18, 300, 30);
        add(title);

        JButton addBtn = createBtn("+ Add Customer", new Color(22, 163, 74));
        addBtn.setBounds(20, 62, 140, 32);
        addBtn.addActionListener(e -> showAddDialog());
        add(addBtn);

        JButton editBtn = createBtn("Edit", new Color(37, 99, 235));
        editBtn.setBounds(168, 62, 80, 32);
        editBtn.addActionListener(e -> showEditDialog());
        add(editBtn);

        JButton deleteBtn = createBtn("Delete", new Color(127, 29, 29));
        deleteBtn.setBounds(256, 62, 90, 32);
        deleteBtn.addActionListener(e -> deleteCustomer());
        add(deleteBtn);

        String[] cols = {"ID", "Name", "Email", "Phone", "Address"};
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
        List<Customer> list = RepoManager.getInstance().getCustomerRepository().getAllCustomers();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()});
        }
    }

    private void showAddDialog() {
        showCustomerDialog(null);
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a customer first."); return; }
        Customer c = new Customer(
                (int) tableModel.getValueAt(row, 0),
                tableModel.getValueAt(row, 1).toString(),
                tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "",
                tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "",
                tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : ""
        );
        showCustomerDialog(c);
    }

    private void showCustomerDialog(Customer existing) {
        boolean isEdit = existing != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isEdit ? "Edit Customer" : "Add Customer", true);
        dlg.setSize(400, 400);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(new Color(22, 26, 42));

        JLabel t = new JLabel(isEdit ? "Edit Customer" : "Add Customer");
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(Color.WHITE);
        t.setBounds(20, 15, 250, 28);
        dlg.add(t);

        String[] labels = {"Full Name", "Email", "Phone", "Address"};
        String[] defaults = isEdit ? new String[]{existing.getName(), existing.getEmail(), existing.getPhone(), existing.getAddress()} : new String[]{"", "", "", ""};
        JTextField[] fields = new JTextField[4];
        int y = 55;
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(new Color(148, 163, 184));
            lbl.setBounds(20, y, 200, 18);
            dlg.add(lbl);
            fields[i] = new JTextField(defaults[i]);
            fields[i].setBackground(new Color(30, 35, 52));
            fields[i].setForeground(Color.WHITE);
            fields[i].setCaretColor(Color.WHITE);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(55, 65, 95), 1),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            fields[i].setBounds(20, y + 20, 355, 32);
            dlg.add(fields[i]);
            y += 62;
        }

        JButton saveBtn = createBtn(isEdit ? "Update" : "Save", new Color(22, 163, 74));
        saveBtn.setBounds(20, y + 5, 110, 35);
        saveBtn.addActionListener(e -> {
            if (fields[0].getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dlg, "Name is required."); return; }
            Customer c = new Customer(isEdit ? existing.getId() : 0, fields[0].getText().trim(),
                    fields[1].getText().trim(), fields[2].getText().trim(), fields[3].getText().trim());
            boolean result = isEdit ? RepoManager.getInstance().getCustomerRepository().updateCustomer(c)
                    : RepoManager.getInstance().getCustomerRepository().addCustomer(c);
            if (result) {
                RepoManager.getInstance().getTransactionLogRepository().log(currentUser.getId(),
                        isEdit ? "UPDATE_CUSTOMER" : "ADD_CUSTOMER", "Customer: " + c.getName());
                loadData();
                dlg.dispose();
            } else {
                JOptionPane.showMessageDialog(dlg, "Operation failed.");
            }
        });
        dlg.add(saveBtn);

        JButton cancelBtn = createBtn("Cancel", new Color(127, 29, 29));
        cancelBtn.setBounds(140, y + 5, 90, 35);
        cancelBtn.addActionListener(e -> dlg.dispose());
        dlg.add(cancelBtn);

        dlg.setVisible(true);
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a customer first."); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = tableModel.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Delete customer: " + name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (RepoManager.getInstance().getCustomerRepository().deleteCustomer(id)) {
                RepoManager.getInstance().getTransactionLogRepository().log(currentUser.getId(), "DELETE_CUSTOMER", "Deleted: " + name);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed. Customer may have active return requests.");
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