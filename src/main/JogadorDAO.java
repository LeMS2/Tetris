package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {

    public static void inserirJogador(String nome, int idade, String email, int nivel, int pontuacao) {
        String sql = "INSERT INTO jogadores(nome, idade, email, nivel, pontuacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, idade);
            pstmt.setString(3, email);
            pstmt.setInt(4, nivel);
            pstmt.setInt(5, pontuacao);
            pstmt.executeUpdate();

            System.out.println("✅ Jogador inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir jogador: " + e.getMessage());
        }
    }

    public static List<String> listarJogadores() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT * FROM jogadores";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String jogador = rs.getInt("id") + " - " + rs.getString("nome") +
                        " | Pontuação: " + rs.getInt("pontuacao") +
                        " | Nível: " + rs.getInt("nivel");
                lista.add(jogador);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar jogadores: " + e.getMessage());
        }

        return lista;
    }

    public static void deletarJogador(int id) {
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

    public static void atualizarPontuacao(int id, int novaPontuacao) {
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
