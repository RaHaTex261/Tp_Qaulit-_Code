package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Classe représentant la fenêtre de connexion.
 * Cette classe gère l'interface utilisateur de la page de connexion,
 * y compris les champs de texte pour le nom d'utilisateur et le mot de passe,
 * ainsi que les boutons pour la connexion et l'inscription.
 */
public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtUserName;
    private JPasswordField txtPassword;
    private JPanel rootPanel;
    private JPanel loginPanel;
    private JButton btnSignIn;
    private JButton btnSignup;

    /**
     * Constructeur de la classe Login.
     * Initialise l'interface graphique de la fenêtre de connexion.
     */
    public Login() {
        
        setTitle("Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        rootPanel = new JPanel();
        rootPanel.setLayout(null);
        getContentPane().add(rootPanel);
        
        
        loginPanel = new JPanel();
        loginPanel.setBounds(-1, -1, 406, 438);
        loginPanel.setLayout(null);
        rootPanel.add(loginPanel);
        
      
        txtUserName = new JTextField();
        txtUserName.setText("Enter your email");
        txtUserName.setBounds(45, 117, 292, 38);
        txtUserName.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUserName.setBackground(Color.WHITE);
        txtUserName.setForeground(Color.BLACK);
        
 
        txtUserName.addFocusListener(new FocusListener() {
            /**
             * Gère l'événement lorsque le champ de texte pour l'email reçoit le focus.
             * Efface le texte par défaut si l'utilisateur clique dans le champ.
             */
            @Override
            public void focusGained(FocusEvent e) {
                if (txtUserName.getText().equals("Enter your email")) {
                    txtUserName.setText("");
                    txtUserName.setForeground(Color.BLACK);
                }
            }

            /**
             * Gère l'événement lorsque le champ de texte pour l'email perd le focus.
             * Restaure le texte par défaut si l'utilisateur n'a pas saisi de valeur.
             */
            @Override
            public void focusLost(FocusEvent e) {
                if (txtUserName.getText().isEmpty()) {
                    txtUserName.setText("Enter your email");
                    txtUserName.setForeground(Color.GRAY);
                }
            }
        });
        loginPanel.add(txtUserName);
        

        txtPassword = new JPasswordField();
        txtPassword.setText("Enter your password");
        txtPassword.setBounds(45, 179, 292, 38);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setForeground(Color.BLACK);
        
        
        txtPassword.addFocusListener(new FocusListener() {
            /**
             * Gère l'événement lorsque le champ de mot de passe reçoit le focus.
             * Efface le texte par défaut si l'utilisateur clique dans le champ.
             */
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(txtPassword.getPassword()).equals("Enter your password")) {
                    txtPassword.setText("");
                    txtPassword.setForeground(Color.BLACK);
                }
            }

            /**
             * Gère l'événement lorsque le champ de mot de passe perd le focus.
             * Restaure le texte par défaut si l'utilisateur n'a pas saisi de valeur.
             */
            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(txtPassword.getPassword()).isEmpty()) {
                    txtPassword.setText("Enter your password");
                    txtPassword.setForeground(Color.GRAY);
                }
            }
        });
        loginPanel.add(txtPassword);
        
        
        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBounds(45, 28, 100, 30);
        lblLogin.setFont(new Font("Arial Black", Font.BOLD, 23));
        lblLogin.setForeground(new Color(43, 11, 156));
        loginPanel.add(lblLogin);
        
        
        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setBounds(45, 73, 301, 7);
        loginPanel.add(separator1);
        
        
        btnSignIn = new JButton("SignIn");
        btnSignIn.setBounds(50, 285, 292, 38);
        btnSignIn.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSignIn.setBackground(Color.BLUE);
        btnSignIn.setForeground(Color.WHITE);
        loginPanel.add(btnSignIn);
        
        
        btnSignup = new JButton("SignUp");
        btnSignup.setBounds(50, 374, 292, 38);
        btnSignup.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSignup.setBackground(Color.RED);
        btnSignup.setForeground(Color.WHITE);
        
        
        btnSignup.addActionListener(new ActionListener() {
            /**
             * Gère l'événement lors du clic sur le bouton d'inscription.
             * Ouvre le formulaire d'inscription et ferme la fenêtre de connexion.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Créer l'interface d'inscription
                Registration registrationForm = new Registration();
                registrationForm.setTitle("Registration");
                registrationForm.setSize(400, 600);
                registrationForm.setVisible(true);
                
               
                dispose();
            }
        });
        loginPanel.add(btnSignup);
        
        
        JLabel lblOr = new JLabel("OR");
        lblOr.setBounds(179, 331, 100, 30);
        lblOr.setFont(new Font("Arial Black", Font.BOLD, 16));
        loginPanel.add(lblOr);
        
      
        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setBounds(49, 236, 301, 7);
        loginPanel.add(separator2);
        
        
        setLocationRelativeTo(null);
        

        SwingUtilities.invokeLater(() -> {
            txtUserName.requestFocusInWindow();
        });
        

        btnSignIn.addActionListener(new ActionListener() {
            /**
             * Gère l'événement lors du clic sur le bouton de connexion.
             * Valide l'email et le mot de passe et affiche un message de connexion réussi ou échoué.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtUserName.getText();
                String password = String.valueOf(txtPassword.getPassword());

               
                if (validateLogin(email, password)) {
                    JOptionPane.showMessageDialog(null, "Vous êtes connecté !");
                   
                    dispose();
                   
                } else {
                    JOptionPane.showMessageDialog(null, "Email ou mot de passe incorrect.");
                }
            }
        });
    }

    /**
     * Méthode pour valider l'email et le mot de passe avec les données de users.txt.
     * @param email L'email à valider.
     * @param password Le mot de passe à valider.
     * @return true si les informations de connexion sont correctes, false sinon.
     */
    private boolean validateLogin(String email, String password) {
        try {
           
            if (email == null || !email.contains("@")) {
                throw new CustomException("L'email doit contenir un '@'.");
            }


            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;

   
            while ((line = reader.readLine()) != null) {
                
                String[] user = line.split(";");

                
                if (user.length == 4) {
                    String storedEmail = user[2].trim();  
                    String storedPassword = user[3].trim();  
                    
                    if (storedEmail.equalsIgnoreCase(email.trim()) && storedPassword.equals(password.trim())) {
                       
                        reader.close();
                        return true;
                    }
                }
            }

            
            reader.close();
            throw new CustomException("Email ou mot de passe incorrect.");

        } catch (IOException e) {
            
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur d'accès au fichier des utilisateurs.");
        } catch (CustomException e) {
            
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        
        return false;
    }

    /**
     * Méthode main pour démarrer l'application de connexion.
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginForm = new Login();
            loginForm.setVisible(true);
        });
    }
}
