package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PrincipalMenu extends JFrame {
    public PrincipalMenu(String nomeJogador) {
        setTitle("Menu Principal - Tetris");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel titulo = new JLabel("Bem-vindo, " + nomeJogador + "!", SwingConstants.CENTER);
        JButton jogarBtn = new JButton("🎮 Jogar");
        JButton sairBtn = new JButton("🚪 Sair");

        add(titulo);
        add(jogarBtn);
        add(sairBtn);

        jogarBtn.addActionListener(e -> {
            dispose();
            abrirJogo();
        });

        sairBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void abrirJogo() {
        JFrame janela = new JFrame("Tetris");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        GamePanel gp = new GamePanel();
        janela.add(gp);
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);

        gp.launchGame();
    }
}

