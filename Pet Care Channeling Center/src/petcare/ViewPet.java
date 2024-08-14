package petcare;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewPet extends JFrame {
    private JLabel ownerIdLabel, nameLabel, typeLabel, ageLabel, breedLabel, descriptionLabel, medicalHistoryLabel;
    private JTextField ownerIdField, nameField, typeField, ageField, breedField, descriptionField, medicalHistoryField;
    private JButton submitButton, updateButton, deleteButton;
    private DefaultTableModel tableModel;
    private JTable petTable;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/petcare";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "GIS001008r0529*";

    public ViewPet() {
        setTitle("Pet Appointment Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        ownerIdLabel = new JLabel("Owner ID");
        ownerIdField = new JTextField(15);
        nameLabel = new JLabel("Name");
        nameField = new JTextField(15);
        typeLabel = new JLabel("Type");
        typeField = new JTextField(15);
        ageLabel = new JLabel("Age");
        ageField = new JTextField(15);
        breedLabel = new JLabel("Breed");
        breedField = new JTextField(15);
        descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(15);
        medicalHistoryLabel = new JLabel("Medical History:");
        medicalHistoryField = new JTextField(15);

        submitButton = new JButton("Add Appointment");
        submitButton.setBorderPainted(false);
        updateButton = new JButton("Update Appointment");
        updateButton.setBorderPainted(false);
        deleteButton = new JButton("Remove Appointment");
        deleteButton.setBorderPainted(false);

        JButton backButton = new JButton("Back");
        backButton.setBorderPainted(false);
        addButtonToPanel(northPanel, gbc, backButton, 0, 2, GridBagConstraints.HORIZONTAL);

        backButton.addActionListener(e -> {

            JFrame adminDashboardFrame = new AdminDashboard("Admin Name");
            adminDashboardFrame.setVisible(true);


           setVisible(false);
        });

        addToPanelCustom(northPanel, gbc, ownerIdLabel, 0, 0);
        addToPanelCustom(northPanel, gbc, ownerIdField, 1, 0);
        addToPanelCustom(northPanel, gbc, nameLabel, 2, 0);
        addToPanelCustom(northPanel, gbc, nameField, 3, 0);
        addToPanelCustom(northPanel, gbc, typeLabel, 4, 0);
        addToPanelCustom(northPanel, gbc, typeField, 5, 0);
        addToPanelCustom(northPanel, gbc, ageLabel, 0, 1);
        addToPanelCustom(northPanel, gbc, ageField, 1, 1);
        addToPanelCustom(northPanel, gbc, breedLabel, 2, 1);
        addToPanelCustom(northPanel, gbc, breedField, 3, 1);
        addToPanelCustom(northPanel, gbc, descriptionLabel, 4, 1);
        addToPanelCustom(northPanel, gbc, descriptionField, 5, 1);
        addToPanelCustom(northPanel, gbc, medicalHistoryLabel, 6, 1);
        addToPanelCustom(northPanel, gbc, medicalHistoryField, 7, 1);

        addButtonToPanel(northPanel, gbc, submitButton, 1, 2, GridBagConstraints.HORIZONTAL);
        addButtonToPanel(northPanel, gbc, updateButton, 2, 2, GridBagConstraints.HORIZONTAL);
        addButtonToPanel(northPanel, gbc, deleteButton, 3, 2, GridBagConstraints.HORIZONTAL);

        add(northPanel, BorderLayout.NORTH);

        String[] columns = {"Pet ID", "Owner ID", "Name", "Type", "Age", "Breed", "Description", "Medical History"};
        tableModel = new DefaultTableModel(columns, 0);
        petTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(petTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchPetData();

        submitButton.addActionListener(e -> insertData());
        deleteButton.addActionListener(e -> deleteSelectedPet());
        updateButton.addActionListener(e -> updateSelectedPet());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void insertData() {
        submitButton.addActionListener(e -> {

            int ownerId = Integer.parseInt(ownerIdField.getText());
            String petName = nameField.getText();
            String petType = typeField.getText();
            String petAge = ageField.getText();
            String petBreed = breedField.getText();
            String description = descriptionField.getText();
            String medicalHistory = medicalHistoryField.getText();


            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                String insertQuery = "INSERT INTO pet(owner_id, P_name, p_type, p_age, p_breed, doc_description, medical_history) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setInt(1, ownerId);
                preparedStatement.setString(2, petName);
                preparedStatement.setString(3, petType);
                preparedStatement.setString(4, petAge);
                preparedStatement.setString(5, petBreed);
                preparedStatement.setString(6, description);
                preparedStatement.setString(7, medicalHistory);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Data inserted successfully!");


                    fetchPetData();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to insert data.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database connection error: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter numeric values for IDs.");
            }
        });
    }

    private void deleteSelectedPet() {
        int selectedRow = petTable.getSelectedRow();
        if (selectedRow != -1) {
            int petId = (int) tableModel.getValueAt(selectedRow, 0);
            deletePet(petId);
            fetchPetData();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to delete.");
        }
    }

    private void deletePet(int petId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String deleteQuery = "DELETE FROM pet WHERE pet_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, petId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting pet: " + e.getMessage());
        }
    }

    private void fetchPetData() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String selectQuery = "SELECT * FROM pet";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("pet_id"),
                        resultSet.getInt("owner_id"),
                        resultSet.getString("P_name"),
                        resultSet.getString("p_type"),
                        resultSet.getString("p_age"),
                        resultSet.getString("p_breed"),
                        resultSet.getString("doc_description"),
                        resultSet.getString("medical_history")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching pet data: " + ex.getMessage());
        }
    }
    private void updateSelectedPet() {
        int selectedRow = petTable.getSelectedRow();
        if (selectedRow != -1) {
            int petId = (int) tableModel.getValueAt(selectedRow, 0);

            int ownerId = (int) tableModel.getValueAt(selectedRow, 1);
            String petName = (String) tableModel.getValueAt(selectedRow, 2);
            String petType = (String) tableModel.getValueAt(selectedRow, 3);
            String petAge = (String) tableModel.getValueAt(selectedRow, 4);
            String petBreed = (String) tableModel.getValueAt(selectedRow, 5);
            String description = (String) tableModel.getValueAt(selectedRow, 6);
            String medicalHistory = (String) tableModel.getValueAt(selectedRow, 7);


            ownerIdField.setText(String.valueOf(ownerId));
            nameField.setText(petName);
            typeField.setText(petType);
            ageField.setText(petAge);
            breedField.setText(petBreed);
            descriptionField.setText(description);
            medicalHistoryField.setText(medicalHistory);


            updateButton.addActionListener(e -> {

                int updatedOwnerId = Integer.parseInt(ownerIdField.getText());
                String updatedPetName = nameField.getText();
                String updatedPetType = typeField.getText();
                String updatedPetAge = ageField.getText();
                String updatedPetBreed = breedField.getText();
                String updatedDescription = descriptionField.getText();
                String updatedMedicalHistory = medicalHistoryField.getText();

                try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                    String updateQuery = "UPDATE pet SET owner_id = ?, P_name = ?, p_type = ?, p_age = ?, p_breed = ?, doc_description = ?, medical_history = ? WHERE pet_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setInt(1, updatedOwnerId);
                    preparedStatement.setString(2, updatedPetName);
                    preparedStatement.setString(3, updatedPetType);
                    preparedStatement.setString(4, updatedPetAge);
                    preparedStatement.setString(5, updatedPetBreed);
                    preparedStatement.setString(6, updatedDescription);
                    preparedStatement.setString(7, updatedMedicalHistory);
                    preparedStatement.setInt(8, petId);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Data updated successfully!");

                        fetchPetData();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update data.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database connection error: " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter numeric values for IDs.");
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to update.");
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
            ViewPet viewPet = new ViewPet();
            viewPet.setSize(1200, 700);
            viewPet.setLocationRelativeTo(null);
        });
    }
}