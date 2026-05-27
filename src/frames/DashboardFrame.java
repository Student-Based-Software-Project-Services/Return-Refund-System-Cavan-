package frames;

import model.User;
import repository.RepoManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends JFrame {

    private final User currentUser;
    private JPanel contentPanel;
    private JButton activeButton;

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Return & Refund Tracking System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(15, 17, 26));

        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(new Color(20, 24, 38));
        sidebar.setPreferredSize(new Dimension(185, 500));

        JLabel logo = new JLabel("↩ RR System");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logo.setForeground(new Color(99, 179, 237));
        logo.setBounds(15, 20, 160, 30);
        sidebar.add(logo);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(40, 48, 70));
        sep.setBounds(10, 58, 165, 2);
        sidebar.add(sep);

        JLabel userInfo = new JLabel("<html><b>" + currentUser.getFullName() + "</b><br><font color='#94a3b8'>" + currentUser.getRole() + "</font></html>");
        userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        userInfo.setForeground(Color.WHITE);
        userInfo.setBounds(15, 68, 160, 38);
        sidebar.add(userInfo);

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(40, 48, 70));
        sep2.setBounds(10, 112, 165, 2);
        sidebar.add(sep2);

        String[] navLabels = {"Dashboard", "Return Requests", "Customers", "Logs"};
        if (currentUser.getRole().equals("ADMIN")) {
            navLabels = new String[]{"Dashboard", "Return Requests", "Customers", "Logs", "Manage Users"};
        }

        int yPos = 130;
        for (String label : navLabels) {
            JButton btn = createNavButton(label);
            btn.setBounds(10, yPos, 165, 36);
            sidebar.add(btn);
            yPos += 44;
        }

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(10, 423, 165, 32);
        logoutBtn.setBackground(new Color(127, 29, 29));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            RepoManager.getInstance().getTransactionLogRepository().log(currentUser.getId(), "LOGOUT", currentUser.getFullName() + " logged out.");
            dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logoutBtn);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(15, 17, 26));

        root.add(sidebar, BorderLayout.WEST);
        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);

        showDashboardHome();
    }

    private JButton createNavButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(new Color(148, 163, 184));
        btn.setBackground(new Color(20, 24, 38));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) btn.setBackground(new Color(30, 35, 55));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeButton) btn.setBackground(new Color(20, 24, 38));
            }
        });
        btn.addActionListener(e -> {
            setActiveButton(btn);
            switch (label) {
                case "Dashboard" -> showDashboardHome();
                case "Return Requests" -> showPanel(new ReturnRequestPanel(currentUser));
                case "Customers" -> showPanel(new CustomerPanel(currentUser));
                case "Logs" -> showPanel(new TransactionLogPanel());
                case "Manage Users" -> showPanel(new ManageUsersPanel(currentUser));
            }
        });
        return btn;
    }

    private void setActiveButton(JButton btn) {
        if (activeButton != null) {
            activeButton.setBackground(new Color(20, 24, 38));
            activeButton.setForeground(new Color(148, 163, 184));
        }
        activeButton = btn;
        activeButton.setBackground(new Color(37, 99, 235));
        activeButton.setForeground(Color.WHITE);
    }

    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showDashboardHome() {
        JPanel home = new JPanel(null);
        home.setBackground(new Color(15, 17, 26));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBounds(25, 22, 350, 35);
        home.add(title);

        java.util.List<model.ReturnRequest> all = RepoManager.getInstance().getReturnRequestRepository().getAllRequests();
        long pending = all.stream().filter(r -> r.getRefundStatus().equals("PENDING")).count();
        long approved = all.stream().filter(r -> r.getRefundStatus().equals("APPROVED")).count();
        long completed = all.stream().filter(r -> r.getRefundStatus().equals("COMPLETED")).count();
        long rejected = all.stream().filter(r -> r.getRefundStatus().equals("REJECTED")).count();

        int[][] cards = {{(int) all.size(), 0}, {(int) pending, 1}, {(int) approved, 2}, {(int) completed, 3}};
        String[] cardLabels = {"Total Requests", "Pending Refunds", "Approved", "Completed"};
        Color[] cardColors = {new Color(37, 99, 235), new Color(202, 138, 4), new Color(22, 163, 74), new Color(99, 179, 237)};

        int cx = 25;
        for (int i = 0; i < 4; i++) {
            JPanel card = createStatCard(cardLabels[i], String.valueOf(cards[i][0]), cardColors[i]);
            card.setBounds(cx, 75, 130, 90);
            home.add(card);
            cx += 143;
        }

        JLabel recentLabel = new JLabel("Recent Return Requests");
        recentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        recentLabel.setForeground(Color.WHITE);
        recentLabel.setBounds(25, 185, 300, 25);
        home.add(recentLabel);

        String[] cols = {"Order ID", "Customer", "Product", "Return Status", "Refund Status"};
        Object[][] data = new Object[Math.min(all.size(), 5)][5];
        for (int i = 0; i < data.length; i++) {
            model.ReturnRequest r = all.get(i);
            data[i] = new Object[]{r.getOrderId(), r.getCustomerName(), r.getProductName(), r.getReturnStatus(), r.getRefundStatus()};
        }

        JTable table = new JTable(data, cols) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(25, 215, 560, 235);
        styleScrollPane(scroll);
        home.add(scroll);

        showPanel(home);
    }

    private JPanel createStatCard(String label, String value, Color accent) {
        JPanel card = new JPanel(null);
        card.setBackground(new Color(22, 26, 42));
        card.setBorder(BorderFactory.createLineBorder(new Color(40, 48, 70), 1));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 28));
        val.setForeground(accent);
        val.setBounds(12, 15, 120, 38);
        card.add(val);

        JLabel lbl = new JLabel("<html>" + label + "</html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setBounds(12, 52, 120, 28);
        card.add(lbl);

        return card;
    }

    static void styleTable(JTable table) {
        table.setBackground(new Color(22, 26, 42));
        table.setForeground(new Color(226, 232, 240));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setGridColor(new Color(40, 48, 70));
        table.setSelectionBackground(new Color(37, 99, 235));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(30, 35, 52));
        table.getTableHeader().setForeground(new Color(99, 179, 237));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setShowVerticalLines(false);
    }

    static void styleScrollPane(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(new Color(40, 48, 70), 1));
        scroll.getViewport().setBackground(new Color(22, 26, 42));
    }
}