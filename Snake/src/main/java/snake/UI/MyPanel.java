/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake.UI;

import snake.gameprocess.GameProcessSnake;

import static snake.Snake.*;
import static java.awt.event.KeyEvent.*;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author Anton
 */
public class MyPanel extends javax.swing.JPanel {

    private final int zeroPoint;
    private final int endPoint;
    private final KeyListener keyListener;
    private JTextPane score;
    private JLabel gameOver;
    private Image background;
    private final Map<String, Icon> nameToIcon = new HashMap<>();
    private final List<JLabel> segmsSnake = new ArrayList<>();
    private final int gameZoneSize;
    private final int segmSize;
    private JLabel apple;
    private Thread currentGame = null;

    {
        //Добавить обработку NumberFormatException
        gameZoneSize = Integer.parseInt(propertiesGame.getProperty("gameZoneSize", "600"));
        segmSize = Integer.parseInt(propertiesGame.getProperty("segmSize", "20"));
    }


    public MyPanel(int realWidth, int realHeight) {

        setBounds(0, 0, realWidth, realHeight);
        setLayout(null);
        zeroPoint = (realHeight - gameZoneSize) / 2;
        endPoint = zeroPoint + gameZoneSize;


        String fs = File.separator;
        String headWestImagePath = "src" + fs + "main" + fs + "resources" + fs + "images" + fs + "headWest.png";
        String headNorthImagePath = "src" + fs + "main" + fs + "resources" + fs + "images" + fs + "headNorth.png";
        String headEastImagePath = "src" + fs + "main" + fs + "resources" + fs + "images" + fs + "headEast.png";
        String headSouthImagePath = "src" + fs + "main" + fs + "resources" + fs + "images" + fs + "headSouth.png";
        String appleImagePath = "src" + fs + "main" + fs + "resources" + fs + "images" + fs + "apple.png";
        String bodyImagePath = "src" + fs + "main" + fs + "resources" + fs + "images" + fs + "body.png";
        String gameOverImagePath = "src" + fs + "main" + fs + "resources" + fs + "images" + fs + "go.jpg";
        String backgroundPath = "src" + fs + "main" + fs + "resources" + fs + "images" + fs + "fon.jpg";

        try {

            background = ImageIO.read(new File(backgroundPath));
            nameToIcon.put("headWest", new ImageIcon(ImageIO.read(
                    new File(headWestImagePath)).getScaledInstance(segmSize, segmSize, java.awt.Image.SCALE_SMOOTH)));
            nameToIcon.put("headNorth", new ImageIcon(ImageIO.read(
                    new File(headNorthImagePath)).getScaledInstance(segmSize, segmSize, java.awt.Image.SCALE_SMOOTH)));
            nameToIcon.put("headEast", new ImageIcon(ImageIO.read(
                    new File(headEastImagePath)).getScaledInstance(segmSize, segmSize, java.awt.Image.SCALE_SMOOTH)));
            nameToIcon.put("headSouth", new ImageIcon(ImageIO.read(
                    new File(headSouthImagePath)).getScaledInstance(segmSize, segmSize, java.awt.Image.SCALE_SMOOTH)));
            nameToIcon.put("apple", new ImageIcon(ImageIO.read(
                    new File(appleImagePath)).getScaledInstance(segmSize, segmSize, java.awt.Image.SCALE_SMOOTH)));
            nameToIcon.put("body", new ImageIcon(ImageIO.read(
                    new File(bodyImagePath)).getScaledInstance(segmSize, segmSize, java.awt.Image.SCALE_SMOOTH)));
            nameToIcon.put("gameOver", new ImageIcon(ImageIO.read(
                    new File(gameOverImagePath)).getScaledInstance(gameZoneSize,
                    gameZoneSize, java.awt.Image.SCALE_SMOOTH)));
        } catch (IOException IOe) {
            logger.warn("One or more images are missing", IOe);
            //добавить дефолтные изображения из Color
        }

        int startButtonWidth = 100;
        int startButtonHeight = 40;
        //Create button "New game"
        JButton newGame = new JButton();
        newGame.setBounds(realHeight + (realWidth - realHeight - startButtonWidth) / 2,
                startButtonHeight, startButtonWidth, startButtonHeight);
        newGame.setText("New game");
        newGame.addActionListener(e -> {
            gameOver.setVisible(false);
            score.setText("0");
            if (currentGame != null) {
                currentGame.interrupt();
                clearingValues();
            }
            Thread game = new GameProcessSnake(this);
            game.start();
            currentGame = game;
        });
        keyListener = new KeyListener();
        newGame.addKeyListener(keyListener);
        add(newGame);

        //Create button "Exit"
        JButton exit = new JButton();
        exit.setBounds(realHeight + (realWidth - realHeight - startButtonWidth) / 2,
                startButtonHeight * 3, startButtonWidth, startButtonHeight);
        exit.setText("Exit");
        exit.addActionListener(e -> System.exit(0));
        exit.addKeyListener(keyListener);
        add(exit);

        JTextPane scoreLable = new JTextPane();
        scoreLable.setBounds(realHeight + (realWidth - realHeight - startButtonWidth) / 2,
                startButtonHeight * 5, 75, startButtonHeight);
        scoreLable.setText("Score :");
        scoreLable.setBackground(this.getBackground());
        scoreLable.setDisabledTextColor(Color.red);
        scoreLable.setFont(new Font("", 2, 20));
        scoreLable.setEnabled(false);
        add(scoreLable);

        score = new JTextPane();
        score.setBounds(scoreLable.getLocation().x + scoreLable.getBounds().width, scoreLable.getLocation().y,
                startButtonWidth / 2, startButtonHeight);
        score.setText("0");
        score.setBackground(this.getBackground());
        score.setDisabledTextColor(Color.red);
        score.setFont(new Font("", 3, 20));
        score.setEnabled(false);
        add(score);

        gameOver = new JLabel();
        gameOver.setBounds(zeroPoint, zeroPoint, gameZoneSize, gameZoneSize);
        gameOver.setIcon(nameToIcon.get("gameOver"));
        gameOver.setVisible(false);
        add(gameOver);
    }

