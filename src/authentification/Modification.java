package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Classe représentant la fenêtre de modification des informations utilisateur.
 * Cette classe permet à l'utilisateur de modifier ses informations, telles que 
 * son prénom, son nom, son email, et son mot de passe.
 * Elle inclut des champs de texte pour la saisie des nouvelles informations et 
 * des boutons pour sauvegarder ou annuler les modifications.
 * 
 * @author TEX BELOHA &amp; David
 * @version 2025
 */
public class Modification extends JFrame {
	 /**
     * Chemin d'accès au fichier de base de données SQLite.
     */
    private static final String FILE_PATH = "auth.sqlite";

    /**
     * URL de la base de données pour la connexion JDBC.
     */
    private static final String DB_URL = "jdbc:sqlite:" + FILE_PATH;

    /**
     * Connexion à la base de données SQLite.
     */
    private static Connection conn;

    /**
     * Champ de texte permettant à l'utilisateur de saisir son prénom.
     */
    private JTextField txtFirstName;

    /**
     * Champ de texte permettant à l'utilisateur de saisir son nom de famille.
     */
    private JTextField txtLastName;

    /**
     * Champ de texte permettant à l'utilisateur de saisir son adresse e-mail.
     */
    private JTextField txtEmail;

    /**
     * Champ de texte permettant à l'utilisateur de saisir son mot de passe.
     */
    private JPasswordField txtPassword;

    /**
     * Champ de texte permettant à l'utilisateur de confirmer son mot de passe.
     */
    private JPasswordField txtConfirmPassword;

    /**
     * Bouton permettant à l'utilisateur de s'inscrire.
     */
    private JButton btnRegister;

    /**
     * Bouton permettant à l'utilisateur de réinitialiser les champs de saisie.
     */
    private JButton btnReset;
    /**
     * Variable pour stocker l'identifiant unique de l'utilisateur.
     * Cet ID est utilisé pour identifier un utilisateur spécifique dans la base de données
     * ou dans l'application pour modifier ou récupérer ses informations.
     */
    private int userId;  // Variable pour stocker l'ID de l'utilisateur

    static Connection getConnection() {
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

    /**
     * Constructeur de la classe Modification.
     * Ce constructeur initialise la fenêtre de modification avec les informations
     * de l'utilisateur spécifié par son identifiant.
     * 
     * @param userId L'identifiant de l'utilisateur dont les informations sont à modifier.
     */
    
    public Modification(int userId) {
        this.userId = userId;  // Stocke l'ID de l'utilisateur
        setTitle("Modification");
        setSize(400, 610);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(null);
        getContentPane().add(rootPanel);
        
        JLabel lblRegistration = new JLabel("Modification");
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
        
        btnRegister = new JButton("Enregistrer");
        btnRegister.setBounds(50, 425, 292, 38);
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRegister.setBackground(Color.BLUE);
        btnRegister.setForeground(Color.WHITE);
        rootPanel.add(btnRegister);
        btnRegister.addActionListener(e -> registerUser());
        
        JButton btnReturn = new JButton("Retourner");
        btnReturn.setBounds(50, 485, 292, 38);
        btnReturn.setFont(new Font("Arial", Font.PLAIN, 16));
        btnReturn.setBackground(Color.RED);
        btnReturn.setForeground(Color.WHITE);
        rootPanel.add(btnReturn);
        btnReturn.addActionListener(e -> {
            dispose(); // Ferme la fenêtre Modification
            SwingUtilities.invokeLater(() -> {
                Welcome welcome = new Welcome("");
                welcome.setVisible(true);
            });
        });
        
        createTable();
        loadUserData(userId);  // Charge les informations de l'utilisateur dans les champs
    }

    /**
     * Constructeur de la classe Modification.
     * Ce constructeur initialise la fenêtre de modification avec les informations
     * de l'utilisateur spécifié par son adresse e-mail.
     * 
     * @param userEmail L'adresse e-mail de l'utilisateur dont les informations sont à modifier.
     */
    
    public Modification(String userEmail) {
		// TODO Auto-generated constructor stub
	}

	private void loadUserData(int userId) {
        String sql = "SELECT first_name, last_name, email, password FROM utilisateurs WHERE id = ?";
        
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);  // Définit l'ID de l'utilisateur dans la requête
            
            ResultSet resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                // Récupérer les informations et les afficher dans les champs
                txtFirstName.setText(resultSet.getString("first_name"));
                txtLastName.setText(resultSet.getString("last_name"));
                txtEmail.setText(resultSet.getString("email"));
                txtPassword.setText(resultSet.getString("password"));  // Assurez-vous que c'est bien le bon format
                txtConfirmPassword.setText(resultSet.getString("password"));  // Mettre la même valeur pour la confirmation du mot de passe
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Utilisateur introuvable", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des données : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
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
            UPDATE utilisateurs
            SET first_name = ?, last_name = ?, email = ?, password = ?
            WHERE id = ?
            """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setInt(5, userId);  // Met l'ID de l'utilisateur à mettre à jour
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Enregistrement réussi!", 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
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
    
    /**
     * Méthode principale qui lance l'application de modification des informations utilisateur.
     * Elle crée une instance de la fenêtre de modification et la rend visible.
     * 
     * @param args Arguments de la ligne de commande (non utilisés ici).
     */
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Modification modification = new Modification(1); // Exemple avec l'ID de l'utilisateur
            modification.setVisible(true);
        });
    }
}
