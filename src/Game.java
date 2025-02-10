import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class Game extends JPanel implements ActionListener,KeyListener {

    int penalwidth =600;
    int penalheight = penalwidth;
    int tile =25;
    int i;
    int score =0;
    Random random;

    //Snake
    tile snakeHead;
    ArrayList<tile> snakeBody;
    //Food
    tile Food;
    //GameLogic
    Timer Gameloop;
    int velocityX;
    int velocityY;


    //tile
    private class tile{
        int x;
        int y;
        tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }


  Game(int panelwidth, int panelheight) {

        this.penalwidth = penalwidth;
        this.penalheight = penalheight;
        this.setPreferredSize(new Dimension(panelwidth,panelheight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new tile(5,5);
        snakeBody = new ArrayList<>(tile);

        Food = new tile(10,10);
        random =new Random();
        placefood();

        velocityX=0;
        velocityY=1;

        Gameloop = new Timer(100,this);
        Gameloop.start();

    }

    private void placefood() {

        Food.x=random.nextInt(penalwidth/tile);
        Food.y=random.nextInt(penalheight/tile);


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g ){


        //Snake Body
        for (int i=0;i<snakeBody.size();i++){
            tile snakepart = snakeBody.get(i);
            g.fillRect(snakepart.x*tile,snakepart.y*tile,tile,tile);
        }

        //Grid
        for (i = 0; i < penalwidth / tile; i++) {

            g.drawLine(i*tile,0,i*tile,penalheight);
            g.drawLine(0,i*tile,penalheight,i*tile);

        }
        //Snake
        g.setColor(Color.GREEN);
        g.fillRect(snakeHead.x*tile, snakeHead.y*tile, tile, tile);

        // Set snake color to green
        g.setColor(Color.GREEN);

        //Snake Body
        for (int i=0;i<snakeBody.size();i++){
            tile snakepart = snakeBody.get(i);
            g.fillRect(snakepart.x*tile,snakepart.y*tile,tile,tile);

        }


        //Food
        g.setColor(Color.RED);
        g.fillRect(Food.x*tile, Food.y*tile, tile, tile);

        // Draw score in the top-left corner
        g.setColor(Color.WHITE);  // Set text color to white
        g.setFont(new Font("Arial", Font.PLAIN, 18));  // Set font style and size
        g.drawString("Score: " + score, 10, 20);  // Draw the score at (10, 20)
    }

    private void move() {

        snakeBody.add(0, new tile(snakeHead.x, snakeHead.y));

        //Eat Food
        if(collision(snakeHead,Food)){
            snakeBody.add(new tile(Food.x,Food.y));
            placefood();

        }

        if (score == 30) {
            JOptionPane.showMessageDialog(this, "Congratulations! Your won The Game :)", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);  // End the game
        }

        // Move the snake in the current direction
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        if (collision(snakeHead, Food)) {
            placefood(); // Generate new food
            score++;
            // Do NOT remove the tail (this increases the length)
        } else {
            // Remove the last body part to keep movement consistent
            snakeBody.remove(snakeBody.size() - 1);
        }

        // Check for self-collision
        for (int i = 0; i < snakeBody.size(); i++) {
            if (collision(snakeHead, snakeBody.get(i))) {
                gameOver();  // Call the gameOver method if collision occurs
                return;  // Stop execution if collision happens
            }
        }


        //Chek if snake Collapse
         if(collision(snakeHead,Food)){}

        // Wrap around logic (if snake goes beyond the screen, it appears on the opposite side)
        if (snakeHead.x < 0) {
            snakeHead.x = penalwidth / tile - 1; // Wrap from left to right
        } else if (snakeHead.x >= penalwidth / tile) {
            snakeHead.x = 0; // Wrap from right to left
        }

        if (snakeHead.y < 0) {
            snakeHead.y = penalheight / tile - 1; // Wrap from top to bottom
        } else if (snakeHead.y >= penalheight / tile) {
            snakeHead.y = 0; // Wrap from bottom to top
        }
    }

    public boolean collision(tile tile1, tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;

    }

    private void gameOver() {
        // Show game over message and display the score
        String message = "Game Over! :( \nYour Marks: " + score + "\nWould you like to try again?";
        int option = JOptionPane.showConfirmDialog(this, message, "Snake Game", JOptionPane.YES_NO_OPTION);

        // If the player clicks "Yes" (Retry)
        if (option == JOptionPane.YES_OPTION) {
            resetGame();  // Reset the game to initial state
        } else {
            System.exit(0);  // If the player clicks "No" (Exit), end the game
        }
    }


    private void resetGame() {
        // Reset score
        score = 0;

        // Reset snake
        snakeHead = new tile(5, 5);
        snakeBody.clear();  // Clear the snake's body

        // Reset velocity (snake direction)
        velocityX = 0;
        velocityY = 1;

        // Reset food position
        placefood();

        // Restart the game loop
        Gameloop.restart();  // Restart the game loop if needed
    }





    @Override
    public void actionPerformed(ActionEvent e) {

        move();
        repaint();

        if (snakeHead.x == Food.x && snakeHead.y == Food.y) {
            placefood();  // Generate a new food position
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX !=1) {
            velocityX = -1;  // Move left
            velocityY = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX !=-1) {
            velocityX = 1;   // Move right
            velocityY = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY !=1) {
            velocityX = 0;
            velocityY = -1;  // Move up
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY !=1) {
            velocityX = 0;
            velocityY = 1;   // Move down
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}




}