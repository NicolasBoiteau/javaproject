import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class WaitTimeCalculator {

    private String url;
    private String utilisateur;
    private String motDePasse;

    public WaitTimeCalculator(String url, String utilisateur, String motDePasse) {
        this.url = url;
        this.utilisateur = utilisateur;
        this.motDePasse = motDePasse;
    }

    public int calculateWaitTime(int orderId) {
        try {
            // Récupérer l'heure actuelle
            Date now = new Date();

            // Calculer le temps d'attente de base (4 minutes)
            int waitTime = 4 * 60;

            // Récupérer le montant total de la commande
            double totalAmount = getTotalAmount(orderId);

            // Ajouter 40 secondes par tranche de 5 euros dépensés
            waitTime += (int) (totalAmount / 5) * 60;

            // Récupérer les commandes dans les 5 dernières minutes
            double totalAmountFromRecentOrders = getTotalAmountFromRecentOrders(now, orderId);

            // Ajouter 60 secondes par tranche de 5 euros pour chaque commande dans les 5 dernières minutes
            waitTime += (int) (totalAmountFromRecentOrders / 3.5) * 60;

            return waitTime;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Valeur d'erreur
        }
    }

    public double getTotalAmount(int orderId) throws SQLException {
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement preparedStatement = connexion.prepareStatement("SELECT TotalAmount FROM Orders WHERE OrderID = ?")) {

            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("TotalAmount");
            }

            return 0; // Aucune commande trouvée
        }
    }

    public double getTotalAmountFromRecentOrders(Date currentTime, int currentOrderId) throws SQLException {
        double totalAmountFromRecentOrders = 0;

        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
             PreparedStatement preparedStatement = connexion.prepareStatement("SELECT TotalAmount FROM Orders WHERE OrderDate >= ? AND OrderID != ?")) {

            // Calculer l'heure 5 minutes avant l'heure actuelle
            long fiveMinutesAgo = currentTime.getTime() - (5 * 60 * 1000);
            Date fiveMinutesAgoDate = new Date(fiveMinutesAgo);

            preparedStatement.setTimestamp(1, new Timestamp(fiveMinutesAgoDate.getTime()));
            preparedStatement.setInt(2, currentOrderId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                totalAmountFromRecentOrders += resultSet.getDouble("TotalAmount");
            }
        }

        return totalAmountFromRecentOrders;
    }
// Ajoutez cette méthode à votre classe BurgerOrderApp
public int getLastOrderId() throws SQLException {
    try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
         java.sql.Statement statement = connexion.createStatement()) {

        String query = "SELECT MAX(OrderID) AS LastOrderId FROM Orders";
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getInt("LastOrderId");
        }

        return -1; // Aucune commande trouvée
    }
}

public static void main(String[] args) {
    try {
        // Créez une instance de WaitTimeCalculator
        WaitTimeCalculator calculator = new WaitTimeCalculator("jdbc:mariadb://localhost:3307/RestaurantDB", "root", "123123");

        // Appelez getLastOrderId() à partir de l'instance de WaitTimeCalculator
        int orderId = calculator.getLastOrderId();

        // Appelez calculateWaitTime() avec l'ID de la commande
        int waitTime = calculator.calculateWaitTime(orderId);

        // Affichez le temps d'attente estimé
        System.out.println("Temps d'attente estimé : " + waitTime + " secondes");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
