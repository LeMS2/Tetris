package mino;

import java.awt.*;

public class Mino_Square extends Mino {
    private Color startColor;
    private Color endColor;

    public Mino_Square() {
        createGradient();
        create(startColor);
    }

    private void createGradient() {
        startColor = new Color(255, 255, 153);
        endColor = new Color(255, 204, 0);
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

    @Override
    public void setXY(int x, int y){
        b[0].x = x;
        b[0].y = y;

        b[1].x = x;
        b[1].y = y + Block.SIZE;

        b[2].x = x + Block.SIZE;
        b[2].y = y;

        b[3].x = x + Block.SIZE;
        b[3].y = y + Block.SIZE;
    }

    @Override
    public void getDirection1() {}
    @Override
    public void getDirection2() {}
    @Override
    public void getDirection3() {}
    @Override
    public void getDirection4() {}
}
