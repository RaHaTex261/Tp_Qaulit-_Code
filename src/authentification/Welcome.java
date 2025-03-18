package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Welcome extends JFrame {
    private static final long serialVersionUID = 1L;
    private String userEmail;
    String dbUrl = "jdbc:sqlite:auth.sqlite";

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
                Modification modificationForm = new Modification();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Welcome("test@example.com").setVisible(true));
    }
}