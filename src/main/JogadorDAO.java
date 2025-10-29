package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {

    // Garanta que Database.connect() retorne uma Connection para o mesmo tetris.db usado pelo PlayManager.
    // Cria a tabela com colunas consistentes (nome, email, idade, nivel, linhas, pontuacao).
    public static void ensureTableExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS jogadores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                email TEXT UNIQUE,
                idade INTEGER,
                nivel INTEGER,
                linhas INTEGER,
                pontuacao INTEGER
            );
        """;
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("❌ Erro ao garantir tabela jogadores: " + e.getMessage());
        }
    }

    // Upsert: se existir jogador com o mesmo email, atualiza; senão, insere.
    public static void upsertByEmail(String nome, int idade, String email, int nivel, int linhas, int pontuacao) {
        if (email == null || email.isEmpty()) {
            System.out.println("⚠️ JogadorDAO.upsertByEmail: email vazio — abortando upsert.");
            return;
        }

        ensureTableExists();

        String checkSql = "SELECT id FROM jogadores WHERE email = ?";
        String insertSql = "INSERT INTO jogadores (nome, email, idade, nivel, linhas, pontuacao) VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE jogadores SET nome = ?, idade = ?, nivel = ?, linhas = ?, pontuacao = ? WHERE email = ?";

        try (Connection conn = Database.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // existe -> update
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, nome);
                    updateStmt.setInt(2, idade);
                    updateStmt.setInt(3, nivel);
                    updateStmt.setInt(4, linhas);
                    updateStmt.setInt(5, pontuacao);
                    updateStmt.setString(6, email);
                    updateStmt.executeUpdate();
                    System.out.println("✅ Jogador atualizado (email encontrado).");
                }
            } else {
                // não existe -> insert
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, nome);
                    insertStmt.setString(2, email);
                    insertStmt.setInt(3, idade);
                    insertStmt.setInt(4, nivel);
                    insertStmt.setInt(5, linhas);
                    insertStmt.setInt(6, pontuacao);
                    insertStmt.executeUpdate();
                    System.out.println("✅ Jogador inserido (novo email).");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro no upsert de jogador: " + e.getMessage());
        }
    }

    // Mantive listar e deletar adaptados ao schema novo
    public static List<String> listarJogadores() {
        List<String> lista = new ArrayList<>();
        ensureTableExists();
        String sql = "SELECT * FROM jogadores ORDER BY pontuacao DESC, nivel DESC";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String jogador = rs.getInt("id") + " - " + rs.getString("nome") +
                        " | Email: " + rs.getString("email") +
                        " | Pontuação: " + rs.getInt("pontuacao") +
                        " | Nível: " + rs.getInt("nivel") +
                        " | Linhas: " + rs.getInt("linhas");
                lista.add(jogador);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar jogadores: " + e.getMessage());
        }

        return lista;
    }

    public static void deletarJogador(int id) {
        ensureTableExists();
        String sql = "DELETE FROM jogadores WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("🗑️ Jogador removido!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao deletar jogador: " + e.getMessage());
        }
    }

    // Atualiza por id (mantido)
    public static void atualizarPontuacao(int id, int novaPontuacao) {
        ensureTableExists();
        String sql = "UPDATE jogadores SET pontuacao = ? WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, novaPontuacao);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("🏅 Pontuação atualizada!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar pontuação: " + e.getMessage());
        }
    }
}
