import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScreenTimeController extends JFrame {

    private JTextField timeField;
    private JButton startButton;
    private Timer timer;

    public ScreenTimeController() {
        setTitle("Smart Screen Time Controller");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        showHomeScreen();
        setVisible(true);
    }

    // HOME SCREEN
    private void showHomeScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("📱 Screen Time Controller");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        timeField = new JTextField();
        timeField.setMaximumSize(new Dimension(200, 30));
        timeField.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = new JButton("Start Session");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton.addActionListener(e -> startSession());

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JLabel("Enter Screen Time (minutes):"));
        panel.add(timeField);
        panel.add(Box.createVerticalStrut(15));
        panel.add(startButton);

        add(panel);
    }

    // START SESSION
    private void startSession() {
        int minutes;

        try {
            minutes = Integer.parseInt(timeField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid minutes");
            return;
        }

        startButton.setEnabled(false);

        timer = new Timer(minutes * 60 * 1000, e -> lockScreen());
        timer.setRepeats(false);
        timer.start();

        JOptionPane.showMessageDialog(this,
                "Session started for " + minutes + " minutes");
    }

    // WHEN TIME REACHES → FAKE LOCK
    private void lockScreen() {
        dispose(); // close main window
        SwingUtilities.invokeLater(FakeLockScreen::new);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScreenTimeController::new);
    }
}

// ================= FAKE LOCK SCREEN =================

class FakeLockScreen extends JFrame {

    private final String PARENT_PIN = "1234";

    FakeLockScreen() {
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setAlwaysOnTop(true);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel lockLabel = new JLabel("🔒 SCREEN LOCKED");
        lockLabel.setForeground(Color.WHITE);
        lockLabel.setFont(new Font("Arial", Font.BOLD, 40));

        JButton callBtn = new JButton("📞 Calling App");
        JButton studyBtn = new JButton("📚 Study App");
        JButton gmailBtn = new JButton("📧 Gmail");
        JButton parentBtn = new JButton("🔑 Parent Override");

        style(callBtn);
        style(studyBtn);
        style(gmailBtn);
        style(parentBtn);

        callBtn.addActionListener(e -> new FakeCallScreen(this));

        studyBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Study App Opened"));

        gmailBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Gmail Opened"));

        parentBtn.addActionListener(e -> {
            String pin = JOptionPane.showInputDialog(this, "Enter Parent PIN");
            if (PARENT_PIN.equals(pin)) {
                JOptionPane.showMessageDialog(this, "Unlocked by Parent");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(this, "Wrong PIN");
            }
        });

        gbc.gridy = 0;
        add(lockLabel, gbc);
        gbc.gridy++;
        add(callBtn, gbc);
        gbc.gridy++;
        add(studyBtn, gbc);
        gbc.gridy++;
        add(gmailBtn, gbc);
        gbc.gridy++;
        add(parentBtn, gbc);

        setVisible(true);
    }

    private void style(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
    }
}

// ================= FAKE CALL SCREEN =================

class FakeCallScreen extends JFrame {

    private Timer callTimer;
    private int seconds = 0;

    FakeCallScreen(JFrame parent) {
        setTitle("Calling...");
        setSize(300, 400);
        setLocationRelativeTo(parent);
        setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.BLACK);

        JLabel nameLabel = new JLabel("📞 Mom");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLabel = new JLabel("Calling...");
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel timerLabel = new JLabel("00:00");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton endCallBtn = new JButton("🔴 End Call");
        endCallBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        endCallBtn.setBackground(Color.RED);
        endCallBtn.setForeground(Color.WHITE);
        endCallBtn.setFocusPainted(false);

        callTimer = new Timer(1000, e -> {
            seconds++;
            int min = seconds / 60;
            int sec = seconds % 60;
            timerLabel.setText(String.format("%02d:%02d", min, sec));
            statusLabel.setText("In Call");
        });
        callTimer.start();

        endCallBtn.addActionListener(e -> {
            callTimer.stop();
            dispose();
        });

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(timerLabel);
        panel.add(Box.createVerticalStrut(40));
        panel.add(endCallBtn);

        add(panel);
        setVisible(true);
    }
}

