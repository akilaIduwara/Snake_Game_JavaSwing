import javax.swing.*;

public class Frame{
    public static void main(String[]args){
        int width = 600;
        int hight = width;

        JFrame snake = new JFrame();
        snake.setName("Snake Game");
        snake.setSize(width, hight);
        snake.setVisible(true);
        snake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        snake.setLocationRelativeTo(null);

        Game snk = new Game(width,hight);
        snake.add(snk);
        snake.pack();

        snk.requestFocus();


    }
}
