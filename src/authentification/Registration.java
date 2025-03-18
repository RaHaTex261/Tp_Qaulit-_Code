package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Registration extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "auth.sqlite";
    private static final String DB_URL = "jdbc:sqlite:" + FILE_PATH;
    private static Connection conn;
    
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnReset;

    private static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Connexion à la base de données établie");
            }
            return conn;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la connexion à la base de données : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS utilisateurs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT,
                last_name TEXT,
                email TEXT,
                password TEXT
            )""";
        
        try (Connection conn = getConnection()) {
            if (conn != null) {
                conn.prepareStatement(sql).execute();
                System.out.println("Table 'utilisateurs' créée ou vérifiée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table : " + e.getMessage());
        }
    }

    public Registration() {
        setTitle("Registration");
        setSize(400, 610);
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
        
        createTable();
    }

    private void registerUser() {
    	    // Validation des champs
    	    String firstName = txtFirstName.getText().trim();
    	    String lastName = txtLastName.getText().trim();
    	    String email = txtEmail.getText().trim();
    	    
    	    // Vérification du mot de passe
    	    String password = new String(txtPassword.getPassword()).trim();
    	    String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
    	    
    	    // Validation des entrées
    	    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
    	        JOptionPane.showMessageDialog(this, 
    	            "Veuillez remplir tous les champs requis", 
    	            "Erreur de validation", 
    	            JOptionPane.ERROR_MESSAGE);
    	        return;
    	    }
    	    
    	    if (!password.equals(confirmPassword)) {
    	        JOptionPane.showMessageDialog(this, 
    	            "Les mots de passe ne correspondent pas", 
    	            "Erreur de validation", 
    	            JOptionPane.ERROR_MESSAGE);
    	        return;
    	    }
    	    
    	    // Préparation et exécution de la requête d'insertion
    	    String sql = """
    	        INSERT INTO utilisateurs (first_name, last_name, email, password)
    	        VALUES (?, ?, ?, ?)
    	        """;
    	    
    	    try (Connection conn = getConnection();
    	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
    	            
    	        pstmt.setString(1, firstName);
    	        pstmt.setString(2, lastName);
    	        pstmt.setString(3, email);
    	        pstmt.setString(4, password);
    	        
    	        int rowsAffected = pstmt.executeUpdate();
    	        
    	        if (rowsAffected > 0) {
    	            JOptionPane.showMessageDialog(this, 
    	                "Enregistrement réussi!", 
    	                "Succès", 
    	                JOptionPane.INFORMATION_MESSAGE);
    	            resetFields(); // Réinitialisation des champs après un enregistrement réussi
    	        } else {
    	            JOptionPane.showMessageDialog(this, 
    	                "Échec de l'enregistrement", 
    	                "Erreur", 
    	                JOptionPane.ERROR_MESSAGE);
    	        }
    	            
    	    } catch (SQLException e) {
    	        JOptionPane.showMessageDialog(this, 
    	            "Erreur lors de l'enregistrement : " + e.getMessage(), 
    	            "Erreur", 
    	            JOptionPane.ERROR_MESSAGE);
    	    }
    	}
	

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

    private JPasswordField createPasswordField(String placeholder, int x, int y) {
        JPasswordField passwordField = new JPasswordField(placeholder);
        passwordField.setBounds(x, y, 292, 38);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                passwordField.setText("");
                passwordField.setForeground(Color.BLACK);
            }
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });
        return passwordField;
    }

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
