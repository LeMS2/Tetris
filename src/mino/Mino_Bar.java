package mino;
import java.awt.*;

public class Mino_Bar extends Mino {
    private Color startColor;
    private Color endColor;

    public Mino_Bar() {
        // não usa mais create(Color.orange)
        createGradient();
        create(startColor);
    }

    private void createGradient() {
        // Aqui você pode guardar as cores do degradê se quiser reutilizar
        startColor = new Color(102, 255, 255);
        endColor = new Color(0, 128, 128);
    }

    @Override
    public void draw(Graphics2D g2) {
        for (Block block : b) { // supondo que cada peça tenha blocos
            int x = block.x;
            int y = block.y;
            int size = Block.SIZE;

            // Cria o degradê vertical
            GradientPaint gradient = new GradientPaint(
                    x, y, startColor,
                    x, y + size, endColor
            );

            g2.setPaint(gradient);
            g2.fillRect(x, y, size, size);

            // Contorno preto pra destacar o bloco
            g2.setColor(Color.black);
            g2.drawRect(x, y, size, size);
        }
    }
    public void setXY(int x, int y){
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x - Block.SIZE;
        b[1].y = b[0].y;
        b[2].x = b[0].x + Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.SIZE*2;
        b[3].y = b[0].y;
    }

    public void getDirection1(){
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE*2;
        tempB[3].y = b[0].y;

        updateXY(1);
    }
    public void getDirection2(){
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + Block.SIZE*2;

        updateXY(2);
    }
    public void getDirection3(){

        getDirection1();
    }
    public void getDirection4(){
        getDirection2();
    }
}