    @Override
    protected void paintComponent(Graphics g) {

        g.drawImage(background, zeroPoint, zeroPoint, gameZoneSize, gameZoneSize, null);
        g.setColor(new Color(80, 80, 150));
        for (int i = zeroPoint; i <= endPoint; i += segmSize) {
            g.drawLine(i, zeroPoint, i, endPoint);
        }
        for (int i = zeroPoint; i <= endPoint; i += segmSize) {
            g.drawLine(zeroPoint, i, endPoint, i);
        }
        repaint();
    }

    public void printGameOver() {
        gameOver.setVisible(true);
        clearingValues();
    }

    private void clearingValues() {
        segmsSnake.forEach(this::remove);
        segmsSnake.clear();
        keyListener.setDefaultDir();
    }


    public void rePrint(List<Point> snake, Point applePos) {

        for (int i = 0; i < segmsSnake.size(); i++) {
            segmsSnake.get(i).setLocation(zeroPoint + snake.get(i).x * segmSize,
                    zeroPoint + snake.get(i).y * segmSize);
        }
        if (snake.size() > segmsSnake.size()) {
            for (int i = segmsSnake.size(); i < snake.size(); i++) {
                Point currentPoint = snake.get(i);
                JLabel newLabel = new JLabel();
                newLabel.setBounds(zeroPoint + currentPoint.x * segmSize, zeroPoint + currentPoint.y * segmSize,
                        segmSize, segmSize);
                if (!(currentPoint.equals(snake.get(0)))) {
                    newLabel.setIcon(nameToIcon.get("body"));
                }
                segmsSnake.add(newLabel);
                add(newLabel);
            }
        }
        switch (keyListener.getActualDir()) {
            case VK_LEFT:
                segmsSnake.get(0).setIcon(nameToIcon.get("headWest"));
                break;
            case VK_UP:
                segmsSnake.get(0).setIcon(nameToIcon.get("headNorth"));
                break;
            case VK_RIGHT:
                segmsSnake.get(0).setIcon(nameToIcon.get("headEast"));
                break;
            case VK_DOWN:
                segmsSnake.get(0).setIcon(nameToIcon.get("headSouth"));
                break;
        }
        if (apple != null) {
            apple.setLocation(zeroPoint + applePos.x * segmSize,
                    zeroPoint + applePos.y * segmSize);
        } else {
            JLabel newLabel = new JLabel();
            newLabel.setBounds(zeroPoint + applePos.x * segmSize, zeroPoint + applePos.y * segmSize,
                    segmSize, segmSize);
            newLabel.setIcon(nameToIcon.get("apple"));
            apple = newLabel;
            add(newLabel);
        }
        keyListener.removeBlock();
    }

    public void scoreUp(int numberUp) {

        int oldScore = Integer.parseInt(score.getText());
        score.setText(oldScore + numberUp + "");

    }

    public KeyListener getKeyListener() {
        return keyListener;
    }
}