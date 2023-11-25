package javaproject;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class BurgerOrderApp extends JFrame {

    private JComboBox<String> burgerComboBox;
    private String url;
    private String utilisateur;
    private String motDePasse;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new BurgerOrderApp();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public BurgerOrderApp() throws ClassNotFoundException, SQLException {
        super("Commander un Burger");

        // Informations de connexion à la base de données
        url = "jdbc:mariadb://localhost:3307/RestaurantDB";
        utilisateur = "root";
        motDePasse = "123123";

        // Charger le pilote JDBC
        Class.forName("org.mariadb.jdbc.Driver");

        // Créer les composants Swing
        JLabel labelBurger = new JLabel("Sélectionnez un Burger:");
        burgerComboBox = new JComboBox<>(getBurgerNames());
        JButton buttonCommander = new JButton("Commander");

        // Gérer l'événement du bouton Commander
        buttonCommander.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commanderBurger();
            }
        });

        // Configurer la mise en page
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 0;
        gc.gridy = 0;
        add(labelBurger, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        add(burgerComboBox, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        add(buttonCommander, gc);

        // Configurer la fenêtre
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String[] getBurgerNames() throws SQLException {
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
                Statement statement = connexion.createStatement()) {
            String query = "SELECT ItemName FROM Menu WHERE Type = 'Burger'";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                resultSet.last();
                int rowCount = resultSet.getRow();
                String[] burgerNames = new String[rowCount];
                resultSet.beforeFirst();

                int index = 0;
                while (resultSet.next()) {
                    burgerNames[index] = resultSet.getString("ItemName");
                    index++;
                }

                return burgerNames;
            }
        }
    }

    private void commanderBurger() {
        String selectedBurger = (String) burgerComboBox.getSelectedItem();

        if (selectedBurger != null) {
            try {
                // Obtenez le coût du burger depuis la base de données
                double burgerCost = getBurgerCost(selectedBurger);

                // Demandez à l'utilisateur la quantité
                String quantityString = JOptionPane.showInputDialog(BurgerOrderApp.this,
                        "Combien de " + selectedBurger + " souhaitez-vous commander?");
                if (quantityString != null) {
                    try {
                        int quantity = Integer.parseInt(quantityString);

                        // Calculez le coût total de la commande
                        double totalCost = burgerCost * quantity;

                        // Insérez la commande dans la base de données
                        insertOrderIntoDatabase(selectedBurger, quantity, totalCost);
                        JOptionPane.showMessageDialog(BurgerOrderApp.this, "Commande passée pour " + selectedBurger);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(BurgerOrderApp.this, "Veuillez entrer une quantité valide.",
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(BurgerOrderApp.this, "Erreur lors de la commande.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(BurgerOrderApp.this, "Veuillez sélectionner un burger.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private double getBurgerCost(String burgerName) throws SQLException {
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse)) {
            String query = "SELECT Price FROM Menu WHERE ItemName = ?";
            try (PreparedStatement statement = connexion.prepareStatement(query)) {
                statement.setString(1, burgerName);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getDouble("Price");
                } else {
                    throw new SQLException("Le burger spécifié n'a pas été trouvé.");
                }
            }
        }
    }

    private void insertOrderIntoDatabase(String selectedBurger, int quantity, double totalCost) {
        String sql = "INSERT INTO Orders (CustomerName, OrderDate, TotalAmount) VALUES (?, CURRENT_TIMESTAMP, ?)";

        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement statement = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, getCustomerName());
            statement.setDouble(2, totalCost);

            int lignesAffectees = statement.executeUpdate();

            if (lignesAffectees > 0) {
                JOptionPane.showMessageDialog(this, "Commande passée avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Une erreur s'est produite. Vérifiez la console pour plus de détails.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getCustomerName() {
        return JOptionPane.showInputDialog(this, "Veuillez entrer votre nom:");
    }
}
