import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Swing_registration extends JFrame {
    private JTextField userIdField, firstNameField, lastNameField, emailField, usernameField, mobileField;
    private JPasswordField passwordField;

    public Swing_registration() {
        setTitle("Registration Form");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Create labels
        JLabel userIdLabel = new JLabel("User ID:");
        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel mobileLabel = new JLabel("Mobile Number:");

        // Create text fields and password field
        userIdField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        mobileField = new JTextField();

        // Create buttons
        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear");
        JButton fetchDetailsButton = new JButton("Fetch Details");

        // Set component bounds
        setBounds(userIdLabel, 20, 20);
        setBounds(userIdField, 120, 20);
        setBounds(firstNameLabel, 20, 50);
        setBounds(firstNameField, 120, 50);
        setBounds(lastNameLabel, 20, 80);
        setBounds(lastNameField, 120, 80);
        setBounds(emailLabel, 20, 110);
        setBounds(emailField, 120, 110);
        setBounds(usernameLabel, 20, 140);
        setBounds(usernameField, 120, 140);
        setBounds(passwordLabel, 20, 170);
        setBounds(passwordField, 120, 170);
        setBounds(mobileLabel, 20, 200);
        setBounds(mobileField, 120, 200);
        setBounds(registerButton, 50, 240);
        setBounds(clearButton, 200, 240);
        setBounds(fetchDetailsButton, 300, 20);

        // Add components to the frame
        add(userIdLabel);
        add(userIdField);
        add(firstNameLabel);
        add(firstNameField);
        add(lastNameLabel);
        add(lastNameField);
        add(emailLabel);
        add(emailField);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(mobileLabel);
        add(mobileField);
        add(registerButton);
        add(clearButton);
        add(fetchDetailsButton);

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        // Clear button action listener
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Fetch Details button action listener
        fetchDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdField.getText().trim();
                if (!userId.isEmpty()) {
                    fetchUserDetails(userId);
                } else {
                    JOptionPane.showMessageDialog(Swing_registration.this, "Please enter a User ID.");
                }
            }
        });
    }

    private void setBounds(JComponent component, int x, int y) {
        component.setBounds(x, y, 150, 25);
    }

    private void registerUser() {
       try {
            // Establish the database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/swing", "root", "kajal@123");

            // Prepare the SQL statement for insertion (including user_id)
            String insertQuery = "INSERT INTO user_registration (user_id, first_name, last_name, email, username, password, mobile_number) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Set the values for the parameters
                preparedStatement.setString(1, userIdField.getText());
                preparedStatement.setString(2, firstNameField.getText());
                preparedStatement.setString(3, lastNameField.getText());
                preparedStatement.setString(4, emailField.getText());
                preparedStatement.setString(5, usernameField.getText());
                preparedStatement.setString(6, new String(passwordField.getPassword()));
                preparedStatement.setString(7, mobileField.getText());

                // Execute the INSERT statement
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User registered successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to register user.");
                }
            }

            // Close the connection
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
        }

        // Call fetchUserDetails for demonstration after registration
        String userId = userIdField.getText().trim();
        if (!userId.isEmpty()) {
            fetchUserDetails(userId);
        }
    }

    private void fetchUserDetails(String userId) {
        try {
        // Establish the database connection
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/swing", "root", "kajal@123");

        // Prepare the SQL statement for fetching user details
        String selectQuery = "SELECT * FROM user_registration WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            // Set the value for the parameter
            preparedStatement.setString(1, userId);

            // Execute the SELECT statement
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // User found, display details or perform actions as needed
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String username = resultSet.getString("username");
                    String mobileNumber = resultSet.getString("mobile_number");

                    // Display the fetched details (you can modify this part based on your needs)
                    JOptionPane.showMessageDialog(this, "User Details:\n" +
                            "User ID: " + userId + "\n" +
                            "First Name: " + firstName + "\n" +
                            "Last Name: " + lastName + "\n" +
                            "Email: " + email + "\n" +
                            "Username: " + username + "\n" +
                            "Mobile Number: " + mobileNumber);
                } else {
                    // User not found
                    JOptionPane.showMessageDialog(this, "User with ID " + userId + " not found.");
                }
            }
        }

        // Close the connection
        connection.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error connecting to the database.");
    }
    }

    private void clearFields() {
        userIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        mobileField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Swing_registration().setVisible(true);
            }
        });
    }
}


