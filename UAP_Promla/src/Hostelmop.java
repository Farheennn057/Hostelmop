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
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        txtName = new JTextField();
        txtNights = new JTextField();
        txtDate = new JTextField();

        cmbRoomType = new JComboBox<>(roomTypes);
        cmbRoomType.addActionListener(e -> updateRoomImage());

        imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        updateRoomImage();

        lblTotalCost = new JLabel("Total Cost: Rp 0", SwingConstants.CENTER);

        JButton btnAdd = new JButton("Add Booking");
        btnAdd.addActionListener(e -> addBooking());

        JButton btnDelete = new JButton("Delete Booking");
        btnDelete.addActionListener(e -> deleteBooking());

        JButton btnExport = new JButton("Export to CSV");
        btnExport.addActionListener(e -> exportToCSV());

        JButton btnImport = new JButton("Import from CSV");
        btnImport.addActionListener(e -> importFromCSV());

        // Add input components to the panel
        panel.add(new JLabel("Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Room Type:"));
        panel.add(cmbRoomType);
        panel.add(new JLabel("Number of Nights:"));
        panel.add(txtNights);
        panel.add(new JLabel("Check-In Date (yyyy-mm-dd):"));
        panel.add(txtDate);
        panel.add(new JLabel("Room Image:"));
        panel.add(imgLabel);
        panel.add(lblTotalCost);
        panel.add(btnAdd);

        // Table for bookings
        tableModel = new DefaultTableModel(new Object[]{"Name", "Room Type", "Nights", "Check-In Date", "Total Cost"}, 0);
        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnDelete);
        btnPanel.add(btnExport);
        btnPanel.add(btnImport);

        // Add components to the frame
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(btnPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateRoomImage() {
        int selectedIndex = cmbRoomType.getSelectedIndex();
        String imagePath = "src/images/" + roomTypes[selectedIndex].toLowerCase() + ".jpg"; // Update with actual path
        ImageIcon imageIcon = new ImageIcon(imagePath);
        imgLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
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
            tableModel.setRowCount(0); // Clear existing data
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

