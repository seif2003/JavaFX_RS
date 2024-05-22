package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    public static Connection connect() {    
        String url = "jdbc:mysql://localhost:2411/rs";
        try {
            Connection conn = DriverManager.getConnection(url, "root", "rsapp20");
            return conn;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ouverture de la connexion SQL : " + e.getMessage());
            e.printStackTrace(); // Ajout pour afficher la trace complète de l'erreur
        }
        return null;
    }
}
