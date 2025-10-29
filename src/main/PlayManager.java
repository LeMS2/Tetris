package main;
import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.LinearGradientPaint;
import java.sql.*;

public class PlayManager {
    // área do jogador
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    public static int dropInterval = 60;
    boolean gameOver;
    boolean scoreSaved = false; // <- novo controle para evitar salvar várias vezes

    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    int level = 1;
    int lines;
    int score;

    // Dados do jogador (setar via LoginScreen)
    public String playerName;
    public String playerEmail;
    public int playerAge;

    public PlayManager(String name, String email, int age) {

        this.playerName = name;
        this.playerEmail = email;
        this.playerAge = age;

        System.out.println("🎮 Dados recebidos -> " + playerName + ", " + playerEmail + ", " + playerAge);

        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    private Mino pickMino() {
        Mino mino = null;
        int i = new Random().nextInt(7);
        switch (i) {
            case 0 -> mino = new Mino_L1();
            case 1 -> mino = new Mino_L2();
            case 2 -> mino = new Mino_Square();
            case 3 -> mino = new Mino_Bar();
            case 4 -> mino = new Mino_T();
            case 5 -> mino = new Mino_Z1();
            case 6 -> mino = new Mino_Z2();
        }
        return mino;
    }

    public void update() {
        if (!currentMino.active) {
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            currentMino.deactivating = false;

            // Condição de fim de jogo
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(3, false);

                // Salva apenas uma vez
                if (!scoreSaved) {
                    saveScoreToDB();
                    scoreSaved = true;
                }
            }

            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            checkDelete();
        } else {
            currentMino.update();
        }
    }

    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {
            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    blockCount++;
                }
            }
            x += Block.SIZE;
            if (x == right_x) {
                if (blockCount == 12) {
                    effectCounterOn = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        if (staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }
                    lineCount++;
                    lines++;

                    if (lines % 10 == 0 && dropInterval > 1) {
                        level++;
                        if (dropInterval > 10) {
                            dropInterval -= 10;
                        } else {
                            dropInterval -= 1;
                        }
                    }

                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }

        if (lineCount > 0) {
            GamePanel.se.play(4, false);
            int singleLineScore = 50 * level;
            score += singleLineScore * lineCount;
        }
    }

    private void saveScoreToDB() {
        System.out.println("=== Salvando pontuação do jogador ===");
        System.out.println("DEBUG salvar -> nome: " + playerName +
                ", email: " + playerEmail +
                ", idade: " + playerAge +
                ", nivel: " + level +
                ", linhas: " + lines +
                ", score: " + score);

        if (playerEmail == null || playerEmail.isEmpty()) {
            System.out.println("⚠️ ERRO: playerEmail está vazio ou nulo. Pontuação não salva.");
            return;
        }

        try {
            // Garante que a tabela exista antes de salvar
            JogadorDAO.ensureTableExists();

            // Usa o DAO para inserir ou atualizar o jogador
            JogadorDAO.upsertByEmail(
                    playerName == null ? "SemNome" : playerName,
                    playerAge,
                    playerEmail,
                    level,
                    lines,
                    score
            );

            System.out.println("✅ Pontuação salva/atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao salvar pontuação via DAO: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g2) {

        LinearGradientPaint blueGrad = new LinearGradientPaint(
                left_x, top_y,
                right_x, bottom_y,
                new float[]{0f, 1f},
                new Color[]{new Color(0, 180, 255), new Color(0, 255, 200)}
        );

        g2.setPaint(blueGrad);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        int x = right_x + 100;
        int y = bottom_y - 200;
        int boxWidth = 200;
        int boxHeight = 200;

        g2.setPaint(blueGrad);
        g2.drawRect(x, y, boxWidth, boxHeight);

        g2.setPaint(blueGrad);
        g2.drawRect(x, top_y, 250, 300);

        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String texto = "PRÓXIMA";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(texto);
        int textX = x + (boxWidth - textWidth) / 2;
        int textY = y + fm.getAscent() + 10;

        GradientPaint tituloGradient = new GradientPaint(
                textX, textY - 10, Color.ORANGE,
                textX, textY + 10, Color.RED,
                true
        );
        g2.setPaint(tituloGradient);
        g2.drawString(texto, textX, textY);

        x += 40;
        y = top_y + 90;

        String[] nomes = {"Nível: ", "Linhas: ", "Pontos: "};
        int[] valores = {level, lines, score};

        for (int i = 0; i < nomes.length; i++) {
            GradientPaint nameGradient = new GradientPaint(
                    x, y - 20, Color.ORANGE,
                    x, y + 10, Color.RED,
                    true
            );
            g2.setPaint(nameGradient);
            g2.drawString(nomes[i], x, y);

            int nameWidth = fm.stringWidth(nomes[i]);
            g2.setColor(Color.WHITE);
            g2.drawString(String.valueOf(valores[i]), x + nameWidth, y);
            y += 70;
        }

        if (currentMino != null) currentMino.draw(g2);
        if (nextMino != null) nextMino.draw(g2);
        for (Block block : staticBlocks) block.draw(g2);

        if (effectCounterOn) {
            effectCounter++;
            float hue = (effectCounter % 60) / 60f;
            Color c1 = Color.getHSBColor(hue, 1f, 1f);
            Color c2 = Color.getHSBColor((hue + 0.2f) % 1f, 1f, 1f);
            GradientPaint gpEffect = new GradientPaint(left_x, 0, c1, right_x, 0, c2);
            g2.setPaint(gpEffect);
            for (int yLine : effectY) g2.fillRect(left_x, yLine, WIDTH, Block.SIZE);
            if (effectCounter > 20) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        g2.setFont(g2.getFont().deriveFont(50f));
        int gameTextX = left_x + 25;
        int gameTextY = top_y + 320;
        GradientPaint gameGradient = new GradientPaint(
                gameTextX, gameTextY - 30, Color.ORANGE,
                gameTextX, gameTextY + 30, Color.RED,
                true
        );
        g2.setPaint(gameGradient);

        if (gameOver) {
            g2.drawString("Fim de Jogo", gameTextX, gameTextY);
        } else if (KeyHandler.pausePressed) {
            g2.drawString("PAUSADO", gameTextX + 45, gameTextY);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
        g2.drawString("Tetris Simples", 55, top_y + 320);
    }

    public void resetGame() {
        staticBlocks.clear();
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

        level = 1;
        lines = 0;
        score = 0;
        dropInterval = 60;
        gameOver = false;
        scoreSaved = false;
        effectCounterOn = false;
        effectY.clear();

        GamePanel.music.play(0, true);
        System.out.println("Jogo reiniciado!");
    }
}
