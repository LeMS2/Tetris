package mino;
import java.awt.*;

public class Mino_Z1 extends Mino {
    private Color startColor;
    private Color endColor;

    public Mino_Z1() {
        createGradient();
        create(startColor);
    }

    private void createGradient() {
        startColor = new Color(255, 102, 102);
        endColor = new Color(153, 0, 0);
    }

    @Override
    public void draw(Graphics2D g2) {
        for (Block block : b) {
            int x = block.x;
            int y = block.y;
            int size = Block.SIZE;

            GradientPaint gradient = new GradientPaint(
                    x, y, startColor,
                    x, y + size, endColor
            );

            g2.setPaint(gradient);
            g2.fillRect(x, y, size, size);

            g2.setColor(Color.black);
            g2.drawRect(x, y, size, size);
        }
    }

    public void setXY(int x, int y){
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.SIZE;
        b[2].x = b[0].x - Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x - Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;
    }

    public void getDirection1(){
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x - Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;

        updateXY(1);
    }
    public void getDirection2(){
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.SIZE;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;

        updateXY(2);
    }
    public void getDirection3(){
        getDirection1();
 }
    public void getDirection4(){
        getDirection2();
 }
}
