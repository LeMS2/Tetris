package main;

import java.sql.*;

public class Ranking {

    public static void mostrarRanking() {
        String sql = "SELECT nome, pontuacao, nivel FROM jogadores ORDER BY pontuacao DESC LIMIT 10";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== 🏆 Ranking dos Jogadores ===");
            while (rs.next()) {
                System.out.println(rs.getString("nome") +
                        " | Pontuação: " + rs.getInt("pontuacao") +
                        " | Nível: " + rs.getInt("nivel"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao mostrar ranking: " + e.getMessage());
        }
    }
}
