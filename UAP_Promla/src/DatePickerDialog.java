import javax.swing.*;
import java.time.LocalDate;

class DatePickerDialog {
    private final JDialog dialog;
    private final JComboBox<Integer> dayComboBox;
    private final JComboBox<String> monthComboBox;
    private final JComboBox<Integer> yearComboBox;
    private LocalDate selectedDate;

    public DatePickerDialog(JFrame parent, LocalDate currentDate) {
        dialog = new JDialog(parent, "Select Date", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        JPanel datePanel = new JPanel();
        dayComboBox = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }

        monthComboBox = new JComboBox<>(RoomManager.getMonths());
        yearComboBox = new JComboBox<>();
        for (int year = currentDate.getYear(); year <= currentDate.getYear() + 5; year++) {
            yearComboBox.addItem(year);
        }

        datePanel.add(new JLabel("Day:"));
        datePanel.add(dayComboBox);
        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("Year:"));
        datePanel.add(yearComboBox);

        dialog.add(datePanel);

        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(e -> {
            int day = (Integer) dayComboBox.getSelectedItem();
            int month = monthComboBox.getSelectedIndex() + 1;
            int year = (Integer) yearComboBox.getSelectedItem();
            selectedDate = LocalDate.of(year, month, day);
            dialog.dispose();
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> {
            selectedDate = null;
            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel);

        dialog.setLocationRelativeTo(parent);
    }

    public LocalDate showDialog() {
        dialog.setVisible(true);
        return selectedDate;
    }
}
