package petcare;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class signup extends JFrame {
    private JFrame frame;
    private JPanel westPanel, centerPanel;
    private JButton signupButton, backButton;

    public signup() {
        frame = new JFrame("Pet Care Channeling Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);


        createWestPanel();
        createCenterPanel();

        frame.add(westPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);



        frame.setVisible(true);
    }

    private void createWestPanel() {
        westPanel = new JPanel();
        westPanel.setBackground(new Color(173, 216, 230));
        westPanel.setPreferredSize(new Dimension(300, 600));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\HP\\Downloads\\login123-modified.png");
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

        JLabel loginLabel = new JLabel("Sign Up");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());

        backButton = new JButton("Back");
        backButton.setBorderPainted(false);
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
        centerPanel.add(signupButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(backButton);
        centerPanel.add(Box.createVerticalGlue());

        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton.addActionListener(e -> {
            Logging loggingPage = new Logging();
            loggingPage.setVisible(true);
            frame.setVisible(false);
        });




        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);


            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/petcare", "root", "GIS001008r0529*");
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO admin (user_name, password) VALUES (?, ?)");

                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();

                pstmt.close();
                conn.close();

                JOptionPane.showMessageDialog(frame, "Details entered successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(signup::new);
    }
}
