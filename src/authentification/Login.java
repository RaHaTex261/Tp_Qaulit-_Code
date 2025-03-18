package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe représentant la fenêtre de connexion dans une application.
 * Cette classe gère l'interface utilisateur de la page de connexion, 
 * permettant à un utilisateur de saisir son nom d'utilisateur et son mot de passe.
 * Elle contient également les actions associées aux boutons de connexion et d'inscription.
 * L'objectif est de faciliter l'authentification des utilisateurs dans l'application.
 * 
 * @author TEX BELOHA 
 * @version 2025
 */

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    /**
     * Champ de texte permettant à l'utilisateur de saisir son nom d'utilisateur.
     */
    private JTextField txtUserName;

    /**
     * Champ de texte permettant à l'utilisateur de saisir son mot de passe.
     */
    private JPasswordField txtPassword;

    /**
     * Panneau racine contenant tous les composants de la fenêtre.
     */
    private JPanel rootPanel;

    /**
     * Panneau dédié à l'interface de connexion, contenant les champs et boutons associés.
     */
    private JPanel loginPanel;

    /**
     * Bouton permettant à l'utilisateur de se connecter.
     */
    private JButton btnSignIn;

    /**
     * Bouton permettant à l'utilisateur de s'inscrire.
     */
    private JButton btnSignup;

    /**
     * Requête préparée pour interagir avec la base de données lors de la connexion.
     */
    private PreparedStatement pstmt;

    /**
     * Résultat de la requête exécutée, utilisé pour récupérer les données après une tentative de connexion.
     */
    private ResultSet rs;

    /**
     * Constructeur de la classe Login. Initialise l'interface graphique de la
     * fenêtre de connexion.
     */
    public Login() {
        // Initialisation de la fenêtre
        setTitle("Login");
        setSize(400, 510);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialisation du panneau principal
        rootPanel = new JPanel();
        rootPanel.setLayout(null);
        getContentPane().add(rootPanel);

        // Panneau de connexion
        loginPanel = new JPanel();
        loginPanel.setBounds(-1, -1, 406, 438);
        loginPanel.setLayout(null);
        rootPanel.add(loginPanel);

        // Champ de texte pour l'email
        txtUserName = new JTextField();
        txtUserName.setText("Enter your email");
        txtUserName.setBounds(45, 117, 292, 38);
        txtUserName.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUserName.setBackground(Color.WHITE);
        txtUserName.setForeground(Color.BLACK);

        // Gestion du focus du champ email
        txtUserName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtUserName.getText().equals("Enter your email")) {
                    txtUserName.setText("");
                    txtUserName.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtUserName.getText().isEmpty()) {
                    txtUserName.setText("Enter your email");
                    txtUserName.setForeground(Color.GRAY);
                }
            }
        });
        loginPanel.add(txtUserName);

        // Champ de texte pour le mot de passe
        txtPassword = new JPasswordField();
        txtPassword.setText("Enter your password");
        txtPassword.setBounds(45, 179, 292, 38);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setForeground(Color.BLACK);

        // Gestion du focus du champ mot de passe
        txtPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(txtPassword.getPassword()).equals("Enter your password")) {
                    txtPassword.setText("");
                    txtPassword.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(txtPassword.getPassword()).isEmpty()) {
                    txtPassword.setText("Enter your password");
                    txtPassword.setForeground(Color.GRAY);
                }
            }
        });
        loginPanel.add(txtPassword);

        // Label du titre "Login"
        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBounds(45, 28, 100, 30);
        lblLogin.setFont(new Font("Arial Black", Font.BOLD, 23));
        lblLogin.setForeground(new Color(43, 11, 156));
        loginPanel.add(lblLogin);

        // Séparateur horizontal
        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setBounds(45, 73, 301, 7);
        loginPanel.add(separator1);

        // Bouton "SignIn"
        btnSignIn = new JButton("SignIn");
        btnSignIn.setBounds(50, 285, 292, 38);
        btnSignIn.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSignIn.setBackground(Color.BLUE);
        btnSignIn.setForeground(Color.WHITE);
        loginPanel.add(btnSignIn);
        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtUserName.getText();
                String password = String.valueOf(txtPassword.getPassword());

                if (validateLogin(email, password)) {
                    JOptionPane.showMessageDialog(null, "Vous êtes connecté !");

                    // Ouvrir la fenêtre Welcome en passant l'email de l'utilisateur connecté
                    Welcome welcomeForm = new Welcome(email);
                    welcomeForm.setVisible(true);

                    // Fermer la fenêtre de connexion
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Email ou mot de passe incorrect.");
                }
            }
        });

        // Bouton "SignUp"
        btnSignup = new JButton("SignUp");
        btnSignup.setBounds(50, 374, 292, 38);
        btnSignup.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSignup.setBackground(Color.RED);
        btnSignup.setForeground(Color.WHITE);
        btnSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir la fenêtre d'inscription
                Registration registrationForm = new Registration();
                registrationForm.setVisible(true);

                // Fermer la fenêtre de connexion (optionnel)
                dispose();
            }
        });

        loginPanel.add(btnSignup);

        // Création du label "OR"
        JLabel lblOr = new JLabel("OR");
        lblOr.setBounds(179, 331, 100, 30);
        lblOr.setFont(new Font("Arial Black", Font.BOLD, 16));
        loginPanel.add(lblOr);

        // Création du séparateur horizontal
        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setBounds(49, 236, 301, 7);
        loginPanel.add(separator2);

        // Création du JPanel pour le pied de page
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBounds(0, 442, 385, 30);
        footerPanel.setBackground(Color.WHITE);

        // Ajout du JPanel au rootPanel
        rootPanel.add(footerPanel);

        // Création du JLabel pour le texte du pied de page
        JLabel lblCopyrightA = new JLabel("Copyright A11 & RaHaTex - 2025");
        lblCopyrightA.setHorizontalAlignment(SwingConstants.CENTER);
        lblCopyrightA.setFont(new Font("Arial", Font.PLAIN, 12));

        // Ajout du JLabel dans le footerPanel
        footerPanel.add(lblCopyrightA, BorderLayout.CENTER);

        // Centrage du champ de texte pour le nom d'utilisateur
        setLocationRelativeTo(null);

        SwingUtilities.invokeLater(() -> {
            txtUserName.requestFocusInWindow();
        });
    }

    /**
     * Méthode pour valider l'email et le mot de passe avec les données de
     * la base de données.
     * 
     * @param email    L'email à valider.
     * @param password Le mot de passe à valider.
     * @return true si les informations de connexion sont correctes, false sinon.
     */
    private boolean validateLogin(String email, String password) {
        try {
            // Connexion à la base de données
            Connection conn = Registration.getConnection();
            if (conn == null) {
                throw new SQLException("Erreur de connexion à la base de données");
            }

            // Configuration de la transaction
            conn.setAutoCommit(false);

            try {
                // Vérification des identifiants
                String sql = "SELECT email, password FROM utilisateurs WHERE email = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, email.trim());

                ResultSet rs = pstmt.executeQuery();

                boolean isValid = false;
                if (rs.next()) {
                    String storedEmail = rs.getString("email");
                    String storedPassword = rs.getString("password");

                    if (storedEmail.equalsIgnoreCase(email.trim()) && 
                        storedPassword.equals(password.trim())) {
                        isValid = true;
                    }
                }

                // Validation de la transaction
                conn.commit();
                return isValid;

            } catch (SQLException e) {
                // Annulation de la transaction en cas d'erreur
                conn.rollback();
                throw e;
            } finally {
                // Fermeture des ressources
                try {
                    if (rs != null) {
                        ((ResultSet)rs).close();
                    }
                    if (pstmt != null) {
                        ((PreparedStatement)pstmt).close();
                    }
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de la connexion : " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Méthode main pour démarrer l'application de connexion.
     * 
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginForm = new Login();
            loginForm.setVisible(true);
        });
    }
}
