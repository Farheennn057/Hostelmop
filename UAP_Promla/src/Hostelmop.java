import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Hostelmop {
    private JFrame frame;
    private JTextField txtName, txtNights, txtDate;
    private JComboBox<String> cmbRoomType;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JLabel imgLabel, lblTotalCost;
    private final String[] roomTypes = {"Standard", "Deluxe", "Suite"};
    private final int[] roomPrices = {500000, 1000000, 2000000};

    public Hostelmop() {
        // Initialize GUI components
        frame = new JFrame("Hotel Booking Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Name:");
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(lblName, gbc);

        txtName = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        inputPanel.add(txtName, gbc);

        JLabel lblRoomType = new JLabel("Room Type:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        inputPanel.add(lblRoomType, gbc);

        cmbRoomType = new JComboBox<>(roomTypes);
        cmbRoomType.addActionListener(e -> updateRoomImage());
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        inputPanel.add(cmbRoomType, gbc);

        JLabel lblNights = new JLabel("Number of Nights:");
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(lblNights, gbc);

        txtNights = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(txtNights, gbc);

        JLabel lblDate = new JLabel("Check-In Date (yyyy-mm-dd):");
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(lblDate, gbc);

        txtDate = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        inputPanel.add(txtDate, gbc);

        JLabel lblImage = new JLabel("Room Image:");
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(lblImage, gbc);

        imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imgLabel.setPreferredSize(new Dimension(200, 150));
        updateRoomImage();
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        inputPanel.add(imgLabel, gbc);

        lblTotalCost = new JLabel("Total Cost: Rp 0", SwingConstants.CENTER);
        lblTotalCost.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        inputPanel.add(lblTotalCost, gbc);

        JButton btnAdd = new JButton("Add Booking");
        btnAdd.addActionListener(e -> addBooking());
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        inputPanel.add(btnAdd, gbc);

        JButton btnDelete = new JButton("Delete Booking");
        btnDelete.addActionListener(e -> deleteBooking());
        gbc.gridx = 1; gbc.gridy = 6;
        inputPanel.add(btnDelete, gbc);

        JButton btnExport = new JButton("Export to CSV");
        btnExport.addActionListener(e -> exportToCSV());
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        inputPanel.add(btnExport, gbc);

        JButton btnImport = new JButton("Import from CSV");
        btnImport.addActionListener(e -> importFromCSV());
        gbc.gridx = 1; gbc.gridy = 7;
        inputPanel.add(btnImport, gbc);

        // Table for bookings
        tableModel = new DefaultTableModel(new Object[]{"Name", "Room Type", "Nights", "Check-In Date", "Total Cost"}, 0);
        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        // Split frame layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, scrollPane);
        splitPane.setDividerLocation(350);
        frame.add(splitPane);

        frame.setVisible(true);
    }

    private void updateRoomImage() {
        String imagePath;
        switch (cmbRoomType.getSelectedItem().toString()) {
            case "Standard":
                imagePath = "C:/Users/ACER/Downloads/standard.jpeg";
                break;
            case "Deluxe":
                imagePath = "C:/Users/ACER/Downloads/deluxe.jpeg";
                break;
            case "Suite":
                imagePath = "C:/Users/ACER/Downloads/suite.jpeg";
                break;
            default:
                imgLabel.setText("Image not available");
                imgLabel.setIcon(null);
                return;
        }

        try {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            if (imageIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                throw new FileNotFoundException("Image not found: " + imagePath);
            }
            imgLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            imgLabel.setText("Image not available");
            imgLabel.setIcon(null);
        }
    }

    private void addBooking() {
        try {
            String name = txtName.getText().trim();
            String roomType = (String) cmbRoomType.getSelectedItem();
            int nights = Integer.parseInt(txtNights.getText().trim());
            String dateStr = txtDate.getText().trim();

            if (name.isEmpty() || dateStr.isEmpty()) {
                throw new IllegalArgumentException("Name and Check-In Date cannot be empty.");
            }

            LocalDate checkInDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
            if (checkInDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Check-In Date cannot be in the past.");
            }

            int selectedIndex = cmbRoomType.getSelectedIndex();
            int totalCost = nights * roomPrices[selectedIndex];

            tableModel.addRow(new Object[]{name, roomType, nights, dateStr, "Rp " + totalCost});
            lblTotalCost.setText("Total Cost: Rp " + totalCost);

            // Clear input fields
            txtName.setText("");
            txtNights.setText("");
            txtDate.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Number of Nights must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(frame, "Check-In Date must be in the format yyyy-mm-dd.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a booking to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCSV() {
        try (PrintWriter writer = new PrintWriter(new File("bookings.csv"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.print(tableModel.getValueAt(i, j));
                    if (j < tableModel.getColumnCount() - 1) writer.print(",");
                }
                writer.println();
            }
            JOptionPane.showMessageDialog(frame, "Bookings exported successfully!", "Export Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to export bookings.", "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("bookings.csv"))) {
            String line;
            tableModel.setRowCount(0);
            while ((line = reader.readLine()) != null) {
                tableModel.addRow(line.split(","));
            }
            JOptionPane.showMessageDialog(frame, "Bookings imported successfully!", "Import Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to import bookings.", "Import Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Hostelmop::new);
    }
}

