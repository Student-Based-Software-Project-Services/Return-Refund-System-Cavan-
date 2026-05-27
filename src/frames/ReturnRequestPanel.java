package frames;

import model.Customer;
import model.ReturnRequest;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class ReturnRequestPanel extends JPanel {

    private final User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ReturnRequestPanel(User user) {
        this.currentUser = user;
        setLayout(null);
        setBackground(new Color(15, 17, 26));
        initComponents();
        loadData(null);
    }

    private void initComponents() {
        JLabel title = new JLabel("Return Request Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 18, 350, 30);
        add(title);

        searchField = new JTextField();
        searchField.setBackground(new Color(30, 35, 52));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 65, 95), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        searchField.setBounds(20, 60, 150, 32);
        add(searchField);

        JButton searchBtn = createBtn("Search", new Color(37, 99, 235));
        searchBtn.setBounds(178, 60, 80, 32);
        searchBtn.addActionListener(e -> loadData(searchField.getText().trim()));
        add(searchBtn);

        JButton clearBtn = createBtn("Clear", new Color(55, 65, 95));
        clearBtn.setBounds(265, 60, 70, 32);
        clearBtn.addActionListener(e -> { searchField.setText(""); loadData(null); });
        add(clearBtn);

        JButton addBtn = createBtn("+ New Request", new Color(22, 163, 74));
        addBtn.setBounds(342, 60, 121, 32);
        addBtn.addActionListener(e -> showAddDialog());
        add(addBtn);

        JButton updateBtn = createBtn("Update Status", new Color(202, 138, 4));
        updateBtn.setBounds(470, 60, 120, 32);
        updateBtn.addActionListener(e -> showUpdateDialog());
        add(updateBtn);

        String[] cols = {"ID", "Order ID", "Customer", "Product", "Return Date", "Return Status", "Refund Status", "Amount"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        DashboardFrame.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(85);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 105, 570, 345);
        DashboardFrame.styleScrollPane(scroll);
        add(scroll);
    }

    private void loadData(String keyword) {
        tableModel.setRowCount(0);
        List<ReturnRequest> list = (keyword == null || keyword.isEmpty())
                ? RepoManager.getInstance().getReturnRequestRepository().getAllRequests()
                : RepoManager.getInstance().getReturnRequestRepository().searchRequests(keyword);
        for (ReturnRequest r : list) {
            tableModel.addRow(new Object[]{
                    r.getId(), r.getOrderId(), r.getCustomerName(), r.getProductName(),
                    r.getReturnDate(), r.getReturnStatus(), r.getRefundStatus(),
                    r.getRefundAmount() != null ? "₱" + r.getRefundAmount() : "₱0.00"
            });
        }
    }

    private void showAddDialog() {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Return Request", true);
        dlg.setSize(440, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(new Color(22, 26, 42));

        JLabel t = new JLabel("New Return Request");
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(Color.WHITE);
        t.setBounds(20, 15, 300, 28);
        dlg.add(t);

        String[] fieldNames = {"Order ID", "Product Name"};
        JTextField[] fields = new JTextField[2];
        int y = 55;
        for (int i = 0; i < fieldNames.length; i++) {
            JLabel lbl = new JLabel(fieldNames[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(new Color(148, 163, 184));
            lbl.setBounds(20, y, 200, 18);
            dlg.add(lbl);
            fields[i] = new JTextField();
            styleDialogField(fields[i]);
            fields[i].setBounds(20, y + 20, 395, 32);
            dlg.add(fields[i]);
            y += 65;
        }

        JLabel custLbl = new JLabel("Customer");
        custLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        custLbl.setForeground(new Color(148, 163, 184));
        custLbl.setBounds(20, y, 200, 18);
        dlg.add(custLbl);

        JComboBox<Customer> custCombo = new JComboBox<>();
        custCombo.setBackground(new Color(30, 35, 52));
        custCombo.setForeground(Color.WHITE);
        custCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        custCombo.setBounds(20, y + 20, 395, 32);
        List<Customer> customers = RepoManager.getInstance().getCustomerRepository().getAllCustomers();
        for (Customer c : customers) custCombo.addItem(c);
        dlg.add(custCombo);
        y += 65;

        JLabel reasonLbl = new JLabel("Return Reason");
        reasonLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonLbl.setForeground(new Color(148, 163, 184));
        reasonLbl.setBounds(20, y, 200, 18);
        dlg.add(reasonLbl);

        JTextField reasonField = new JTextField();
        styleDialogField(reasonField);
        reasonField.setBounds(20, y + 20, 395, 32);
        dlg.add(reasonField);
        y += 65;

        JButton saveBtn = createBtn("Save Request", new Color(22, 163, 74));
        saveBtn.setBounds(20, y + 10, 150, 36);
        saveBtn.addActionListener(e -> {
            if (fields[0].getText().trim().isEmpty() || fields[1].getText().trim().isEmpty() || reasonField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Please fill in all fields.");
                return;
            }
            Customer selectedCustomer = (Customer) custCombo.getSelectedItem();
            if (selectedCustomer == null) { JOptionPane.showMessageDialog(dlg, "No customers available."); return; }
            ReturnRequest r = new ReturnRequest();
            r.setOrderId(fields[0].getText().trim());
            r.setProductName(fields[1].getText().trim());
            r.setCustomerId(selectedCustomer.getId());
            r.setReturnReason(reasonField.getText().trim());
            r.setReturnDate(new Date(System.currentTimeMillis()));
            r.setHandledBy(currentUser.getId());
            if (RepoManager.getInstance().getReturnRequestRepository().addRequest(r)) {
                RepoManager.getInstance().getTransactionLogRepository().log(currentUser.getId(), "ADD_REQUEST",
                        "Added return request for Order ID: " + r.getOrderId());
                loadData(null);
                dlg.dispose();
            } else {
                JOptionPane.showMessageDialog(dlg, "Failed to add request.");
            }
        });
        dlg.add(saveBtn);

        JButton cancelBtn = createBtn("Cancel", new Color(127, 29, 29));
        cancelBtn.setBounds(180, y + 10, 100, 36);
        cancelBtn.addActionListener(e -> dlg.dispose());
        dlg.add(cancelBtn);

        dlg.setVisible(true);
    }

    private void showUpdateDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a request."); return; }
        int id = (int) tableModel.getValueAt(row, 0);

        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Update Status", true);
        dlg.setSize(380, 325);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(new Color(22, 26, 42));

        JLabel t = new JLabel("Update Return #" + id);
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(Color.WHITE);
        t.setBounds(20, 15, 300, 25);
        dlg.add(t);

        JLabel rsLbl = new JLabel("Return Status");
        rsLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rsLbl.setForeground(new Color(148, 163, 184));
        rsLbl.setBounds(20, 55, 160, 18);
        dlg.add(rsLbl);

        JComboBox<String> returnStatusCombo = new JComboBox<>(new String[]{"REQUESTED", "UNDER_REVIEW", "ITEM_RECEIVED", "REFUND_SENT"});
        returnStatusCombo.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        returnStatusCombo.setBackground(new Color(30, 35, 52));
        returnStatusCombo.setForeground(Color.WHITE);
        returnStatusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        returnStatusCombo.setBounds(20, 75, 330, 32);
        dlg.add(returnStatusCombo);

        JLabel rfLbl = new JLabel("Refund Status");
        rfLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rfLbl.setForeground(new Color(148, 163, 184));
        rfLbl.setBounds(20, 118, 160, 18);
        dlg.add(rfLbl);

        JComboBox<String> refundStatusCombo = new JComboBox<>(new String[]{"PENDING", "APPROVED", "REJECTED", "COMPLETED"});
        refundStatusCombo.setSelectedItem(tableModel.getValueAt(row, 6).toString());
        refundStatusCombo.setBackground(new Color(30, 35, 52));
        refundStatusCombo.setForeground(Color.WHITE);
        refundStatusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refundStatusCombo.setBounds(20, 138, 330, 32);
        dlg.add(refundStatusCombo);

        JLabel amtLbl = new JLabel("Refund Amount (₱)");
        amtLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        amtLbl.setForeground(new Color(148, 163, 184));
        amtLbl.setBounds(20, 181, 160, 18);
        dlg.add(amtLbl);

        JTextField amtField = new JTextField(tableModel.getValueAt(row, 7).toString().replace("₱", ""));
        styleDialogField(amtField);
        amtField.setBounds(20, 200, 200, 32);
        dlg.add(amtField);

        JButton saveBtn = createBtn("Update", new Color(37, 99, 235));
        saveBtn.setBounds(20, 242, 110, 34);
        saveBtn.addActionListener(e -> {
            try {
                BigDecimal amount = new BigDecimal(amtField.getText().trim().isEmpty() ? "0" : amtField.getText().trim());
                String rs = returnStatusCombo.getSelectedItem().toString();
                String rfs = refundStatusCombo.getSelectedItem().toString();
                if (RepoManager.getInstance().getReturnRequestRepository().updateStatuses(id, rs, rfs, amount)) {
                    RepoManager.getInstance().getTransactionLogRepository().log(currentUser.getId(), "UPDATE_REQUEST",
                            "Updated request #" + id + " -> Return: " + rs + ", Refund: " + rfs);
                    loadData(null);
                    dlg.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Invalid amount.");
            }
        });
        dlg.add(saveBtn);

        JButton cancelBtn = createBtn("Cancel", new Color(127, 29, 29));
        cancelBtn.setBounds(140, 242, 90, 34);
        cancelBtn.addActionListener(e -> dlg.dispose());
        dlg.add(cancelBtn);

        dlg.setVisible(true);
    }

    private void styleDialogField(JTextField field) {
        field.setBackground(new Color(30, 35, 52));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 65, 95), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
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