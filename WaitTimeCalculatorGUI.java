import java.awt.Component;
import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class WaitTimeCalculatorGUI extends JFrame {

    private WaitTimeCalculator calculator;

    public WaitTimeCalculatorGUI(String url, String utilisateur, String motDePasse) {
        calculator = new WaitTimeCalculator(url, utilisateur, motDePasse);

        // Configurer la fenêtre
        setTitle("Calculateur de temps d'attente");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer les composants Swing
        JLabel titleLabel = new JLabel("Bienvenue au Restaurant");
        JLabel priceLabel = new JLabel("Prix de la dernière commande: ");
        JLabel orderIdLabel = new JLabel("Numéro de la dernière commande: ");
        JLabel waitTimeLabel = new JLabel("Temps d'attente estimé: ");

        // Obtenir l'ID de la dernière commande
        try {
            int lastOrderId = calculator.getLastOrderId();

            // Calculer le temps d'attente
            int waitTime = calculator.calculateWaitTime(lastOrderId);

            // Obtenir le prix de la dernière commande
            double totalAmount = calculator.getTotalAmount(lastOrderId);

            // Mettre à jour les étiquettes
            priceLabel.setText("Prix de la dernière commande: " + totalAmount + " euros");
            orderIdLabel.setText("Numéro de la dernière commande: " + lastOrderId);
            waitTimeLabel.setText("Temps d'attente estimé: " + waitTime + " secondes");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des informations de la dernière commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Configurer la mise en page
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        waitTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ajouter les composants à la fenêtre
        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(priceLabel);
        add(orderIdLabel);
        add(waitTimeLabel);
        add(Box.createVerticalGlue());

        // Afficher la fenêtre
        setVisible(true);
    }

    public static void main(String[] args) {
        // Créer une instance de WaitTimeCalculatorGUI
        SwingUtilities.invokeLater(() -> new WaitTimeCalculatorGUI("jdbc:mariadb://localhost:3307/RestaurantDB", "root", "123123"));
    }
}
