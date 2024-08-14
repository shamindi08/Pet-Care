package petcare;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Properties;
import com.github.lgooddatepicker.components.TimePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;


public class ViewAppointment extends JPanel {
    private JLabel petIDLabel,  dateLabel, timeLabel;
    private JTextField petIDField;
    private JDatePickerImpl datePicker;
    private TimePicker timePicker;
    private JButton submitButton, updateButton, deleteButton;
    private JTable appointmentTable;
    private JScrollPane tableScrollPane;


    private Connection connection;
    public ViewAppointment() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        petIDLabel = new JLabel("Pet ID:");
        petIDField = new JTextField(15);
        dateLabel = new JLabel("Date");
        timeLabel = new JLabel("Time");

        UtilDateModel dateModel = new UtilDateModel();
        Properties dateProperties = new Properties();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, dateProperties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        timePicker = new TimePicker();

        addToPanelCustom(northPanel, gbc, petIDLabel, 0, 0);
        addToPanelCustom(northPanel, gbc, petIDField, 1, 0);

        addToPanelCustom(northPanel, gbc, dateLabel, 0, 1);
        addToPanelCustom(northPanel, gbc, datePicker, 1, 1);
        addToPanelCustom(northPanel, gbc, timeLabel, 2, 1);
        addToPanelCustom(northPanel, gbc, timePicker, 3, 1);

        submitButton = new JButton("Add Appointment");
        submitButton.setBorderPainted(false);
        updateButton = new JButton("Update Appointment");
        updateButton.setBorderPainted(false);
        deleteButton = new JButton("Remove Appointment");
        deleteButton.setBorderPainted(false);

        addButtonToPanel(northPanel, gbc, submitButton, 1, 2, GridBagConstraints.HORIZONTAL);
        addButtonToPanel(northPanel, gbc, updateButton, 2, 2, GridBagConstraints.HORIZONTAL);
        addButtonToPanel(northPanel, gbc, deleteButton, 3, 2, GridBagConstraints.HORIZONTAL);

        JButton backButton = new JButton("Back");
        backButton.setBorderPainted(false);
        addButtonToPanel(northPanel, gbc, backButton, 0, 2, GridBagConstraints.HORIZONTAL);

        backButton.addActionListener(e -> {

            JFrame adminDashboardFrame = new AdminDashboard("Admin Name");
            adminDashboardFrame.setVisible(true);
            setVisible(false);
        });

        Dimension btnSize = new Dimension(200, 30);
        submitButton.setPreferredSize(btnSize);
        updateButton.setPreferredSize(btnSize);
        deleteButton.setPreferredSize(btnSize);

        add(northPanel, BorderLayout.NORTH);


        submitButton.addActionListener(e -> {
            String petID = petIDField.getText();
            java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
            String selectedTime = timePicker.getTimeStringOrEmptyString();

            String url = "jdbc:mysql://localhost:3306/petcare";
            String username = "root";
            String password = "GIS001008r0529*";

            try {
                connection = DriverManager.getConnection(url, username, password);
                String insertQuery = "INSERT INTO appointment (pet_id, appo_time, appo_date) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, petID);
                preparedStatement.setString(2, selectedTime);
                preparedStatement.setDate(3, sqlDate);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    preparedStatement.close();
                    connection.close();

                    JOptionPane.showMessageDialog(this, "Appointment added successfully!");
                    populateAppointmentTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add appointment. No rows affected.",
                            "Insert Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(this, "Failed to add appointment. Check if the Pet ID exists.",
                        "Insert Failed", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        updateButton.addActionListener(e -> {

            int selectedRowIndex = appointmentTable.getSelectedRow();

            if (selectedRowIndex == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to update.",
                        "No Row Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }


            int appoID = (int) appointmentTable.getValueAt(selectedRowIndex, 0);


            String updatedPetID = petIDField.getText();
            java.util.Date updatedDate = (java.util.Date) datePicker.getModel().getValue();
            java.sql.Date updatedSqlDate = new java.sql.Date(updatedDate.getTime());
            String updatedTime = timePicker.getTimeStringOrEmptyString();

            String url = "jdbc:mysql://localhost:3306/petcare";
            String username = "root";
            String password = "GIS001008r0529*";

            try {
                connection = DriverManager.getConnection(url, username, password);
                String updateQuery = "UPDATE appointment SET pet_id = ?, appo_time = ?, appo_date = ? WHERE appo_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, updatedPetID);
                preparedStatement.setString(2, updatedTime);
                preparedStatement.setDate(3, updatedSqlDate);
                preparedStatement.setInt(4, appoID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    preparedStatement.close();
                    connection.close();

                    JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
                    populateAppointmentTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update appointment. No rows affected.",
                            "Update Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(this, "Failed to update appointment.",
                        "Update Failed", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        deleteButton.addActionListener(e -> {

            int selectedRowIndex = appointmentTable.getSelectedRow();

            if (selectedRowIndex == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.",
                        "No Row Selected", JOptionPane.WARNING_MESSAGE);
                return; // Exit if no row is selected
            }


            int appoID = (int) appointmentTable.getValueAt(selectedRowIndex, 0);

            String url = "jdbc:mysql://localhost:3306/petcare";
            String username = "root";
            String password = "GIS001008r0529*";

            try {
                connection = DriverManager.getConnection(url, username, password);
                String deleteQuery = "DELETE FROM appointment WHERE appo_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setInt(1, appoID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    preparedStatement.close();
                    connection.close();

                    JOptionPane.showMessageDialog(this, "Appointment deleted successfully!");
                    populateAppointmentTable(); // Refresh the table after deletion
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete appointment. No rows affected.",
                            "Deletion Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(this, "Failed to delete appointment.",
                        "Deletion Failed", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });






        appointmentTable = new JTable();
        tableScrollPane = new JScrollPane(appointmentTable);
        add(tableScrollPane, BorderLayout.CENTER);
        populateAppointmentTable();

    }
    private void populateAppointmentTable() {
        String url = "jdbc:mysql://localhost:3306/petcare";
        String username = "root";
        String password = "GIS001008r0529*";

        try {
            connection = DriverManager.getConnection(url, username, password);
            String selectQuery = "SELECT * FROM appointment";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();


            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Apponitmnet ID");
            model.addColumn("Pet ID");
            model.addColumn("Time");
            model.addColumn("Date");

            while (resultSet.next()) {
                int appoID = resultSet.getInt("appo_id");
                int petID = resultSet.getInt("pet_id");
                String appoTime = resultSet.getString("appo_time");
                Date appoDate = resultSet.getDate("appo_date");

                // Add each row of data to the TableModel
                model.addRow(new Object[]{appoID, petID, appoTime, appoDate});
            }

            // Set the populated TableModel to the JTable
            appointmentTable.setModel(model);

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch appointment data.",
                    "Fetch Failed", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addToPanelCustom(JPanel panel, GridBagConstraints gbc, JComponent component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
    }

    private void addButtonToPanel(JPanel panel, GridBagConstraints gbc, JComponent component, int x, int y, int fill) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.fill = fill;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(component, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Appointment Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ViewAppointment appointmentView = new ViewAppointment();
            frame.add(appointmentView);

            int width = 1000;
            int height = 600;
            frame.setSize(width, height);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}