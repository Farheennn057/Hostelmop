import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HotelBookingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelBookingFrame::new);
    }
}

class HotelBookingFrame {
    private JFrame frame;
    private JTextField txtName, txtNights;
    private JComboBox<String> cmbRoomType;
    private JComboBox<Integer> cmbDay, cmbYear;
    private JComboBox<String> cmbMonth;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JLabel imgLabel, lblTotalCost, lblRoomPrice;

    private final RoomManager roomManager;

    public HotelBookingFrame() {
        this.roomManager = new RoomManager();

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

        cmbRoomType = new JComboBox<>(roomManager.getRoomTypes());
        cmbRoomType.addActionListener(e -> updateRoomDetails());
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        inputPanel.add(cmbRoomType, gbc);

        lblRoomPrice = new JLabel("Price per night: Rp 0", SwingConstants.CENTER);
        lblRoomPrice.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        inputPanel.add(lblRoomPrice, gbc);

        JLabel lblNights = new JLabel("Number of Nights:");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        inputPanel.add(lblNights, gbc);

        txtNights = new JTextField();
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        inputPanel.add(txtNights, gbc);

        JLabel lblDate = new JLabel("Check-In Date:");
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(lblDate, gbc);

        JPanel datePanel = new JPanel();

        cmbDay = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            cmbDay.addItem(day);
        }

        cmbMonth = new JComboBox<>(RoomManager.getMonths());

        cmbYear = new JComboBox<>();
        for (int year = LocalDate.now().getYear(); year <= LocalDate.now().getYear() + 5; year++) {
            cmbYear.addItem(year);
        }

        datePanel.add(cmbDay);
        datePanel.add(cmbMonth);
        datePanel.add(cmbYear);

        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        inputPanel.add(datePanel, gbc);

        JLabel lblImage = new JLabel("Room Image:");
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(lblImage, gbc);

        imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imgLabel.setPreferredSize(new Dimension(200, 150));
        updateRoomDetails();
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        inputPanel.add(imgLabel, gbc);

        lblTotalCost = new JLabel("Total Cost: Rp 0", SwingConstants.CENTER);
        lblTotalCost.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        inputPanel.add(lblTotalCost, gbc);

        // Tombol Add Booking
        JButton btnAdd = new JButton("Add Booking");
        btnAdd.addActionListener(e -> addBooking());
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        inputPanel.add(btnAdd, gbc);

// Tombol Delete Booking
        JButton btnDelete = new JButton("Delete Booking");
        btnDelete.addActionListener(e -> deleteBooking());
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        inputPanel.add(btnDelete, gbc);

// Tombol Export to CSV
        JButton btnExport = new JButton("Export to CSV");
        btnExport.addActionListener(e -> exportToCSV());
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        inputPanel.add(btnExport, gbc);

// Tombol Import from CSV
        JButton btnImport = new JButton("Import from CSV");
        btnImport.addActionListener(e -> importFromCSV());
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        inputPanel.add(btnImport, gbc);


        tableModel = new DefaultTableModel(new Object[]{"Name", "Room Type", "Nights", "Check-In Date", "Total Cost"}, 0);
        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, scrollPane);
        splitPane.setDividerLocation(400);
        frame.add(splitPane);

        frame.setVisible(true);
    }

    private void updateRoomDetails() {
        String roomType = (String) cmbRoomType.getSelectedItem();
        int price = roomManager.getRoomPrice(roomType);
        lblRoomPrice.setText("Price per night: Rp " + formatCurrency(price));

        String imagePath = roomManager.getRoomImagePath(roomType);
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

            int day = (int) cmbDay.getSelectedItem();
            int month = cmbMonth.getSelectedIndex() + 1;
            int year = (int) cmbYear.getSelectedItem();

            LocalDate checkInDate = LocalDate.of(year, month, day);
            if (checkInDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Check-In Date cannot be in the past.");
            }

            int totalCost = roomManager.calculateTotalCost(roomType, nights);

            tableModel.addRow(new Object[]{name, roomType, nights, checkInDate.format(DateTimeFormatter.ISO_DATE), "Rp " + formatCurrency(totalCost)});
            lblTotalCost.setText("Total Cost: Rp " + formatCurrency(totalCost));

            txtName.setText("");
            txtNights.setText("");
            cmbDay.setSelectedIndex(0);
            cmbMonth.setSelectedIndex(0);
            cmbYear.setSelectedIndex(0);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Number of Nights must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
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

    private String formatCurrency(int amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }
}

class RoomManager {
    private final String[] roomTypes = {"Standard", "Deluxe", "Suite"};
    private final int[] roomPrices = {500000, 1000000, 2000000};

    public String[] getRoomTypes() {
        return roomTypes;
    }

    public int getRoomPrice(String roomType) {
        for (int i = 0; i < roomTypes.length; i++) {
            if (roomTypes[i].equals(roomType)) {
                return roomPrices[i];
            }
        }
        throw new IllegalArgumentException("Invalid room type.");
    }

    public int calculateTotalCost(String roomType, int nights) {
        return getRoomPrice(roomType) * nights;
    }

    public String getRoomImagePath(String roomType) {
        switch (roomType) {
            case "Standard":
                return "images/Standard.jpg";
            case "Deluxe":
                return "images/Deluxe.jpg";
            case "Suite":
                return "images/Suite.jpg";
            default:
                throw new IllegalArgumentException("Invalid room type.");
        }
    }

    public static String[] getMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }
}
