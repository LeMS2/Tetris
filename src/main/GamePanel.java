package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    final int FPS = 60;

    Thread gameThread;
    PlayManager pm;
    public static Sound music = new Sound();
    public static Sound se = new Sound();

    // 🔹 Botões
    JButton btnReiniciar;
    JButton btnMenu; // caso queira usar depois

    public GamePanel() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null); // necessário para posicionar os botões manualmente
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        // 🔹 Gerenciador do jogo
        pm = new PlayManager();

        // Certifique-se de que o JPanel usa layout nulo
        this.setLayout(null);

// 🔸 BOTÃO REINICIAR
        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setBounds(20, 10, 120, 40); // lado esquerdo, topo
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.setBackground(new Color(50, 50, 50));
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.setFont(new Font("Arial", Font.BOLD, 16));

        btnReiniciar.addActionListener(e -> {
            pm.resetGame(); // reinicia o jogo
            requestFocusInWindow(); // volta o foco pro jogo
            repaint();
        });
        this.add(btnReiniciar);

// 🔸 BOTÃO MENU PRINCIPAL
        btnMenu = new JButton("Menu Principal");
        btnMenu.setBounds(20, 60, 120, 40); // abaixo do botão reiniciar
        btnMenu.setFocusPainted(false);
        btnMenu.setBackground(new Color(50, 50, 50));
        btnMenu.setForeground(Color.WHITE);
        btnMenu.setFont(new Font("Arial", Font.BOLD, 16));

        btnMenu.addActionListener(e -> {
            // Caso tenha uma tela de menu:
            // new MenuPrincipal().setVisible(true);
            // ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();
            System.out.println("Voltar ao menu (implemente se desejar)");
        });
        this.add(btnMenu);
    }

    // 🔹 Inicia o loop do jogo
    // =============================
    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();

        music.play(0, true);
        music.loop();
    }

    // =============================
    // 🔹 Loop principal
    // =============================
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    // =============================
    // 🔹 Atualização do jogo
    // =============================
    private void update() {
        if (!KeyHandler.pausePressed && !pm.gameOver) {
            pm.update();
        }
    }

    // =============================
    // 🔹 Desenho
    // =============================
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }
}
