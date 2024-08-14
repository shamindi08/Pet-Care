package petcare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewOwner extends JFrame {
    private DefaultTableModel tableModel;
    private JTable ownerTable;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/petcare";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "GIS001008r0529*";

    private JTextField nameField;
    private JTextField contactNoField;
    private JTextField addressField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private String ownerId;

    public ViewOwner() {
        setTitle("Owner Details Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);


        
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);

        JLabel contactLabel = new JLabel("Contact No:");
        contactNoField = new JTextField(15);

        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField(15);

        addButton = new JButton("Add Owner");
        addButton.setBorderPainted(false);
        deleteButton = new JButton("Delete Owner");
        deleteButton.setBorderPainted(false);
        updateButton = new JButton("Update Owner");
        updateButton.setBorderPainted(false);

        JButton backButton = new JButton("Back");
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> showAdminDashboard());

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;




        addButton.addActionListener(e -> addOwner());
        deleteButton.addActionListener(e -> deleteOwner());
        updateButton.addActionListener(e -> updateOwner());

        gbc.gridx = 0;
        gbc.gridy = 0;
        northPanel.add(nameLabel, gbc);
        gbc.gridy++;
        northPanel.add(contactLabel, gbc);
        gbc.gridy++;
        northPanel.add(addressLabel, gbc);


        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        northPanel.add(nameField, gbc);
        gbc.gridy++;
        northPanel.add(contactNoField, gbc);
        gbc.gridy++;
        northPanel.add(addressField, gbc);


        gbc.gridy++;
        northPanel.add(addButton, gbc);
        gbc.gridy++;
        northPanel.add(deleteButton, gbc);
        gbc.gridy++;
        northPanel.add(updateButton, gbc);
        gbc.gridy++;
        northPanel.add(backButton, gbc);

        add(northPanel, BorderLayout.NORTH);

        String[] columns = {"Owner ID", "Name", "Contact No", "Address"};
        tableModel = new DefaultTableModel(columns, 0);
        ownerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ownerTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchOwnerData();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void showAdminDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard("Admin");
        adminDashboard.setVisible(true);
        dispose();
    }

    private void addOwner() {
        String ownerName = nameField.getText();
        String contactNo = contactNoField.getText();
        String address = addressField.getText();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String insertQuery = "INSERT INTO owner(o_name, contact_no, address) VALUES ( ?, ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, ownerId);
            preparedStatement.setString(1, ownerName);
            preparedStatement.setString(2, contactNo);
            preparedStatement.setString(3, address);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "New owner added successfully!");
                fetchOwnerData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add new owner.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding new owner: " + ex.getMessage());
        }
    }

    private void fetchOwnerData() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String selectQuery = "SELECT * FROM owner";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("owner_id"),
                        resultSet.getString("o_name"),
                        resultSet.getString("contact_no"),
                        resultSet.getString("address")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching owner data: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        contactNoField.setText("");
        addressField.setText("");
    }

    private void deleteOwner() {
        int selectedRow = ownerTable.getSelectedRow();
        if (selectedRow != -1) {
            int ownerId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String deleteQuery = "DELETE FROM owner WHERE owner_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setInt(1, ownerId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Owner deleted successfully!");
                    fetchOwnerData();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete owner.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error deleting owner: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to delete.");
        }
    }

    private void updateOwner() {
        int selectedRow = ownerTable.getSelectedRow();
        if (selectedRow != -1) {
            int ownerId = (int) tableModel.getValueAt(selectedRow, 0);
            String ownerName = nameField.getText();
            String contactNo = contactNoField.getText();
            String address = addressField.getText();

            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String updateQuery = "UPDATE owner SET o_name = ?, contact_no = ?, address = ? WHERE owner_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

                preparedStatement.setString(1, ownerName);
                preparedStatement.setString(2, contactNo);
                preparedStatement.setString(3, address);
                preparedStatement.setInt(4, ownerId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Owner updated successfully!");
                    fetchOwnerData();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update owner.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating owner: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to update.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ViewOwner viewOwnerDetails = new ViewOwner();
            viewOwnerDetails.setSize(800, 600);
            viewOwnerDetails.setLocationRelativeTo(null);
        });
    }
}
