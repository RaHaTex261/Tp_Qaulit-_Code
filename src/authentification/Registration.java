package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Registration extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "users.txt"; // Fichier de stockage
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnReset;

    public Registration() {
        setTitle("Registration");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(null);
        getContentPane().add(rootPanel);

        JLabel lblRegistration = new JLabel("Registration");
        lblRegistration.setBounds(45, 28, 292, 30);
        lblRegistration.setFont(new Font("Arial Black", Font.BOLD, 23));
        lblRegistration.setForeground(new Color(43, 11, 156));
        rootPanel.add(lblRegistration);

        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setBounds(45, 73, 301, 7);
        rootPanel.add(separator1);

        // Champs de saisie
        txtFirstName = createTextField("Enter your first name", 45, 117);
        txtLastName = createTextField("Enter your last name", 45, 179);
        txtEmail = createTextField("Enter your email", 45, 241);
        txtPassword = createPasswordField("Enter your password", 45, 303);
        txtConfirmPassword = createPasswordField("Confirm password", 45, 365);

        rootPanel.add(txtFirstName);
        rootPanel.add(txtLastName);
        rootPanel.add(txtEmail);
        rootPanel.add(txtPassword);
        rootPanel.add(txtConfirmPassword);

        // Bouton d'inscription
        btnRegister = new JButton("Register");
        btnRegister.setBounds(50, 425, 292, 38);
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRegister.setBackground(Color.BLUE);
        btnRegister.setForeground(Color.WHITE);
        rootPanel.add(btnRegister);
        btnRegister.addActionListener(e -> registerUser());

        // Bouton de réinitialisation
        btnReset = new JButton("Reset");
        btnReset.setBounds(50, 485, 292, 38);
        btnReset.setFont(new Font("Arial", Font.PLAIN, 16));
        btnReset.setBackground(Color.RED);
        btnReset.setForeground(Color.WHITE);
        rootPanel.add(btnReset);
        btnReset.addActionListener(e -> resetFields());

        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setBounds(49, 546, 301, 7);
        rootPanel.add(separator2);
    }

    // Création d'un champ de texte avec un placeholder
    private JTextField createTextField(String placeholder, int x, int y) {
        JTextField textField = new JTextField(placeholder);
        textField.setBounds(x, y, 292, 38);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        return textField;
    }

    // Création d'un champ de mot de passe avec un placeholder
    private JPasswordField createPasswordField(String placeholder, int x, int y) {
        JPasswordField passwordField = new JPasswordField(placeholder);
        passwordField.setBounds(x, y, 292, 38);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setForeground(Color.GRAY);

        passwordField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });
        return passwordField;
    }

    private void registerUser() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword()).trim();

        try {
            // Vérifier que tous les champs sont remplis
            if (firstName.equals("Enter your first name") || lastName.equals("Enter your last name") ||
                email.equals("Enter your email") || password.equals("Enter your password") ||
                confirmPassword.equals("Confirm password")) {
                throw new CustomException("All fields must be filled!");
            }

            // Vérification de l'email
            if (!email.contains("@") || !email.contains(".")) {
                throw new CustomException("Invalid email address! Email must contain '@' and '.'");
            }

            // Vérification du mot de passe robuste
            if (!isPasswordStrong(password)) {
                throw new CustomException("Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character.");
            }

            // Vérification si les mots de passe correspondent
            if (!password.equals(confirmPassword)) {
                throw new CustomException("Passwords do not match!");
            }

            // Vérifier si le fichier users.txt existe
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                try {
                    file.createNewFile(); // Créer le fichier s'il n'existe pas
                } catch (IOException e) {
                    throw new CustomException("Error creating file: " + e.getMessage());
                }
            }

            // Sauvegarde dans users.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                writer.write(firstName + ";" + lastName + ";" + email + ";" + password);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetFields();
            } catch (IOException e) {
                throw new CustomException("Error saving user data: " + e.getMessage());
            }
        } catch (CustomException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Vérification de la robustesse du mot de passe
    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") && // Doit contenir au moins une majuscule
               password.matches(".*[0-9].*") && // Doit contenir au moins un chiffre
               password.matches(".*[!@#\\$%^&*].*"); // Doit contenir au moins un caractère spécial
    }

    // Réinitialisation des champs de saisie
    private void resetFields() {
        txtFirstName.setText("Enter your first name");
        txtLastName.setText("Enter your last name");
        txtEmail.setText("Enter your email");
        txtPassword.setText("Enter your password");
        txtConfirmPassword.setText("Confirm password");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Registration registrationForm = new Registration();
            registrationForm.setVisible(true);
        });
    }
}
