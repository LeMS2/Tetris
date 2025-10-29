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

    // 🔹 Dados do jogador
    private String playerName;
    private String playerEmail;
    private int playerAge;

    // 🔹 Botão Reiniciar
    JButton btnReiniciar;

    // 🔸 Novo construtor que recebe os dados do jogador
    public GamePanel(String name, String email, int age) {
        this.playerName = name;
        this.playerEmail = email;
        this.playerAge = age;

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        // 🔹 Passa os dados do jogador pro PlayManager
        pm = new PlayManager(name, email, age);

        // 🔸 BOTÃO REINICIAR
        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setBounds(20, 10, 120, 40);
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.setBackground(new Color(50, 50, 50));
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.setFont(new Font("Arial", Font.BOLD, 16));

        btnReiniciar.addActionListener(e -> {
            pm.resetGame();
            requestFocusInWindow();
            repaint();
        });
        this.add(btnReiniciar);
    }

    // 🔹 Inicia o loop do jogo
    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();

        music.play(0, true);
        music.loop();
    }

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

    private void update() {
        if (!KeyHandler.pausePressed && !pm.gameOver) {
            pm.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }
}
