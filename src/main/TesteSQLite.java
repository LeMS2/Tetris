package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteSQLite {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:tetris.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("✅ Conectado ao banco SQLite com sucesso!");
        } catch (SQLException e) {
            System.out.println("❌ Erro na conexão: " + e.getMessage());
        }
    }
}
