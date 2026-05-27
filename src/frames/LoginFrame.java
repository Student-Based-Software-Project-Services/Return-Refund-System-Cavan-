package frames;

import model.User;
import repository.RepoManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Return & Refund Tracking System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(15, 17, 26), getWidth(), getHeight(), new Color(22, 26, 42)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel leftPanel = new JPanel(null);
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(380, 500));

        JLabel brandIcon = new JLabel("↩");
        brandIcon.setFont(new Font("Segoe UI", Font.BOLD, 52));
        brandIcon.setForeground(new Color(99, 179, 237));
        brandIcon.setBounds(60, 100, 80, 70);
        leftPanel.add(brandIcon);

        JLabel titleLabel = new JLabel("<html>Return &<br>Refund System</html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(60, 170, 280, 70);
        leftPanel.add(titleLabel);

        JLabel subLabel = new JLabel("Track. Manage. Resolve.");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLabel.setForeground(new Color(148, 163, 184));
        subLabel.setBounds(60, 245, 260, 25);
        leftPanel.add(subLabel);

        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setForeground(new Color(45, 55, 80));
        sep.setBounds(370, 0, 2, 462);
        leftPanel.add(sep);

        JPanel rightPanel = new JPanel(null);
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(420, 500));

        JLabel loginTitle = new JLabel("Sign In");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        loginTitle.setForeground(Color.WHITE);
        loginTitle.setBounds(80, 100, 200, 35);
        rightPanel.add(loginTitle);

        JLabel loginSub = new JLabel("Enter your credentials to continue");
        loginSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginSub.setForeground(new Color(148, 163, 184));
        loginSub.setBounds(80, 135, 260, 20);
        rightPanel.add(loginSub);

        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        userLabel.setForeground(new Color(99, 179, 237));
        userLabel.setBounds(80, 185, 120, 18);
        rightPanel.add(userLabel);

        usernameField = new JTextField();
        styleField(usernameField);
        usernameField.setBounds(80, 205, 260, 38);
        rightPanel.add(usernameField);

        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        passLabel.setForeground(new Color(99, 179, 237));
        passLabel.setBounds(80, 255, 120, 18);
        rightPanel.add(passLabel);

        passwordField = new JPasswordField();
        styleField(passwordField);
        passwordField.setBounds(80, 275, 260, 38);
        rightPanel.add(passwordField);

        loginButton = new JButton("LOGIN");
        loginButton.setBounds(80, 335, 260, 42);
        loginButton.setBackground(new Color(49, 130, 206));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { loginButton.setBackground(new Color(63, 153, 225)); }
            @Override
            public void mouseExited(MouseEvent e) { loginButton.setBackground(new Color(49, 130, 206)); }
        });
        loginButton.addActionListener(e -> doLogin());
        rightPanel.add(loginButton);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin(); }
        });

        root.add(leftPanel, BorderLayout.WEST);
        root.add(rightPanel, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(30, 35, 52));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 65, 95), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        User user = RepoManager.getInstance().getUserRepository().authenticate(username, password);
        if (user != null) {
            RepoManager.getInstance().getTransactionLogRepository().log(user.getId(), "LOGIN", user.getFullName() + " logged in.");
            dispose();
            new DashboardFrame(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}