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
        setIconImage(new ImageIcon("/home/leticia/Documentos/Tetris/assets/tetris.png").getImage());
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal com gradiente de fundo
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(15, 15, 15);
                Color color2 = new Color(40, 40, 40);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Labels com texto branco
        JLabel nomeLabel = new JLabel("Nome:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel idadeLabel = new JLabel("Idade:");
        nomeLabel.setForeground(Color.WHITE);
        emailLabel.setForeground(Color.WHITE);
        idadeLabel.setForeground(Color.WHITE);

        // Campos de entrada
        nomeField = new JTextField();
        emailField = new JTextField();
        idadeField = new JTextField();

        // Botão estilizado
        entrarButton = new JButton("Entrar");
        entrarButton.setBackground(new Color(60, 60, 60));
        entrarButton.setForeground(Color.WHITE);
        entrarButton.setFocusPainted(false);
        entrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        entrarButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        entrarButton.setOpaque(true);

        // Efeito de brilho ao passar o mouse
        entrarButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                entrarButton.setBackground(new Color(90, 90, 90));
            }

            public void mouseExited(MouseEvent e) {
                entrarButton.setBackground(new Color(60, 60, 60));
            }
        });

        // Adiciona os componentes ao painel
        panel.add(nomeLabel);
        panel.add(nomeField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(emailField);
        panel.add(idadeLabel);
        panel.add(idadeField);
        panel.add(new JLabel());
        panel.add(entrarButton);

        add(panel);

        // Ação do botão
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
            dispose();
            JFrame frame = new JFrame("Tetris - " + nome);
            GamePanel gamePanel = new GamePanel(nome, email, Integer.parseInt(idade));
            frame.add(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            gamePanel.launchGame();


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
