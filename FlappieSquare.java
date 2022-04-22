import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FlappieSquare implements ActionListener, MouseListener, KeyListener { 

    public static FlappieSquare flappieSquare;

    public boolean gameOver, start;

    public Rectangle square;

    public Renderer renderer;

    public int tick, yMotion, point;

    public ArrayList<Rectangle> cols;

    public Random rand;

    public final int WIDTH = 800, HEIGHT = 800;

    public FlappieSquare() {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();
        square = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20); // Create our Square
        cols = new ArrayList<Rectangle>(); // Create new Array List of Columns

        jframe.addMouseListener(this); // Allow player to use Mouse
        jframe.addKeyListener(this); // Allow player to use Keyboard Num
        jframe.setResizable(false); // Prevent player to resize the game window
        jframe.setVisible(true); // Let the Game Window visible 
        jframe.add(renderer);
        jframe.setTitle("Flappie Square"); // Set Title of the Game Window
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit when closing the Game Window
        jframe.setSize(WIDTH, HEIGHT); 


        addCol(true);
        addCol(true);
        addCol(true);
        addCol(true);

        timer.start(); 
    }

    public void addCol(boolean start) { //function to create and add column
        int space = 300;
        int width = 100;
        int height = 40 + rand.nextInt(300); // random height of Column

        if (start) {
            cols.add(new Rectangle(WIDTH + width + cols.size() * 300, HEIGHT - height - 120, width, height));
            cols.add(new Rectangle(WIDTH + width + (cols.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            cols.add(new Rectangle(cols.get(cols.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            cols.add(new Rectangle(cols.get(cols.size() - 1).x, 0, width, HEIGHT - height - space));
        }

    }

    public void actionPerformed(ActionEvent e) {
        tick++;

        int velocity = 13; // Speed of the columns
        if (start) {
            for (int i = 0; i < cols.size(); i++) {
                Rectangle column = cols.get(i);
                column.x -= velocity;
            }

            if (tick % 2 == 0 && yMotion < 15) {
                yMotion += 2; 
            }

            for (int i = 0; i < cols.size(); i++) {
                Rectangle column = cols.get(i);

                if (column.x + column.width < 0) { // Remove or do not create a useless column
                    cols.remove(column); 

                    if (column.y == 0) {
                        addCol(false);
                    }
                }
            }

            square.y += yMotion; // Weight of Square

            for (Rectangle column : cols) { // Increase Score each time the Square pass a column
                if (column.y == 0 && square.x + square.width / 2 > column.x + column.width / 2 - 7
                        && square.x + square.width / 2 < column.x + column.width / 2 + 7) {
                    point++;
                }

                if (column.intersects(square)) { // Game Over when Square touches a column
                    gameOver = true;

                    square.x = column.x - square.width;
                }
            }

            if (square.y > HEIGHT - 120 || square.y < 0) { // Game over when the Square is higher than the Game Window
                gameOver = true;
            }
        }

        if (square.y + yMotion >= HEIGHT - 120) {
            square.y = HEIGHT - 120 - square.height;   
        }
        renderer.repaint();
    }

    public void paintColumn(Graphics g, Rectangle column) { // Give the Column the dark green color
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void repaint(Graphics g) {
        g.setColor(Color.cyan); // Paint the background
        g.fillRect(0, 0, WIDTH, HEIGHT); 

        g.setColor(Color.orange.darker()); // Paint the ground
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.green); // Paint the grass
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        g.setColor(Color.red); // Paint out Square
        g.fillRect(square.x, square.y, square.width, square.height);

        for (Rectangle column : cols) { // Paint all the Columns dark green
            paintColumn(g, column); 
        }

        g.setColor(Color.white); // Set the white color for all the numbers and words
        g.setFont(new Font("Arial", 1, 100)); // Set the Font Arial for the words

        if (!start) {
            g.drawString("Click to Start", 125, HEIGHT / 2 - 200); // Create a sentence "Click to Start"
        }

        if (gameOver) {
            g.drawString("Game Over", 125, HEIGHT / 2 - 200); // Create a sentence "Game Over"
        }

        if (!gameOver && start) {
            g.drawString(String.valueOf(point), WIDTH / 2 - 25, 100); // Show the Score of player when playing
        }
    }

    public void jump() {
        if (gameOver) { // Delete all data such as score and column when losing
            square = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            cols.clear();
            point = 0;
            yMotion = 0; 

            addCol(true);
            addCol(true);
            addCol(true);
            addCol(true);

            gameOver = false; // Restart the game
        } 

        if (!start) { 
            start = true;
        } else if (!gameOver) { // When started, the Square will automatically fall down
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10; // adjust the height of jump
        }
    }

    public static void main(String[] args) {
        flappieSquare = new FlappieSquare();
    }

    @Override
    public void mouseClicked(MouseEvent e) { // Click to get the Square jump
        jump();

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        

    }

    @Override
    public void keyPressed(KeyEvent e) {
        

    }

    @Override
    public void keyReleased(KeyEvent e) { // Press Space to get the Square jump
         if (e.getKeyCode() == KeyEvent.VK_SPACE){
             jump();
         }

    }
}

class Renderer extends JPanel { 
    private static final long serialVersionUID = 1L;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FlappieSquare.flappieSquare.repaint(g);
    }
}