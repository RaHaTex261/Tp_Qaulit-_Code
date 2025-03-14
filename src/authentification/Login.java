package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtUserName;
    private JPasswordField txtPassword;
    private JPanel rootPanel;
    private JPanel loginPanel;
    private JButton btnSignIn;
    private JButton btnSignup;

    public Login() {
        // Configuration de la fenêtre principale
        setTitle("Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Configuration du panel principal avec un layout absolu
        rootPanel = new JPanel();
        rootPanel.setLayout(null);
        getContentPane().add(rootPanel);
        
        // Création du panel de connexion
        loginPanel = new JPanel();
        loginPanel.setBounds(-1, -1, 406, 438);
        loginPanel.setLayout(null);
        rootPanel.add(loginPanel);
        
        // Configuration du champ de texte pour le nom d'utilisateur (Email)
        txtUserName = new JTextField();
        txtUserName.setText("Enter your email");
        txtUserName.setBounds(45, 117, 292, 38);
        txtUserName.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUserName.setBackground(Color.WHITE);
        txtUserName.setForeground(Color.BLACK);
        
        // Gestion du focus pour le champ de texte
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
        
        // Configuration du champ de mot de passe
        txtPassword = new JPasswordField();
        txtPassword.setText("Enter your password");
        txtPassword.setBounds(45, 179, 292, 38);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setForeground(Color.BLACK);
        
        // Gestion du focus pour le champ de mot de passe
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
        
        // Ajout du label de titre
        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBounds(45, 28, 100, 30);
        lblLogin.setFont(new Font("Arial Black", Font.BOLD, 23));
        lblLogin.setForeground(new Color(43, 11, 156));
        loginPanel.add(lblLogin);
        
        // Ajout d'un séparateur horizontal
        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setBounds(45, 73, 301, 7);
        loginPanel.add(separator1);
        
        // Configuration du bouton de connexion
        btnSignIn = new JButton("SignIn");
        btnSignIn.setBounds(50, 285, 292, 38);
        btnSignIn.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSignIn.setBackground(Color.BLUE);
        btnSignIn.setForeground(Color.WHITE);
        loginPanel.add(btnSignIn);
        
        // Configuration du bouton d'inscription
        btnSignup = new JButton("SignUp");
        btnSignup.setBounds(50, 374, 292, 38);
        btnSignup.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSignup.setBackground(Color.RED);
        btnSignup.setForeground(Color.WHITE);
        
        // Ajout du gestionnaire d'événements pour le bouton d'inscription
        btnSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Créer l'interface d'inscription
                Registration registrationForm = new Registration();
                registrationForm.setTitle("Registration");
                registrationForm.setSize(400, 600);
                registrationForm.setVisible(true);
                
                // Fermer l'interface de login
                dispose();
            }
        });
        loginPanel.add(btnSignup);
        
        // Ajout du label "OR" entre les boutons
        JLabel lblOr = new JLabel("OR");
        lblOr.setBounds(179, 331, 100, 30);
        lblOr.setFont(new Font("Arial Black", Font.BOLD, 16));
        loginPanel.add(lblOr);
        
        // Ajout d'un second séparateur horizontal
        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setBounds(49, 236, 301, 7);
        loginPanel.add(separator2);
        
        // Centrer la fenêtre sur l'écran
        setLocationRelativeTo(null);
        
        // Demander le focus au composant temporaire
        SwingUtilities.invokeLater(() -> {
            txtUserName.requestFocusInWindow();
        });
        
        // Ajouter l'action de connexion
        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtUserName.getText();
                String password = String.valueOf(txtPassword.getPassword());

                // Valider les informations d'email et mot de passe
                if (validateLogin(email, password)) {
                    JOptionPane.showMessageDialog(null, "Vous êtes connecté !");
                    // Fermer la fenêtre de login après la connexion
                    dispose();
                    // Vous pouvez ouvrir la fenêtre principale de l'application ici
                } else {
                    JOptionPane.showMessageDialog(null, "Email ou mot de passe incorrect.");
                }
            }
        });
    }

    // Méthode pour valider l'email et le mot de passe avec les données de users.txt
    private boolean validateLogin(String email, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(";");
                if (user.length == 4) {  // Assurez-vous qu'il y a bien 4 éléments
                    String storedEmail = user[2];  // L'email est dans la 3ème position (index 2)
                    String storedPassword = user[3];  // Le mot de passe est dans la 4ème position (index 3)

                    // Vérifier si l'email et le mot de passe sont corrects
                    if (storedEmail.equals(email) && storedPassword.equals(password)) {
                        reader.close();
                        return true;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode main corrigée
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginForm = new Login();
            loginForm.setVisible(true);
        });
    }
}
