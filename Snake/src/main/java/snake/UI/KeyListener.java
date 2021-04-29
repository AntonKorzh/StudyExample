package snake.UI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

public class KeyListener extends KeyAdapter {

    private int yStep = 0;
    private int xStep = -1;
    private int actualDir = VK_LEFT;
    private boolean block = false;


    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (check(keyCode)) {
            switch (keyCode) {
                case VK_DOWN:
                    yStep = -1;
                    xStep = 0;
                    break;
                case VK_UP:
                    yStep = 1;
                    xStep = 0;
                    break;
                case VK_LEFT:
                    yStep = 0;
                    xStep = -1;
                    break;
                case VK_RIGHT:
                    yStep = 0;
                    xStep = 1;
                    break;
            }
            actualDir = keyCode;
        }
    }

    private boolean check(int newPressButton) {
        if ((!block) &&
                (((actualDir == VK_LEFT || actualDir == VK_RIGHT) && (newPressButton == VK_UP || newPressButton == VK_DOWN))
                        || ((actualDir == VK_DOWN || actualDir == VK_UP) && (newPressButton == VK_LEFT || newPressButton == VK_RIGHT)))) {
            actualDir = newPressButton;
            return block = true;
        }
        return false;
    }

    public void removeBlock() {
        block = false;
    }

    public int getXDir() {
        return xStep;
    }

    public int getYDir() {
        return yStep;
    }

    public void setDefaultDir() {
        actualDir = VK_LEFT;
        xStep = -1;
        yStep = 0;
        removeBlock();
    }

    public int getActualDir(){
        return actualDir;
    }

}

