package snake;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import snake.UI.MyPanel;
import org.apache.log4j.Logger;

import javax.swing.*;


/**
 * @author Anton
 */
public class Snake {

    /**
     * @param args the command line arguments
     */

    public final static Logger logger = Logger.getLogger(Snake.class);
    public final static Properties propertiesGame = new Properties();


    public static void main(String[] args) {

        logger.debug("Application run");

        String pathToProperties = "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "Properties.XML";
        try {
            //Добавить проверку на внутренюю совместимость параметров
            propertiesGame.loadFromXML(new FileInputStream(pathToProperties));
        } catch (java.io.FileNotFoundException fnfe) {
            logger.error("Properties.XML not found", fnfe);
        } catch (java.util.InvalidPropertiesFormatException ipfe) {
            logger.error("Properties.XML not valid", ipfe);
        } catch (IOException ioe) {
            logger.error("Properties.XML read error", ioe);
        }

        JFrame snakeFrame = new JFrame();
        snakeFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        int windowWidth = Integer.parseInt(propertiesGame.getProperty("WindowWidth", "800"));
        int windowHeight = Integer.parseInt(propertiesGame.getProperty("WindowHeight", "650"));
        snakeFrame.setPreferredSize(new Dimension(windowWidth, windowHeight));
        snakeFrame.setResizable(false);
        snakeFrame.setLayout(null);
        snakeFrame.pack();
        MyPanel gameZone = new MyPanel(snakeFrame.getContentPane().getWidth(), snakeFrame.getContentPane().getHeight());
        snakeFrame.add(gameZone);
        snakeFrame.setVisible(true);

    }

}
