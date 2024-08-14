package petcare;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Home extends JFrame {

    public Home() {
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);



        JPanel mainPanel = new JPanel(new GridLayout(2, 2));

        JPanel card1 = createCardPanel("New Appointment", "https://img.freepik.com/free-vector/pet-medical-care-cartoon-background-with-vet-vaccinating-cat-office-veterinary-clinic-flat-vector-illustration_1284-79727.jpg", Color.lightGray);
        JPanel card2 = createCardPanel("View Appointment", "https://img.freepik.com/premium-vector/vet-doctor-appointment-cartoon-people-pets-animal-surgery-veterinarian-examining-patients-puppy-inspection-veterinary-hospital-cat-dog-treatment-garish-vector-concept_533410-2622.jpg", Color.lightGray);
        JPanel card3 = createCardPanel("View Pet Details", "https://i.pinimg.com/736x/05/b0/80/05b080efd8fcbfec87862861c137d921.jpg", Color.lightGray);
        JPanel card4 = createCardPanel("View Owner Details", "https://img.freepik.com/premium-vector/veterinary-clinic-concept_118813-14570.jpg", Color.lightGray);

        mainPanel.add(card1);
        mainPanel.add(card2);
        mainPanel.add(card3);
        mainPanel.add(card4);

        getContentPane().add(mainPanel);
        getContentPane().add(mainPanel, BorderLayout.CENTER);


        JButton backButton = new JButton("Back");
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {

            JFrame adminDashboardFrame = new AdminDashboard("Admin Name");
            adminDashboardFrame.setVisible(true);


           setVisible(false);
        });

        getContentPane().add(backButton, BorderLayout.SOUTH);
    }


    private JPanel createCardPanel(String content, String imageURL, Color bgColor) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cardPanel.setBackground(bgColor);

        JLabel label = new JLabel(content, SwingConstants.CENTER);
        cardPanel.add(label, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new BorderLayout());
        cardPanel.add(imagePanel, BorderLayout.CENTER);

        try {
            URL url = new URL(imageURL);
            ImageIcon icon = new ImageIcon(url);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(scaledIcon);
            imagePanel.add(imageLabel, BorderLayout.CENTER);


            cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {

                    JFrame anotherPage = new JFrame("Another Page");
                    anotherPage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    anotherPage.setSize(600, 400);
                    anotherPage.setLocationRelativeTo(null);
                    anotherPage.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cardPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Home form = new Home();
            form.setVisible(true);
        });
    }
}
