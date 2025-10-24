package main;
import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;

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

    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    int level = 1;
    int lines;
    int score;

    public PlayManager() {

        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); // 1280/2 - 360/2 = 460
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
            case 0:
                mino = new Mino_L1();
                break;
            case 1:
                mino = new Mino_L2();
                break;
            case 2:
                mino = new Mino_Square();
                break;
            case 3:
                mino = new Mino_Bar();
                break;
            case 4:
                mino = new Mino_T();
                break;
            case 5:
                mino = new Mino_Z1();
                break;
            case 6:
                mino = new Mino_Z2();
                break;
        }
        return mino;
    }

    public void update() {
        if (currentMino.active == false) {
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            currentMino.deactivating = false;

            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y){
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(3, false);
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
    private void checkDelete(){
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while(x < right_x && y < bottom_y){

            for(int i = 0; i < staticBlocks.size(); i++){
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y){
                    blockCount++;
                }
            }
            x += Block.SIZE;
            if(x == right_x){
                if(blockCount == 12){
                    effectCounterOn = true;
                    effectY.add(y);

                    for(int i = staticBlocks.size() -1; i > -1; i--){
                        if(staticBlocks.get(i).y == y){
                            staticBlocks.remove(i);
                        }
                    }
                    lineCount++;
                    lines++;

                    if(lines % 10 == 0 && dropInterval > 1){
                        level++;
                        if(dropInterval > 10){
                            dropInterval -= 10;
                        }
                        else{
                            dropInterval -= 1;
                        }
                    }

                    for(int i = 0; i < staticBlocks.size(); i++){
                        if(staticBlocks.get(i).y < y){
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
        if(lineCount > 0){
            GamePanel.se.play(4, false);
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }

    }
    public void draw(Graphics2D g2) {
        // Área principal do jogo
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Área da próxima peça
        int x = right_x + 100;
        int y = bottom_y - 200;
        int boxWidth = 200;
        int boxHeight = 200;
        g2.drawRect(x, y, boxWidth, boxHeight);

        // ----------------------
        // Título "PRÓXIMA" com gradiente laranja → vermelho
        // ----------------------
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String texto = "PRÓXIMA";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(texto);
        int textX = x + (boxWidth - textWidth) / 2; // centraliza horizontalmente
        int textY = y + fm.getAscent() + 10;        // um pouco abaixo do topo da caixa

        // Gradiente para o título
        GradientPaint tituloGradient = new GradientPaint(
                textX, textY - 10, Color.ORANGE,
                textX, textY + 10, Color.RED,
                true
        );
        g2.setPaint(tituloGradient);
        g2.drawString(texto, textX, textY);

        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("Nível: " + level, x, y); y+= 70;
        g2.drawString("Linhas: " + lines, x, y); y+= 70;
        g2.drawString("Pontos: " + score, x, y);

        // ----------------------
        // Desenha minos
        // ----------------------
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        if (nextMino != null) {
            nextMino.draw(g2);
        }

        // ----------------------
        // Desenha blocos estáticos e efeito
        // ----------------------
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        if (effectCounterOn) {
            effectCounter++;

            float hue = (effectCounter % 60) / 60f;
            Color c1 = Color.getHSBColor(hue, 1f, 1f);
            Color c2 = Color.getHSBColor((hue + 0.2f) % 1f, 1f, 1f);

            GradientPaint gpEffect = new GradientPaint(left_x, 0, c1, right_x, 0, c2);
            g2.setPaint(gpEffect);

            for (int i = 0; i < effectY.size(); i++) {
                g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
            }

            if (effectCounter > 20) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // ----------------------
        // Texto de fim de jogo / pausa com gradiente laranja → vermelho
        // ----------------------
        g2.setFont(g2.getFont().deriveFont(50f));
        int gameTextX = left_x + 25;
        int gameTextY = top_y + 320;

        GradientPaint gameGradient = new GradientPaint(
                gameTextX, gameTextY - 30, Color.ORANGE,
                gameTextX, gameTextY + 30, Color.RED,
                true
        );
        g2.setPaint(gameGradient);

        if(gameOver){
            g2.drawString("Fim de Jogo", gameTextX, gameTextY);
        } else if (KeyHandler.pausePressed) {
            g2.drawString("PAUSADO", gameTextX + 45, gameTextY);
        }

        // ----------------------
        // Título do jogo
        // ----------------------
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
        g2.drawString("Tetris Simples", 35 + 20, top_y + 320);
    }
    public void resetGame() {
        staticBlocks.clear(); // limpa os blocos já fixos
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

        level = 1;
        lines = 0;
        score = 0;
        dropInterval = 60;
        gameOver = false;
        effectCounterOn = false;
        effectY.clear();

        // Reinicia a música se quiser
        GamePanel.music.play(0, true);

        System.out.println("Jogo reiniciado!");
    }

}




