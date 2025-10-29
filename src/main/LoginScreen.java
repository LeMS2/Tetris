package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginScreen extends JFrame {

    private JTextField nomeField;
    private JTextField emailField;
    private JTextField idadeField;
    private JButton entrarButton;

    public LoginScreen() {
        setTitle("Login - Tetris");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nomeLabel = new JLabel("Nome:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel idadeLabel = new JLabel("Idade:");

        nomeField = new JTextField();
        emailField = new JTextField();
        idadeField = new JTextField();

        entrarButton = new JButton("Entrar");

        add(nomeLabel); add(nomeField);
        add(emailLabel); add(emailField);
        add(idadeLabel); add(idadeField);
        add(new JLabel()); add(entrarButton);

        entrarButton.addActionListener(e -> salvarJogador());

        setVisible(true);
    }

    private void salvarJogador() {
        String nome = nomeField.getText().trim();
        String email = emailField.getText().trim();
        String idade = idadeField.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || idade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        try (Connection conn = conectarBanco()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados!");
                return;
            }

            // Cria a tabela se não existir
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS jogadores (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    idade INTEGER NOT NULL,
                    pontuacao INTEGER DEFAULT 0,
                    nivel INTEGER DEFAULT 1
                );
            """;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }

            // Insere ou ignora se o e-mail já existir
            String insertSQL = """
                INSERT OR IGNORE INTO jogadores (nome, email, idade)
                VALUES (?, ?, ?);
            """;
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, nome);
                pstmt.setString(2, email);
                pstmt.setInt(3, Integer.parseInt(idade));
                pstmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
            dispose();
            new PrincipalMenu(nome);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar jogador: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Idade deve ser um número válido!");
        }
    }

    private Connection conectarBanco() {
        try {
            String url = "jdbc:sqlite:/home/leticia/Documentos/Tetris/tetris.db";
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
