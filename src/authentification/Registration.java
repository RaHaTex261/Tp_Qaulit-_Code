package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe représentant un formulaire d'inscription.
 * Cette classe permet à un utilisateur de s'inscrire en fournissant son prénom, nom, email et mot de passe.
 * Les données sont ensuite enregistrées dans un fichier "users.txt".
 */
public class Registration extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "auth.db"; // Fichier de stockage
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnReset;

    /**
     * Constructeur qui initialise la fenêtre de l'application avec les champs et boutons nécessaires.
     * Configure la mise en page, les champs de saisie, les boutons et les actions associées.
     */
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

        
        btnRegister = new JButton("Register");
        btnRegister.setBounds(50, 425, 292, 38);
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRegister.setBackground(Color.BLUE);
        btnRegister.setForeground(Color.WHITE);
        rootPanel.add(btnRegister);
        btnRegister.addActionListener(e -> registerUser());

       
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

    /**
     * Crée un champ de texte avec un placeholder et une gestion des événements de focus.
     * @param placeholder Le texte à afficher lorsque le champ est vide.
     * @param x La position horizontale du champ.
     * @param y La position verticale du champ.
     * @return Un champ de texte prêt à être utilisé.
     */
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

    /**
     * Crée un champ de mot de passe avec un placeholder et une gestion des événements de focus.
     * @param placeholder Le texte à afficher lorsque le champ est vide.
     * @param x La position horizontale du champ.
     * @param y La position verticale du champ.
     * @return Un champ de mot de passe prêt à être utilisé.
     */
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

    /**
     * Méthode de gestion de l'inscription de l'utilisateur.
     * Vérifie que tous les champs sont remplis, que l'email est valide,
     * que le mot de passe est robuste et que les mots de passe correspondent.
     * Enregistre les données dans un fichier "users.txt".
     */
    private void registerUser() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword()).trim();

        try {
          
            if (firstName.equals("Enter your first name") || lastName.equals("Enter your last name") ||
                email.equals("Enter your email") || password.equals("Enter your password") ||
                confirmPassword.equals("Confirm password")) {
                throw new CustomException("All fields must be filled!");
            }

         
            if (!email.contains("@") || !email.contains(".")) {
                throw new CustomException("Invalid email address! Email must contain '@' and '.'");
            }

           
            if (!isPasswordStrong(password)) {
                throw new CustomException("Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character.");
            }

    
            if (!password.equals(confirmPassword)) {
                throw new CustomException("Passwords do not match!");
            }

   
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                try {
                    file.createNewFile(); // Créer le fichier s'il n'existe pas
                } catch (IOException e) {
                    throw new CustomException("Error creating file: " + e.getMessage());
                }
            }

     
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

    /**
     * Vérifie si le mot de passe respecte les critères de robustesse :
     * - Minimum 8 caractères
     * - Contient au moins une majuscule, un chiffre, et un caractère spécial.
     * @param password Le mot de passe à vérifier.
     * @return true si le mot de passe est robuste, false sinon.
     */
    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") && // Doit contenir au moins une majuscule
               password.matches(".*[0-9].*") && // Doit contenir au moins un chiffre
               password.matches(".*[!@#\\$%^&*].*"); // Doit contenir au moins un caractère spécial
    }

    /**
     * Réinitialise les champs de saisie du formulaire.
     */
    private void resetFields() {
        txtFirstName.setText("Enter your first name");
        txtLastName.setText("Enter your last name");
        txtEmail.setText("Enter your email");
        txtPassword.setText("Enter your password");
        txtConfirmPassword.setText("Confirm password");
    }

    /**
     * Point d'entrée de l'application.
     * Lance le formulaire d'inscription dans une nouvelle fenêtre.
     * @param args Arguments de ligne de commande.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Registration registrationForm = new Registration();
            registrationForm.setVisible(true);
        });
    }
}