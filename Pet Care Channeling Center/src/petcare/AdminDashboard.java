package petcare;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private String adminName;

    public AdminDashboard(String adminName) {
        this.adminName = adminName;
        initializeUI();


    }

    private void initializeUI() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(  180, 187, 201 ));
        JLabel headerLabel = new JLabel("Welcome, " + adminName);
        headerPanel.add(headerLabel);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(173, 216, 230));
        GridBagLayout gridBagLayout = new GridBagLayout();
        sidebarPanel.setLayout(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 10, 5, 10);


        constraints.weighty = 1.0;


        JButton homeButton = new JButton("Home");
        constraints.weighty = 0.0;
        gridBagLayout.setConstraints(homeButton, constraints);
        sidebarPanel.add(homeButton);

        homeButton.setBorderPainted(false);

        homeButton.addActionListener(e -> {
            Home homePage = new Home();
            homePage.setVisible(true);
            setVisible(false);


        });


        JButton appointmentsButton = new JButton("Appointments");
        constraints.weighty = 0.0;
        gridBagLayout.setConstraints(appointmentsButton, constraints);
        sidebarPanel.add(appointmentsButton);
        appointmentsButton.setBorderPainted(false);

        appointmentsButton.addActionListener(e -> {
            JFrame frame = new JFrame("Appointment Viewer");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            ViewAppointment appointmentView = new ViewAppointment();
            frame.add(appointmentView);

            int width = 1000;
            int height = 600;
            frame.setSize(width, height);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            setVisible(false);
        });


        JButton managePetsButton = new JButton("Manage Pets");
        constraints.weighty = 0.0;
        gridBagLayout.setConstraints(managePetsButton, constraints);
        sidebarPanel.add(managePetsButton);
        managePetsButton.setBorderPainted(false);

        managePetsButton.addActionListener(e -> {
            ViewPet viewPet = new ViewPet();
            viewPet.setSize(1200, 700);
            viewPet.setLocationRelativeTo(null);
            viewPet.setVisible(true);
            setVisible(false);
        });

        JButton manageOwnersButton = new JButton("Manage Owners");
        constraints.weighty = 0.0;
        gridBagLayout.setConstraints(manageOwnersButton, constraints);
        sidebarPanel.add(manageOwnersButton);
        manageOwnersButton.setBorderPainted(false);

        manageOwnersButton.addActionListener(e -> {
            ViewOwner viewOwner = new ViewOwner();
            viewOwner.setSize(800, 600);
            viewOwner.setLocationRelativeTo(null);
            viewOwner.setVisible(true);
            setVisible(false);
        });

        JButton todayAppointmentButton = new JButton("Today Appointment");
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 0.0;
        gridBagLayout.setConstraints(todayAppointmentButton, constraints);
        sidebarPanel.add(todayAppointmentButton);
        todayAppointmentButton.setBorderPainted(false);

        todayAppointmentButton.addActionListener(e -> {
            TodayAppointment todayAppointment = new TodayAppointment();
            todayAppointment.setVisible(true);



            setVisible(false);
        });
        JButton backButton = new JButton("Log Out");
        constraints.weighty = 0.0;
        gridBagLayout.setConstraints(backButton, constraints);
        sidebarPanel.add(backButton);
        backButton.setBorderPainted(false);

        backButton.addActionListener(e -> {

            Logging loginPage = new Logging();

            loginPage.setVisible(true);

            setVisible(false);
        });





        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        JLabel contentLabel = new JLabel("Welcome To PetWell", SwingConstants.CENTER);
        contentLabel.setFont(contentLabel.getFont().deriveFont(17f));
        contentPanel.add(contentLabel, BorderLayout.NORTH);


        ImageIcon imageIcon = new ImageIcon("C:\\Users\\HP\\Downloads\\pet-veterinarian-concept-veterinary-doctor-checking-treating-animal-idea-pet-care-animal-medical-vac\\veterinarian_2.jpg"); // Replace with your image path


        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);


        int width = contentPanel.getWidth();
        int height = contentPanel.getHeight();


        Image scaledImage = imageIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage);
        imageLabel.setIcon(imageIcon);

        contentPanel.add(imageLabel, BorderLayout.CENTER);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            String adminName = "Admin";
            new AdminDashboard(adminName);
        });
    }
}
