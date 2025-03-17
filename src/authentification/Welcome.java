package authentification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JFrame {
    private static final long serialVersionUID = 1L;

    public Welcome() {
        // Titre de la fenêtre
        setTitle("Bienvenue");
        setSize(400, 300);  // Augmenté la taille pour avoir plus d'espace
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création du panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Message de bienvenue
        JLabel welcomeLabel = new JLabel("Bienvenue !", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Ajout du message au panneau
        panel.add(welcomeLabel, BorderLayout.CENTER);

        // Création et ajout du pied de page
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBackground(Color.LIGHT_GRAY); // Fond du pied de page

        // Création du JLabel pour le texte du pied de page
        JLabel lblCopyrightA = new JLabel("Copyright A11 & RaHaTex - 2025");
        lblCopyrightA.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte horizontalement
        lblCopyrightA.setFont(new Font("Arial", Font.PLAIN, 12)); // Choisir la police et la taille du texte

        // Ajout du JLabel dans le footerPanel
        footerPanel.add(lblCopyrightA, BorderLayout.CENTER);

        // Ajout du footerPanel au bas du panel principal
        panel.add(footerPanel, BorderLayout.SOUTH);

        // Ajout du panneau principal à la fenêtre
        add(panel);

        // Ajout du menu
        JMenuBar menuBar = new JMenuBar();

        // Création du menu
        JMenu optionsMenu = new JMenu("Options");
        
        // Option "Déconnexion"
        JMenuItem logoutMenuItem = new JMenuItem("Déconnexion");
        logoutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action pour la déconnexion
                JOptionPane.showMessageDialog(Welcome.this, "Déconnexion effectuée", "Déconnexion", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Option "Modification"
        JMenuItem modifyMenuItem = new JMenuItem("Modification");
        modifyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action pour la modification
                JOptionPane.showMessageDialog(Welcome.this, "Modification de profil", "Modification", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Option "Suppression de l'utilisateur"
        JMenuItem deleteUserMenuItem = new JMenuItem("Supprimer l'utilisateur");
        deleteUserMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Afficher une boîte de dialogue de confirmation
                int option = JOptionPane.showConfirmDialog(
                        Welcome.this,
                        "Êtes-vous sûr de vouloir supprimer définitivement cet utilisateur ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION
                );

                // Si l'utilisateur confirme, effectuer la suppression
                if (option == JOptionPane.YES_OPTION) {
                    // Ici vous pouvez ajouter la logique de suppression (par exemple, supprimer de la base de données)
                    JOptionPane.showMessageDialog(Welcome.this, "Utilisateur supprimé définitivement.", "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);

                    // Fermer la fenêtre après la suppression (ou vous pouvez rediriger vers une autre fenêtre)
                    System.exit(0);  // Ferme l'application
                }
            }
        });

        // Ajouter les options au menu
        optionsMenu.add(logoutMenuItem);
        optionsMenu.add(modifyMenuItem);
        optionsMenu.add(deleteUserMenuItem);  // Ajouter l'option de suppression

        // Ajouter le menu à la barre de menu
        menuBar.add(optionsMenu);

        // Ajouter la barre de menu à la fenêtre
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        // Création et affichage de la fenêtre
        SwingUtilities.invokeLater(() -> {
            Welcome frame = new Welcome();
            frame.setVisible(true);
        });
    }
}
