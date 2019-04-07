package game;
import com.sun.javafx.scene.traversal.Direction;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import java.util.*;
import java.util.List;
import javafx.scene.transform.Rotate;
import org.omg.CORBA.CODESET_INCOMPATIBLE;

public class Game{

    private static final long TASK_UPDATE_PERIOD_MS = 70;
    private static final long TASK_UPDATE_DELAY_MS = TASK_UPDATE_PERIOD_MS;

    private static final int WINDOW_HEIGHT = 800;
    private static final int WINDOW_WIDTH = 800;
    private static final int BLOCK_COUNT = 40;
    private static final int GRID_BLOCK_SIZE = WINDOW_HEIGHT / BLOCK_COUNT;



    private Stage gameStage;
    private GraphicsContext context;
    private Snake snake;
    private Board board;
    private AnimationTimer animationTimer;
    private Timer timer;
    private TimerTask task;
    private int points;
    private boolean stearing = true;
    private List<Point> tail;
    private Point headLocation;
    javafx.scene.image.Image head = new Image("/image/snakehead.png");
    javafx.scene.image.Image body = new Image("/image/snakebody.png");
    javafx.scene.image.Image snakeTail = new Image("/image/snaketail.png");
    javafx.scene.image.Image snakeBodyCurve = new Image("/image/snakebodycurve.png");

    private boolean isGameInProgress = true;
    private boolean gameOver = false;


    public Game(Stage primaryStage){
        this.gameStage = primaryStage;
    }

