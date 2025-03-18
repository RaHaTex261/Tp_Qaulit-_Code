package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

/**
 * Cette classe représente l'interface utilisateur de bienvenue après qu'un utilisateur se soit connecté.
 * Elle permet à l'utilisateur de voir son adresse e-mail, de se déconnecter, de modifier ses informations
 * ou de supprimer son compte.
 * 
 * @author TEX BELOHA
 * @version 2025
 */
public class Welcome extends JFrame {
	/**
	 * UID pour la sérialisation de la classe. Il est nécessaire pour la gestion
	 * des versions de la classe lors de la sérialisation/désérialisation.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Adresse e-mail de l'utilisateur.
	 * Cette variable stocke l'adresse e-mail de l'utilisateur connecté, 
	 * ce qui permet de l'afficher dans l'interface utilisateur ou de l'utiliser 
	 * pour les opérations liées à l'utilisateur.
	 */
	private String userEmail;

	/**
	 * URL de connexion à la base de données SQLite.
	 * Cette URL spécifie le chemin de la base de données 'auth.sqlite' qui est utilisée 
	 * pour stocker les informations des utilisateurs, comme leurs informations de connexion.
	 */
	String dbUrl = "jdbc:sqlite:auth.sqlite";


    /**
     * Constructeur de la classe Welcome.
     * 
     * @param userEmail L'adresse e-mail de l'utilisateur qui s'est connecté.
     */
    public Welcome(String userEmail) { 
        this.userEmail = userEmail; // Assigner correctement l'email

        setTitle("Bienvenue");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Bienvenue, " + userEmail, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.LIGHT_GRAY);

        JLabel lblCopyright = new JLabel("Copyright A11 & RaHaTex - 2025", SwingConstants.CENTER);
        lblCopyright.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(lblCopyright, BorderLayout.CENTER);

        panel.add(footerPanel, BorderLayout.SOUTH);
        add(panel);

        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem logoutMenuItem = new JMenuItem("Déconnexion");
        logoutMenuItem.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(Welcome.this, "Déconnexion effectuée", "Déconnexion", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            SwingUtilities.invokeLater(() -> new Login().setVisible(true));
        });

        JMenuItem modifyMenuItem = new JMenuItem("Modification");
        modifyMenuItem.addActionListener(e -> {
            dispose(); // Ferme la fenêtre Welcome
            SwingUtilities.invokeLater(() -> {
                Modification modificationForm = new Modification(userEmail);
                modificationForm.setVisible(true);
            });
        });

        JMenuItem deleteUserMenuItem = new JMenuItem("Supprimer compte");
        deleteUserMenuItem.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    Welcome.this,
                    "Êtes-vous sûr de vouloir supprimer définitivement cet utilisateur ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                boolean isDeleted = deleteUser(this.userEmail);
                if (isDeleted) {
                    JOptionPane.showMessageDialog(Welcome.this, "Utilisateur supprimé définitivement.", "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(Welcome.this, "Erreur lors de la suppression de l'utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        optionsMenu.add(logoutMenuItem);
        optionsMenu.add(modifyMenuItem);
        optionsMenu.add(deleteUserMenuItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Supprime un utilisateur de la base de données en fonction de son adresse e-mail.
     * 
     * @param email L'adresse e-mail de l'utilisateur à supprimer.
     * @return true si l'utilisateur a été supprimé avec succès, false sinon.
     */
    private boolean deleteUser(String email) {
        if (email == null || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun utilisateur sélectionné.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            String query = "DELETE FROM utilisateurs WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, email);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'utilisateur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Point d'entrée principal de l'application pour tester la classe Welcome.
     * 
     * @param args Arguments de ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Welcome("test@example.com").setVisible(true));
    }
}
