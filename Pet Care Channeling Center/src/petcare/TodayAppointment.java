package petcare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class TodayAppointment extends JFrame {
    private DefaultTableModel tableModel;
    private Connection connection;

    public TodayAppointment() {
        setUpUI();
        connectToDatabase();
    }

    private void setUpUI() {
        tableModel = new DefaultTableModel();
        JTable appointmentTable = new JTable(tableModel);
        tableModel.addColumn("Pet Name");
        tableModel.addColumn("Appointment Date");

        JButton viewTodayButton = new JButton("View Today's Appointments");
        viewTodayButton.setBorderPainted(false);
        JButton backButton = new JButton("Back");
        backButton.setBorderPainted(false);

        viewTodayButton.addActionListener(e -> displayTodayAppointments());
        backButton.addActionListener(e -> {


            JFrame adminDashboardFrame = new AdminDashboard("Admin Name");
            adminDashboardFrame.setVisible(true);


            setVisible(false);
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 5));
        buttonPanel.add(viewTodayButton);
        buttonPanel.add(backButton);

        JPanel northPanel = new JPanel();
        northPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
        setTitle("Today's Appointments");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/petcare";
            String username = "root";
            String password = "GIS001008r0529*";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database!");
            System.exit(1);
        }
    }

    private void displayTodayAppointments() {
        LocalDate today = LocalDate.now();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT pet.P_name, appointment.appo_date " +
                    "FROM appointment " +
                    "INNER JOIN pet ON appointment.pet_id = pet.pet_id " +
                    "WHERE appointment.appo_date = ?");
            statement.setDate(1, Date.valueOf(today));
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                String petName = resultSet.getString("P_name");
                LocalDate appointmentDate = resultSet.getDate("appo_date").toLocalDate();
                Vector<Object> row = new Vector<>();
                row.add(petName);
                row.add(appointmentDate.toString());
                tableModel.addRow(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch appointments!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodayAppointment());
    }

    @Override
    public void dispose() {
        super.dispose();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
