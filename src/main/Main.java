package main;

import javax.swing.*;

//public class Main {
//    public static void main(String[] args) {
//      JFrame window = new JFrame("Tetris Simples");
//      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      window.setResizable(false);
//
//      GamePanel gp = new GamePanel();
//      window.add(gp);
//      window.pack();
//
//      window.setLocationRelativeTo(null);
//      window.setVisible(true);
//
//      gp.launchGame();
//      }
//}

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new LoginScreen(); // começa pela tela de login
    });
  }
}

