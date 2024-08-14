package petcare;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Logging extends JFrame {
    private JFrame frame;
    private JPanel westPanel, centerPanel;
    private JButton signupButton;
    private JButton loginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private String authenticateUser(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/petcare";
        String dbUsername = "root";
        String dbPassword = "GIS001008r0529*";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT * FROM admin WHERE user_name = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                ResultSet resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("user_name");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    public Logging() {


        frame = new JFrame("Pet Care Channeling Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        createWestPanel();
        createCenterPanel();

        frame.add(westPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        signupButton.addActionListener(e -> {
            signup signupPage = new signup();
            signupPage.setVisible(true);
            frame.setVisible(false);
        });

        loginButton.addActionListener(e -> {
            String enteredUsername = usernameField.getText();
            char[] enteredPassword = passwordField.getPassword();

            String adminName = String.valueOf(authenticateUser(enteredUsername, new String(enteredPassword)));
            if (adminName != null) {
                JOptionPane.showMessageDialog(frame, "Welcome, " + adminName + "!");
                new AdminDashboard(adminName).setVisible(true);
                frame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        });
    }

    private void createWestPanel() {
        westPanel = new JPanel();
        westPanel.setBackground(new Color(173, 216, 230));
        westPanel.setPreferredSize(new Dimension(300, 600));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\HP\\Downloads\\Imi1.png");
        int scaledWidth = 250;
        int scaledHeight = 250;

        Image scaledImage = imageIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledImageIcon);
        imageLabel.setPreferredSize(new Dimension(scaledWidth, scaledHeight));


        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createVerticalGlue());
        verticalBox.add(imageLabel);
        verticalBox.add(Box.createVerticalGlue());

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalGlue());
        horizontalBox.add(verticalBox);
        horizontalBox.add(Box.createHorizontalGlue());

        westPanel.add(horizontalBox);
    }



    private void createCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));


        JLabel welcomeLabel = new JLabel("Welcome to Pet Well");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginLabel = new JLabel("Login");
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(welcomeLabel);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());

        loginButton = new JButton("Login");
        loginButton.setBorderPainted(false);
        signupButton = new JButton("Sign Up");
        signupButton.setBorderPainted(false);
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(loginLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(usernameLabel);
        centerPanel.add(usernameField);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(passwordLabel);
        centerPanel.add(passwordField);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(loginButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(signupButton);
        centerPanel.add(Box.createVerticalGlue());

        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Logging::new);
    }
}