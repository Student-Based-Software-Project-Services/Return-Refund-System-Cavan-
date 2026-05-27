package frames;

import model.TransactionLog;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionLogPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public TransactionLogPanel() {
        setLayout(null);
        setBackground(new Color(15, 17, 26));
        initComponents();
        loadData();
    }

    private void initComponents() {
        JLabel title = new JLabel("Transaction Logs");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 18, 300, 30);
        add(title);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(20, 60, 100, 32);
        refreshBtn.setBackground(new Color(37, 99, 235));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadData());
        add(refreshBtn);

        String[] cols = {"ID", "Username", "Action", "Details", "Timestamp"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        DashboardFrame.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 105, 570, 350);
        DashboardFrame.styleScrollPane(scroll);
        add(scroll);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<TransactionLog> logs = RepoManager.getInstance().getTransactionLogRepository().getAllLogs();
        for (TransactionLog l : logs) {
            tableModel.addRow(new Object[]{l.getId(), l.getUsername(), l.getAction(), l.getDetails(), l.getLogTime()});
        }
    }
}