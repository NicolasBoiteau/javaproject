import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class RestaurantDashboard extends JFrame {

    private JTextArea ordersTextArea;
    private JTextArea menuTextArea;
    private JTextArea stockTextArea;

    private String url;
    private String utilisateur;
    private String motDePasse;

    private static final int REFRESH_INTERVAL = 1; // Rafraîchir toutes les 60 secondes

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new RestaurantDashboard();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public RestaurantDashboard() throws ClassNotFoundException, SQLException {
        super("Tableau de bord du restaurant");

        // Initialiser les informations de connexion
        url = "jdbc:mariadb://localhost:3307/RestaurantDB";
        utilisateur = "root";
        motDePasse = "123123";

        // Créer les composants Swing
        JTabbedPane tabbedPane = new JTabbedPane();
        ordersTextArea = new JTextArea(10, 40);
        menuTextArea = new JTextArea(10, 40);
        stockTextArea = new JTextArea(10, 40);

        // ... (autres initialisations)

        // Ajouter les onglets au panneau d'onglets
        tabbedPane.addTab("Dernières commandes", new JScrollPane(ordersTextArea));
        tabbedPane.addTab("Menu", new JScrollPane(menuTextArea));
        tabbedPane.addTab("Stock", new JScrollPane(stockTextArea));

        // Ajouter le panneau d'onglets à la fenêtre
        add(tabbedPane);

        // Ajouter un Timer pour le rafraîchissement périodique
        Timer timer = new Timer(REFRESH_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Charger les données dans les onglets
                    loadData(url, utilisateur, motDePasse);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Démarrer le Timer
        timer.start();

        // Configurer la fenêtre
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Charger les données initiales dans les onglets
        loadData(url, utilisateur, motDePasse);
    }

    private void loadData(String url, String utilisateur, String motDePasse) throws SQLException {
        // Charger les données dans les JTextAreas
        displayLastOrders(url, utilisateur, motDePasse);
        displayMenu(url, utilisateur, motDePasse);
        displayStock(url, utilisateur, motDePasse);
    }




    private void displayLastOrders(String url, String utilisateur, String motDePasse) throws SQLException {
        StringBuilder ordersText = new StringBuilder();

        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {

            String query = "SELECT * FROM Orders ORDER BY OrderDate DESC LIMIT 5";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int orderId = resultSet.getInt("OrderID");
                    String customerName = resultSet.getString("CustomerName");
                    Date orderDate = resultSet.getTimestamp("OrderDate");
                    double totalAmount = resultSet.getDouble("TotalAmount");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String formattedDate = dateFormat.format(orderDate);

                    ordersText.append("Commande ID: ").append(orderId).append("\n");
                    ordersText.append("Client: ").append(customerName).append("\n");
                    ordersText.append("Date de commande: ").append(formattedDate).append("\n");
                    ordersText.append("Montant total: ").append(totalAmount).append("\n\n");
                }
            }
        }

        // Afficher le texte dans le composant JTextArea
        ordersTextArea.setText(ordersText.toString());
    }

    private void displayMenu(String url, String utilisateur, String motDePasse) throws SQLException {
        StringBuilder menuText = new StringBuilder();

        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {

            String query = "SELECT * FROM Menu";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int itemId = resultSet.getInt("ItemID");
                    String itemName = resultSet.getString("ItemName");
                    String description = resultSet.getString("Description");
                    double price = resultSet.getDouble("Price");

                    menuText.append("ID: ").append(itemId).append("\n");
                    menuText.append("Nom: ").append(itemName).append("\n");
                    menuText.append("Description: ").append(description).append("\n");
                    menuText.append("Prix: ").append(price).append("\n\n");
                }
            }
        }

        // Afficher le texte dans le composant JTextArea
        menuTextArea.setText(menuText.toString());
    }

    private void displayStock(String url, String utilisateur, String motDePasse) throws SQLException {
        StringBuilder stockText = new StringBuilder();

        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             Statement statement = connexion.createStatement()) {

            String query = "SELECT * FROM Stock";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int bread = resultSet.getInt("Bread");
                    int meat = resultSet.getInt("Meat");
                    int cheese = resultSet.getInt("Cheese");
                    int salad = resultSet.getInt("Salad");
                    int tomato = resultSet.getInt("Tomato");
                    int onions = resultSet.getInt("Onions");

                    stockText.append("Pain: ").append(bread).append("\n");
                    stockText.append("Viande: ").append(meat).append("\n");
                    stockText.append("Fromage: ").append(cheese).append("\n");
                    stockText.append("Salade: ").append(salad).append("\n");
                    stockText.append("Tomate: ").append(tomato).append("\n");
                    stockText.append("Oignons: ").append(onions).append("\n\n");
                }
            }
        }

        // Afficher le texte dans le composant JTextArea
        stockTextArea.setText(stockText.toString());
    }
}
