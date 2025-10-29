package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PrincipalMenu extends JFrame {

    private String nomeJogador;
    private String emailJogador;
    private int idadeJogador;

    public PrincipalMenu(String nomeJogador, String emailJogador, int idadeJogador) {
        this.nomeJogador = nomeJogador;
        this.emailJogador = emailJogador;
        this.idadeJogador = idadeJogador;

        setTitle("Menu Principal - Tetris");
        setIconImage(new ImageIcon("/home/leticia/Documentos/Tetris/assets/tetris.png").getImage());
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel com gradiente preto/cinza
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(10, 10, 10);
                Color color2 = new Color(45, 45, 45);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Título estilizado
        JLabel titulo = new JLabel("Bem-vindo, " + nomeJogador + "!", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        // Botões estilizados
        JButton jogarBtn = criarBotao("🎮 Jogar");
        JButton sairBtn = criarBotao("🚪 Sair");

         colocar o botão classificação e por que não está aparecendo?

        panel.add(titulo);
        panel.add(jogarBtn);
        panel.add(sairBtn);

        add(panel);

        // Ações dos botões
        jogarBtn.addActionListener(e -> {
            dispose();
            abrirJogo();
        });

        sairBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(new Color(60, 60, 60));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        botao.setOpaque(true);

        // Efeito de hover
        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(new Color(90, 90, 90));
            }

            public void mouseExited(MouseEvent e) {
                botao.setBackground(new Color(60, 60, 60));
            }
        });

        return botao;
    }

    private void abrirJogo() {
        JFrame janela = new JFrame("Tetris");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);
        janela.setIconImage(new ImageIcon("assets/tetris.png").getImage());

        // ✅ Agora passando os dados do jogador para o GamePanel
        GamePanel gp = new GamePanel(nomeJogador, emailJogador, idadeJogador);

        janela.add(gp);
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);

        gp.launchGame();
    }
}
