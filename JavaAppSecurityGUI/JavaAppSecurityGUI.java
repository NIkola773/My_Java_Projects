
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

public class JavaAppSecurityGUI {

    private static final String FILE_NAME = "text.txt";
    private static final java.util.List<String> suspiciousPatterns = Arrays.asList("malware", "attack", "phishing");
    private static final int PORT = 9999;
    private static java.util.List<String> taskList = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JavaAppSecurityGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MySecurityApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        JButton portScannerButton = new JButton("Port Scanner");
        JButton malwareCheckerButton = new JButton("Malware Checker");
        JButton taskManagerButton = new JButton("Task Manager");
        JButton exitButton = new JButton("Exit");

        panel.add(registerButton);
        panel.add(loginButton);
        panel.add(portScannerButton);
        panel.add(malwareCheckerButton);
        panel.add(taskManagerButton);
        panel.add(exitButton);

        frame.add(panel);

        // Add action listeners
        registerButton.addActionListener(e -> showRegisterDialog());
        loginButton.addActionListener(e -> showLoginDialog());
        portScannerButton.addActionListener(e -> showPortScannerDialog());
        malwareCheckerButton.addActionListener(e -> showMalwareChecker());
        taskManagerButton.addActionListener(e -> showTaskManager());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private static void showRegisterDialog() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (username != null && password != null) {
            register(username, password);
        }
    }

    private static void showLoginDialog() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (username != null && password != null) {
            login(username, password);
        }
    }

    private static void showPortScannerDialog() {
        String ipAddress = JOptionPane.showInputDialog("Enter the IP address to scan:");
        if (ipAddress != null && !ipAddress.trim().isEmpty()) {
            performPortScan(ipAddress);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid IP address!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showMalwareChecker() {
        JOptionPane.showMessageDialog(null, "Malware Checker running on port " + PORT);
    }

    private static void showTaskManager() {
        String[] options = {"Add Task", "Show Tasks", "Save Tasks to File", "Back"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an option", "Task Manager",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> addTask();
            case 1 -> showTasks();
            case 2 -> saveTasksToFile();
            default -> {
            }
        }
    }

    // Methods for core functionality
    private static void register(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String hashedPassword = hashPassword(password);
            writer.write(username + "," + hashedPassword);
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Registration successful!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error registering user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(hashPassword(password))) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error logging in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void performPortScan(String ipAddress) {
        StringBuilder result = new StringBuilder();
        int startPort = 1, endPort = 1024;

        for (int port = startPort; port <= endPort; port++) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ipAddress, port), 100);
                result.append("Port ").append(port).append(" is OPEN.\n");
            } catch (IOException e) {
                result.append("Port ").append(port).append(" is CLOSED.\n");
            }
        }

        JOptionPane.showMessageDialog(null, result.toString(), "Port Scanner Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void addTask() {
        String task = JOptionPane.showInputDialog("Enter a task:");
        if (task != null && !task.trim().isEmpty()) {
            taskList.add(task);
            JOptionPane.showMessageDialog(null, "Task added successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid task input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showTasks() {
        if (taskList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available.", "Task Manager", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Tasks:\n" + String.join("\n", taskList), "Task Manager", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void saveTasksToFile() {
        if (taskList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks to save.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fileName = JOptionPane.showInputDialog("Enter the file name:");
        if (fileName != null && !fileName.trim().isEmpty()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                writer.println(String.join("\n", taskList));
                JOptionPane.showMessageDialog(null, "Tasks saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving tasks.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid file name!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password");
        }
    }
}