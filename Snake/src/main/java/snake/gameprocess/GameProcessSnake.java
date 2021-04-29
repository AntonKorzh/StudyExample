/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.gameprocess;

import snake.UI.MyPanel;
import static snake.Snake.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Anton
 */
public class GameProcessSnake extends Thread {

    private final MyPanel panel;
    private final LinkedList<Point> posSegm = new LinkedList<>();
    private Point applePos;
    private boolean wellFedSnake = false;
    private final int cellNumber;
    private final int speedSnake;

    {
        //Добавить обработку NumberFormatException
        cellNumber = Integer.parseInt(propertiesGame.getProperty("cellNumber", "30"));
        speedSnake = Integer.parseInt(propertiesGame.getProperty("speedSnake", "600"));
    }

    public GameProcessSnake(MyPanel panel) {
        this.panel = panel;
        createNewSnake(4);
        moveApple();
        panel.rePrint(posSegm, applePos);
    }


    private void createNewSnake(int lenght) {
        for (int i = 0; i < lenght; i++) {
            posSegm.add(new Point(cellNumber / 2 + i, cellNumber / 2));
        }
    }


    private void moveApple() {
        Random getNewApplePos = new Random();
        Point newApplePos;
        do {
            newApplePos = new Point(getNewApplePos.nextInt(cellNumber),
                    getNewApplePos.nextInt(cellNumber));
        } while (posSegm.contains(newApplePos));
        applePos = newApplePos;
    }


    private Point getNewHeadPos() {
        int oldX = posSegm.getFirst().x;
        int oldY = posSegm.getFirst().y;
        int dx = panel.getKeyListener().getXDir();
        int dy = panel.getKeyListener().getYDir();
        if (dx != 0) {
            if (oldX == 0 && dx == -1) {
                return new Point(cellNumber + dx, oldY);
            } else if (oldX == cellNumber - 1 && dx == 1) {
                return new Point(0, oldY);
            }
        }
        if (dy != 0) {
            if (oldY == 0 && dy == 1) {
                return new Point(oldX, cellNumber - 1);
            } else if (oldY == cellNumber - 1 && dy == -1) {
                return new Point(oldX, 0);
            }
        }
        return new Point(oldX + dx, oldY - dy);
    }

    private void move() {

        if (!wellFedSnake) {
            posSegm.removeLast();
            wellFedSnake = false;
        }
        posSegm.addFirst(getNewHeadPos());

        if (posSegm.getFirst().equals(applePos)) {
            moveApple();
            wellFedSnake = true;
            panel.scoreUp(10);
        } else {
            wellFedSnake = false;
        }
        Point head = posSegm.getFirst();
        for (int bodySeg = 4; bodySeg < posSegm.size(); bodySeg++) {
            if (posSegm.get(bodySeg).equals(head)) {
                panel.printGameOver();
                interrupt();
                return;
            }
        }
        panel.rePrint(posSegm, applePos);
    }


    @Override
    public void run() {
        logger.debug("Game started");
        while (!Thread.currentThread().isInterrupted()) {
            move();
            try {
                synchronized (this) {
                    wait(60 * 1000 / speedSnake);
                }
            } catch (InterruptedException ex) {
                this.interrupt();
            }
        }
        logger.debug("Game finished");
    }
}