    public void start() throws Exception {
/*
        Parent root = FXMLLoader.load(getClass().getResource("game.fxml"));
*/

        gameStage.setTitle("Snake");
        GridPane root = new GridPane();
        Canvas canvas = new Canvas(WINDOW_WIDTH,WINDOW_HEIGHT + 100);
        context = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        board = new Board(WINDOW_WIDTH, WINDOW_HEIGHT, GRID_BLOCK_SIZE);
        snake = new Snake(WINDOW_WIDTH, WINDOW_HEIGHT, GRID_BLOCK_SIZE);
        snake.setHeadLocation(0, 0);

        drawPoints();
        snake.addTailSegment();
        snake.addTailSegment();
        snake.addTailSegment();
        snake.addTailSegment();
        snake.addTailSegment();
        snake.addTailSegment();
        headLocation = snake.getHeadLocation();
        tail = snake.getTail();
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.UP && snake.getDirection() != Direction.DOWN && stearing == false){
                System.out.println(headLocation);
                System.out.println(board.getFood().getLocation());
                snake.setDirection(Direction.UP);
                stearing = true;
            } else if(e.getCode() == KeyCode.DOWN && snake.getDirection() != Direction.UP && stearing == false){
                snake.setDirection(Direction.DOWN);
                stearing = true;
            } else if(e.getCode() == KeyCode.LEFT && snake.getDirection() != Direction.RIGHT && stearing == false){
                snake.setDirection(Direction.LEFT);
                stearing = true;
            } else if(e.getCode() == KeyCode.RIGHT && snake.getDirection() != Direction.LEFT && stearing == false){
                snake.setDirection(Direction.RIGHT);
                stearing = true;
            } else if(e.getCode() == KeyCode.R){

            }
        });
        gameStage.setScene(scene);
        gameStage.centerOnScreen();
        gameStage.show();
        gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isGameInProgress) {
                    drawWhite();
                    drawSnake();
                    drawFood();
                    drawPoints();
                }
            }
        };
        animationTimer.start();

        task = createTimerTask();
        timer = new Timer("Timer");
        timer.scheduleAtFixedRate(task, TASK_UPDATE_DELAY_MS,TASK_UPDATE_PERIOD_MS);



    }

    private TimerTask createTimerTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(isGameInProgress){
                    snake.snakeUpdate();
                    if(snake.colidedWithWall()){
                        endGame("Chuj Ci w dupe");
                    } else if (snake.colidedWithTail()) {
                        endGame("Chuj Ci w dupe");
                    }
                    if(stearing == true){
                        stearing = false;
                    }
                    boolean foundFood = board.foundFood(snake);
                    if (foundFood) {
                        snake.addTailSegment();
                        board.addFood();
                        increasePoints();
                    }

                }
            }
        };
        return task;
    }

    private void endGame(String reason) {
        timer.cancel();
        timer = null;
        isGameInProgress = false;
        gameOver = true;
        System.out.println("Game over: " + reason);
    }


    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy, int height, int width) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + height / 2, tlpy + width / 2);
        gc.drawImage(image, tlpx, tlpy, height, width);
        gc.restore(); // back to original state (before rotation)
    }

    private void increasePoints(){
        points += 20;
    }

    private void drawPoints(){
        context.setFill(Color.BLACK);
        context.fillRect(0,WINDOW_HEIGHT,WINDOW_WIDTH,WINDOW_HEIGHT);
        context.setFill(Color.WHITE);
        context.fillText(Integer.toString(points), Math.round(WINDOW_WIDTH  / 2),WINDOW_HEIGHT + 50);
    }

    private void drawWhite(){
        context.setFill(Color.LIGHTGRAY);
        context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void drawGrid() {
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        context.setStroke(Color.LIGHTGRAY);
        context.setLineWidth(0.5);

        for(int i = 0; i < WINDOW_WIDTH; i += GRID_BLOCK_SIZE){
            context.strokeLine(i, 0, i, i + WINDOW_HEIGHT);
        }
        for(int y = 0; y < WINDOW_HEIGHT; y += GRID_BLOCK_SIZE){
            context.strokeLine(0, y, y +  WINDOW_WIDTH, y);
        }
    }

    private void drawSnake() {
        checkDrawDirection(head, headLocation);
        for(int i=0; i<tail.size();i++){
            if(board.foundFood(snake)){
                System.out.println("Sram");
            }
            if(i==0){
                drawSnakeCurve(body, snakeBodyCurve, i, headLocation);
            } else {
                if (i < tail.size() - 1) {
                    drawSnakeCurve(body, snakeBodyCurve, i, tail.get(i - 1));
                } else {
                    drawSnakePart(snakeTail, tail.get(i), 0);
                }
            }
        }
    }

    private void drawSnakeCurve(Image body, Image snakeBodyCurve, int i, Point point) {
        if (tail.get(i).getX() - GRID_BLOCK_SIZE == point.getX() && tail.get(i).getY() == tail.get(i + 1).getY() || tail.get(i).getX() + GRID_BLOCK_SIZE == point.getX() && tail.get(i).getY() == tail.get(i + 1).getY()) {
            drawSnakePart(body, tail.get(i), 0);
        } else if (tail.get(i).getX() == point.getX() && tail.get(i).getY() - GRID_BLOCK_SIZE == tail.get(i + 1).getY() || tail.get(i).getX() == point.getX() && tail.get(i).getY() + GRID_BLOCK_SIZE == tail.get(i + 1).getY()) {
            drawSnakePart(body, tail.get(i), 90);
        }
        if (curveLeftTop(tail, i, point) || curveTopLeft(tail, i, point)) {
            drawSnakePart(snakeBodyCurve, tail.get(i), 0);
            System.out.println("curvelefttop");
        } else if (curveRightTop(tail, i, point) || curveBottomLeft(tail, i, point)) {
            drawSnakePart(snakeBodyCurve, tail.get(i), 90);
            System.out.println("curverighttop");
        } else if (curveLeftBottom(tail, i, point) || curveBottomRight(tail, i, point)) {
            drawSnakePart(snakeBodyCurve, tail.get(i), 180);
            System.out.println("curveleftbottom");
        } else if (curveRightBottom(tail, i, point) || curveTopRight(tail, i, point)) {
            drawSnakePart(snakeBodyCurve, tail.get(i), 270);
            System.out.println("curvelefttop");
        }
    }

    private void checkDrawDirection(Image head, Point headLocation) {
        if (snake.getDirection() == Direction.UP) {
            if (tail.get(0).getY() == headLocation.getY() + GRID_BLOCK_SIZE) {
                drawSnakePart(head, headLocation, 270);
            } else if (tail.get(0).getY() == headLocation.getY() && tail.get(0).getX() == headLocation.getX() + GRID_BLOCK_SIZE){
                drawSnakePart(head, headLocation, 180);
            } else{
                drawSnakePart(head, headLocation, 0);
            }
        } else if (snake.getDirection() == Direction.DOWN) {
            if (tail.get(0).getY() == headLocation.getY() - GRID_BLOCK_SIZE) {
                drawSnakePart(head, headLocation, 90);
            } else if (tail.get(0).getY() == headLocation.getY() && tail.get(0).getX() == headLocation.getX() + GRID_BLOCK_SIZE){
                drawSnakePart(head, headLocation, 180);
            } else{
                drawSnakePart(head, headLocation, 0);
            }
        } else if (snake.getDirection() == Direction.RIGHT) {
            if (tail.get(0).getX() == headLocation.getX() - GRID_BLOCK_SIZE) {
                drawSnakePart(head, headLocation, 0);
            } else if (tail.get(0).getY() == headLocation.getY() + GRID_BLOCK_SIZE && tail.get(0).getX() == headLocation.getX()){
                drawSnakePart(head, headLocation, 270);
            } else{
                drawSnakePart(head, headLocation, 90);
            }
        } else if (snake.getDirection() == Direction.LEFT) {
            if (tail.get(0).getX() == headLocation.getX() + GRID_BLOCK_SIZE) {
                drawSnakePart(head, headLocation, 180);
            } else if (tail.get(0).getY() == headLocation.getY() + GRID_BLOCK_SIZE && tail.get(0).getX() == headLocation.getX()){
                drawSnakePart(head, headLocation, 270);
            } else{
                drawSnakePart(head, headLocation, 90);

            }
        }
    }

    private void drawSnakePart(Image head, Point headLocation, int i) {
        drawRotatedImage(context, head, i, headLocation.getX(), headLocation.getY(), GRID_BLOCK_SIZE, GRID_BLOCK_SIZE);
    }

    private boolean curveRightBottom(List<Point> tail, int i, Point prevpoint) {
        return tail.get(i).getX() + GRID_BLOCK_SIZE == prevpoint.getX() && tail.get(i).getY() - GRID_BLOCK_SIZE == tail.get(i + 1).getY();
    }

    private boolean curveLeftTop(List<Point> tail, int i, Point prevpoint) {
        return tail.get(i).getX() + GRID_BLOCK_SIZE == prevpoint.getX() && tail.get(i).getY() + GRID_BLOCK_SIZE == tail.get(i + 1).getY();
    }

    private boolean curveRightTop(List<Point> tail, int i, Point prevpoint) {
        return tail.get(i).getX() - GRID_BLOCK_SIZE == prevpoint.getX() && tail.get(i).getY() + GRID_BLOCK_SIZE == tail.get(i + 1).getY();
    }

    private boolean curveLeftBottom(List<Point> tail, int i, Point prevpoint) {
        return tail.get(i).getX() - GRID_BLOCK_SIZE == prevpoint.getX() && tail.get(i).getY() - GRID_BLOCK_SIZE == tail.get(i + 1).getY();
    }

    private boolean curveTopRight(List<Point> tail, int i, Point prevpoint) {
        return tail.get(i).getX() + GRID_BLOCK_SIZE == tail.get(i + 1).getX() && tail.get(i).getY() - GRID_BLOCK_SIZE == prevpoint.getY();
    }

    private boolean curveTopLeft(List<Point> tail, int i, Point prevpoint) {
        return tail.get(i).getX() + GRID_BLOCK_SIZE == tail.get(i + 1).getX() && tail.get(i).getY() + GRID_BLOCK_SIZE == prevpoint.getY();
    }

    private boolean curveBottomLeft(List<Point> tail, int i, Point prevpoint) {
        return tail.get(i).getX() - GRID_BLOCK_SIZE == tail.get(i + 1).getX() && tail.get(i).getY() + GRID_BLOCK_SIZE == prevpoint.getY();
    }

    private boolean curveBottomRight(List<Point> tail, int i, Point prevpoint) {
        return tail.get(i).getX() - GRID_BLOCK_SIZE == tail.get(i + 1).getX() && tail.get(i).getY() - GRID_BLOCK_SIZE == prevpoint.getY();
    }

     private void drawFood() {
        context.setFill(Color.GREEN);
        context.fillOval(board.getFood().getLocation().getX(), board.getFood().getLocation().getY(), GRID_BLOCK_SIZE, GRID_BLOCK_SIZE);
        for(int i=0; i<tail.size();i++){
            if(board.getFood().getLocation().getX() == tail.get(i).getX() && board.getFood().getLocation().getY() == tail.get(i).getY() ){
                board.addFood();
                System.out.println("Nowe jedzenie");
            }
        }
    }
}