//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class Swing_registration extends JFrame {
//    private JTextField userIdField, firstNameField, lastNameField, emailField, usernameField, mobileField;
//    private JPasswordField passwordField;
//
//    public Swing_registration() {
//        setTitle("Registration Form");
//        setSize(400, 300);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(null);
//
//        // Create labels
//        JLabel userIdLabel = new JLabel("User ID:");
//        JLabel firstNameLabel = new JLabel("First Name:");
//        JLabel lastNameLabel = new JLabel("Last Name:");
//        JLabel emailLabel = new JLabel("Email:");
//        JLabel usernameLabel = new JLabel("Username:");
//        JLabel passwordLabel = new JLabel("Password:");
//        JLabel mobileLabel = new JLabel("Mobile Number:");
//
//        // Create text fields and password field
//        userIdField = new JTextField();
//        firstNameField = new JTextField();
//        lastNameField = new JTextField();
//        emailField = new JTextField();
//        usernameField = new JTextField();
//        passwordField = new JPasswordField();
//        mobileField = new JTextField();
//
//        // Create buttons
//        JButton registerButton = new JButton("Register");
//        JButton clearButton = new JButton("Clear");
//
//        // Set component bounds
//        setBounds(userIdLabel, 20, 20);
//        setBounds(userIdField, 120, 20);
//        setBounds(firstNameLabel, 20, 50);
//        setBounds(firstNameField, 120, 50);
//        setBounds(lastNameLabel, 20, 80);
//        setBounds(lastNameField, 120, 80);
//        setBounds(emailLabel, 20, 110);
//        setBounds(emailField, 120, 110);
//        setBounds(usernameLabel, 20, 140);
//        setBounds(usernameField, 120, 140);
//        setBounds(passwordLabel, 20, 170);
//        setBounds(passwordField, 120, 170);
//        setBounds(mobileLabel, 20, 200);
//        setBounds(mobileField, 120, 200);
//        setBounds(registerButton, 50, 240);
//        setBounds(clearButton, 200, 240);
//
//        // Add components to the frame
//        add(userIdLabel);
//        add(userIdField);
//        add(firstNameLabel);
//        add(firstNameField);
//        add(lastNameLabel);
//        add(lastNameField);
//        add(emailLabel);
//        add(emailField);
//        add(usernameLabel);
//        add(usernameField);
//        add(passwordLabel);
//        add(passwordField);
//        add(mobileLabel);
//        add(mobileField);
//        add(registerButton);
//        add(clearButton);
//
//        // Register button action listener
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                registerUser();
//            }
//        });
//
//        // Clear button action listener
//        clearButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                clearFields();
//            }
//        });
//    }
//
//    private void setBounds(JComponent component, int x, int y) {
//        component.setBounds(x, y, 150, 25);
//    }
//
//    private void registerUser() {
//        try {
//            // Establish the database connection
//            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/swing", "root", "kajal@123");
//
//            // Prepare the SQL statement for insertion (including user_id)
//            String insertQuery = "INSERT INTO user_registration (user_id, first_name, last_name, email, username, password, mobile_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
//                // Set the values for the parameters
//                preparedStatement.setString(1, userIdField.getText());
//                preparedStatement.setString(2, firstNameField.getText());
//                preparedStatement.setString(3, lastNameField.getText());
//                preparedStatement.setString(4, emailField.getText());
//                preparedStatement.setString(5, usernameField.getText());
//                preparedStatement.setString(6, new String(passwordField.getPassword()));
//                preparedStatement.setString(7, mobileField.getText());
//
//                // Execute the INSERT statement
//                int rowsAffected = preparedStatement.executeUpdate();
//
//                if (rowsAffected > 0) {
//                    JOptionPane.showMessageDialog(this, "User registered successfully!");
//                    clearFields();
//                } else {
//                    JOptionPane.showMessageDialog(this, "Failed to register user.");
//                }
//            }
//
//            // Close the connection
//            connection.close();
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
//        }
//    }
//
//    private void clearFields() {
//        userIdField.setText("");
//        firstNameField.setText("");
//        lastNameField.setText("");
//        emailField.setText("");
//        usernameField.setText("");
//        passwordField.setText("");
//        mobileField.setText("");
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new Swing_registration().setVisible(true);
//            }
//        });
//    }
//}
