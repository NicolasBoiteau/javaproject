import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class test {

    public static void main(String[] args) {
        // Informations de connexion à la base de données
        String url = "jdbc:mariadb://localhost:3306/votre_base_de_donnees";
        String utilisateur = "votre_utilisateur";
        String motDePasse = "votre_mot_de_passe";

        // Chargement du pilote JDBC
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Connexion à la base de données
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse)) {
            System.out.println("Connexion réussie !");

            // Exécution d'une requête SQL
            String sql = "SELECT * FROM votre_table";
            try (Statement statement = connexion.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                // Traitement des résultats
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("nom");
                    // Ajoutez d'autres colonnes selon votre schéma de base de données

                    System.out.println("ID: " + id + ", Nom: " + nom);
                    // Imprimez d'autres colonnes selon votre schéma de base de données
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
