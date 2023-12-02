import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final int DOT_SIZE = 10;
    private final int DELAY = 100;

    private ArrayList<Point> snake;
    private Point apple;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private Timer timer;
    private int score = 0;

    public SnakeGame() {
        initGame();
        addKeyListener(this);
        setFocusable(true);
        setBackground(Color.black);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void initGame() {
        snake = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            snake.add(new Point(WIDTH / 2 - i * DOT_SIZE, HEIGHT / 2));
        }
        createApple();
    }

    private void createApple() {
        int x = new Random().nextInt((WIDTH / DOT_SIZE)) * DOT_SIZE;
        int y = new Random().nextInt((HEIGHT / DOT_SIZE)) * DOT_SIZE;
        apple = new Point(x, y);
    }

    private void move() {
        Point head = snake.get(0);
        int x = head.x;
        int y = head.y;

        if (leftDirection) {
            x -= DOT_SIZE;
        }
        if (rightDirection) {
            x += DOT_SIZE;
        }
        if (upDirection) {
            y -= DOT_SIZE;
        }
        if (downDirection) {
            y += DOT_SIZE;
        }

        Point newHead = new Point(x, y);

        if (newHead.equals(apple)) {
            snake.add(0, new Point(apple));
            createApple();
            score += 100;
        } else {
            snake.remove(snake.size() - 1);
        }

        for (int i = 1; i < snake.size(); i++) {
            if (newHead.equals(snake.get(i))) {
                inGame = false;
                break;
            }
        }

        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }

        snake.add(0, newHead);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.setColor(Color.red);
            g.fillRect(apple.x, apple.y, DOT_SIZE, DOT_SIZE);

            g.setColor(Color.green);
            for (Point point : snake) {
                g.fillRect(point.x, point.y, DOT_SIZE, DOT_SIZE);
            }

            g.setColor(Color.white);
            Font scoreFont = new Font("Helvetica", Font.PLAIN, 14);
            g.setFont(scoreFont);
            g.drawString("Score: " + score, 10, 20);

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }

    }

    private void gameOver(Graphics g) {
        String message = "Game Over! Score: " + score;
        Font font = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && !rightDirection) {
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }

        if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && !leftDirection) {
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }

        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !downDirection) {
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }

        if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && !upDirection) {
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}