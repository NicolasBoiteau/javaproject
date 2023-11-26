
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class BurgerMenuSwing extends JFrame {

    private JTextField textFieldID;
    private JTextField textFieldNom;
    private JTextField textFieldDescription;
    private JTextField textFieldPrix;
    private JTextField textFieldBread;
    private JTextField textFieldMeat;
    private JTextField textFieldCheese;
    private JTextField textFieldSalad;
    private JTextField textFieldTomato;
    private JTextField textFieldOnions;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new BurgerMenuSwing();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public BurgerMenuSwing() throws ClassNotFoundException, SQLException {
        super("Ajouter un Burger au Menu");

        // Informations de connexion à la base de données
        String url = "jdbc:mariadb://localhost:3307/RestaurantDB";
        String utilisateur = "root";
        String motDePasse = "123123";


        // Créer les composants Swing
        JLabel labelID = new JLabel("ID du Burger:");
        JLabel labelNom = new JLabel("Nom du Burger:");
        JLabel labelDescription = new JLabel("Description:");
        JLabel labelPrix = new JLabel("Prix:");
        JLabel labelBread = new JLabel("Bread:");
        JLabel labelMeat = new JLabel("Meat:");
        JLabel labelCheese = new JLabel("Cheese:");
        JLabel labelSalad = new JLabel("Salad:");
        JLabel labelTomato = new JLabel("Tomato:");
        JLabel labelOnions = new JLabel("Onions:");

        textFieldID = new JTextField(20);
        textFieldNom = new JTextField(20);
        textFieldDescription = new JTextField(20);
        textFieldPrix = new JTextField(20);
        textFieldBread = new JTextField(20);
        textFieldMeat = new JTextField(20);
        textFieldCheese = new JTextField(20);
        textFieldSalad = new JTextField(20);
        textFieldTomato = new JTextField(20);
        textFieldOnions = new JTextField(20);

        JButton buttonAjouter = new JButton("Ajouter au Menu");

        // Gérer l'événement du bouton Ajouter
        buttonAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterBurgerAuMenu(url, utilisateur, motDePasse);
            }
        });

        // Configurer la mise en page
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 0;
        gc.gridy = 0;
        add(labelID, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        add(textFieldID, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        add(labelNom, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        add(textFieldNom, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        add(labelDescription, gc);

        gc.gridx = 1;
        gc.gridy = 2;
        add(textFieldDescription, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        add(labelPrix, gc);

        gc.gridx = 1;
        gc.gridy = 3;
        add(textFieldPrix, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        add(labelBread, gc);

        gc.gridx = 1;
        gc.gridy = 4;
        add(textFieldBread, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        add(labelMeat, gc);

        gc.gridx = 1;
        gc.gridy = 5;
        add(textFieldMeat, gc);

        gc.gridx = 0;
        gc.gridy = 6;
        add(labelCheese, gc);

        gc.gridx = 1;
        gc.gridy = 6;
        add(textFieldCheese, gc);

        gc.gridx = 0;
        gc.gridy = 7;
        add(labelSalad, gc);

        gc.gridx = 1;
        gc.gridy = 7;
        add(textFieldSalad, gc);

        gc.gridx = 0;
        gc.gridy = 8;
        add(labelTomato, gc);

        gc.gridx = 1;
        gc.gridy = 8;
        add(textFieldTomato, gc);

        gc.gridx = 0;
        gc.gridy = 9;
        add(labelOnions, gc);

        gc.gridx = 1;
        gc.gridy = 9;
        add(textFieldOnions, gc);

        gc.gridx = 1;
        gc.gridy = 10;
        add(buttonAjouter, gc);

        // Configurer la fenêtre
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void ajouterBurgerAuMenu(String url, String utilisateur, String motDePasse) {
        String sql = "INSERT INTO Menu (ItemID, ItemName, Description, Price, Type, Bread, Meat, Cheese, Salad, Tomato, Onions) " +
                "VALUES (?, ?, ?, ?, 'Burger', ?, ?, ?, ?,1, ?)";
    
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement statement = connexion.prepareStatement(sql)) {
    
            // Assuming ItemID is an integer, modify this line accordingly if it's a different type
            statement.setInt(1, Integer.parseInt(textFieldID.getText()));
            statement.setString(2, textFieldNom.getText());
            statement.setString(3, textFieldDescription.getText());
            statement.setDouble(4, Double.parseDouble(textFieldPrix.getText()));
            statement.setInt(5, Integer.parseInt(textFieldBread.getText()));
            statement.setInt(6, Integer.parseInt(textFieldMeat.getText()));
            statement.setInt(7, Integer.parseInt(textFieldCheese.getText()));
            statement.setInt(8, Integer.parseInt(textFieldSalad.getText()));
            statement.setInt(9, Integer.parseInt(textFieldTomato.getText()));
            statement.setInt(10, Integer.parseInt(textFieldOnions.getText()));
    
            int lignesAffectees = statement.executeUpdate();
    
            if (lignesAffectees > 0) {
                JOptionPane.showMessageDialog(this, "Le burger a été ajouté au menu avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Échec de l'ajout du burger au menu.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Une erreur s'est produite. Vérifiez la console pour plus de détails.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    
        // Effacer les champs après l'ajout
        textFieldID.setText("");
        textFieldNom.setText("");
        textFieldDescription.setText("");
        textFieldPrix.setText("");
        textFieldBread.setText("");
        textFieldMeat.setText("");
        textFieldCheese.setText("");
        textFieldSalad.setText("");
        textFieldTomato.setText("");
        textFieldOnions.setText("");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
