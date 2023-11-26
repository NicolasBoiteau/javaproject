import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class StockAdditionGUI extends JFrame {

    private JTextField breadField;
    private JTextField meatField;
    private JTextField cheeseField;
    private JTextField saladField;
    private JTextField tomatoField;
    private JTextField onionsField;

    public StockAdditionGUI() {
        super("Ajouter du Stock");

        breadField = new JTextField(10);
        meatField = new JTextField(10);
        cheeseField = new JTextField(10);
        saladField = new JTextField(10);
        tomatoField = new JTextField(10);
        onionsField = new JTextField(10);

        JButton addButton = new JButton("Ajouter au Stock");

    

        setLayout(new GridLayout(7, 2));
        add(new JLabel("Quantité de Pain:"));
        add(breadField);
        add(new JLabel("Quantité de Viande:"));
        add(meatField);
        add(new JLabel("Quantité de Fromage:"));
        add(cheeseField);
        add(new JLabel("Quantité de Salade:"));
        add(saladField);
        add(new JLabel("Quantité de Tomate:"));
        add(tomatoField);
        add(new JLabel("Quantité d'Oignons:"));
        add(onionsField);
        add(addButton);

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void deductIngredientsFromStock(int quantity, String selectedBurger) {
        String url = "jdbc:mariadb://localhost:3307/RestaurantDB";
        String utilisateur = "root";
        String motDePasse = "123123";
    
        try {
            Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
    
            // Récupérer les informations du burger depuis la table Menu
            String query = "SELECT * FROM Menu WHERE ItemName = ?";
            try (PreparedStatement infoStatement = connexion.prepareStatement(query)) {
                infoStatement.setString(1, selectedBurger); // Utiliser setString au lieu de setInt
                ResultSet resultSet = infoStatement.executeQuery();
    
                if (resultSet.next()) {
                    int breadNeeded = resultSet.getInt("Bread") * quantity;
                    int meatNeeded = resultSet.getInt("Meat") * quantity;
                    int cheeseNeeded = resultSet.getInt("Cheese") * quantity;
                    int saladNeeded = resultSet.getInt("Salad") * quantity;
                    int tomatoNeeded = resultSet.getInt("Tomato") * quantity;
                    int onionsNeeded = resultSet.getInt("Onions") * quantity;
    
                    // Mettre à jour le stock
                    String updateSql = "UPDATE Stock SET Bread = Bread - ?, Meat = Meat - ?, Cheese = Cheese - ?, Salad = Salad - ?, Tomato = Tomato - ?, Onions = Onions - ?";
                    try (PreparedStatement updateStatement = connexion.prepareStatement(updateSql)) {
                        updateStatement.setInt(1, breadNeeded);
                        updateStatement.setInt(2, meatNeeded);
                        updateStatement.setInt(3, cheeseNeeded);
                        updateStatement.setInt(4, saladNeeded);
                        updateStatement.setInt(5, tomatoNeeded);
                        updateStatement.setInt(6, onionsNeeded);
    
                        updateStatement.executeUpdate();
                    }
                }
            }
    
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    public void addStockToDatabase(int bread, int meat, int cheese, int salad, int tomato, int onions) {
        String url = "jdbc:mariadb://localhost:3307/RestaurantDB";
        String utilisateur = "root";
        String motDePasse = "123123";
    
        try {
            Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
    
            String sql = "UPDATE Stock SET Bread = Bread + ?, Meat = Meat + ?, Cheese = Cheese + ?, Salad = Salad + ?, Tomato = Tomato + ?, Onions = Onions + ?";
    
            try (PreparedStatement statement = connexion.prepareStatement(sql)) {
                statement.setInt(1, bread);
                statement.setInt(2, meat);
                statement.setInt(3, cheese);
                statement.setInt(4, salad);
                statement.setInt(5, tomato);
                statement.setInt(6, onions);
    
                statement.executeUpdate();
            }
    
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StockAdditionGUI());
    }
}
