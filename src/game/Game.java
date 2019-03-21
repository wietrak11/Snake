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
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import java.util.*;
public class Game{

    private static final long TASK_UPDATE_PERIOD_MS = 70;
    private static final long TASK_UPDATE_DELAY_MS = TASK_UPDATE_PERIOD_MS;


    private static final int WINDOW_HEIGHT = 800;
    private static final int WINDOW_WIDTH = 800;
    private static final int BLOCK_COUNT = 20;
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


        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.UP && snake.getDirection() != Direction.DOWN && stearing == false){
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
                        drawPoints();
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
        List<Point> tail = snake.getTail();
        javafx.scene.image.Image head = new Image("/game/snakehead.png");
        javafx.scene.image.Image body = new Image("/game/snakebody.png");
        context.setFill(new ImagePattern(head));
        context.fillRect(snake.getHeadLocation().getX(), snake.getHeadLocation().getY(), snake.getBlockSize(), snake.getBlockSize());

        for(int i=0; i<snake.getTail().size();i++){
            if(i==0){
                context.setFill(Color.BLACK);
                context.fillRect(tail.get(i).getX(), tail.get(i).getY(), snake.getBlockSize(), snake.getBlockSize());
            } else {
                if(i < tail.size() - 1){
                    if (curveLeftTop(tail, i)) {
                        drawRect(Color.GREEN, tail.get(i), i);
                    } else if (curveLeftBottom(tail, i)) {
                        drawRect(Color.YELLOW, tail.get(i), i);
                    } else if (curveRightTop(tail, i)) {
                        drawRect(Color.YELLOWGREEN, tail.get(i), i);
                    } else if (CurveRightBottom(tail, i)) {
                        drawRect(Color.BLUE, tail.get(i), i);
                    } else {
                        context.setFill(Color.BLACK);
                        context.fillRect(tail.get(i).getX(), tail.get(i).getY(), snake.getBlockSize(), snake.getBlockSize());
                    }
                } else {
                    context.setFill(new ImagePattern(body));
                    context.fillRect(tail.get(i).getX(), tail.get(i).getY(), snake.getBlockSize(), snake.getBlockSize());
                }
            }
        }
    }

    private boolean CurveRightBottom(List<Point> tail, int i) {
        return tail.get(i).getX() + GRID_BLOCK_SIZE == tail.get(i - 1).getX() && tail.get(i).getY() - GRID_BLOCK_SIZE == tail.get(i + 1).getY();

    }

    private boolean curveRightTop(List<Point> tail, int i) {
        return tail.get(i).getX() + GRID_BLOCK_SIZE == tail.get(i - 1).getX() && tail.get(i).getY() + GRID_BLOCK_SIZE == tail.get(i + 1).getY();

    }

    private boolean curveLeftBottom(List<Point> tail, int i) {
        return tail.get(i).getX() - GRID_BLOCK_SIZE == tail.get(i - 1).getX() && tail.get(i).getY() - GRID_BLOCK_SIZE == tail.get(i + 1).getY();

    }

    private boolean curveLeftTop(List<Point> tail, int i) {
        return tail.get(i).getX() - GRID_BLOCK_SIZE == tail.get(i - 1).getX() && tail.get(i).getY() + GRID_BLOCK_SIZE == tail.get(i + 1).getY();
    }

    private void checkNodePosition(){

    }

    private void drawRect(Color color, Point tail, int i){
        context.setFill(color);
        context.fillRect(tail.getX(), tail.getY(), snake.getBlockSize(), snake.getBlockSize());
    }

    private void drawFood() {
        context.setFill(Color.GREEN);
        context.fillOval(board.getFood().getLocation().getX(), board.getFood().getLocation().getY(), GRID_BLOCK_SIZE, GRID_BLOCK_SIZE);
        for(int i=0; i<snake.getTail().size();i++){
            if(board.getFood().getLocation().getX() == snake.getTail().get(i).getX() && board.getFood().getLocation().getY() == snake.getTail().get(i).getY() ){
                board.addFood();
                System.out.println("Nowe jedzenie");
            }
        }
    }

}
