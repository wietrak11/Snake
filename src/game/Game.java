package game;
import com.sun.javafx.scene.traversal.Direction;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.shape.Rectangle;

public class Game{

    private static final long TASK_UPDATE_PERIOD_MS = 70;
    private static final long TASK_UPDATE_DELAY_MS = 100;


    private static final int WINDOW_HEIGHT = 800;
    private static final int WINDOW_WIDTH = 800;
    private static final int GRID_BLOCK_SIZE = 20;



    private GraphicsContext graphicsContext;
    private Stage gameStage;
    private GraphicsContext context;
    private Snake snake;
    private Board board;
    private AnimationTimer animationTimer;
    private Timer timer;
    private TimerTask task;
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
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
        Group root = new Group();
        Canvas canvas = new Canvas(WINDOW_WIDTH,WINDOW_HEIGHT + 2*GRID_BLOCK_SIZE);
        context = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        board = new Board(WINDOW_WIDTH, WINDOW_HEIGHT, GRID_BLOCK_SIZE);
        snake = new Snake(WINDOW_WIDTH, WINDOW_HEIGHT, GRID_BLOCK_SIZE);
        snake.setHeadLocation(100, 100);



        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.UP && snake.getDirection() != Direction.DOWN && stearing == false){
                if(snake.getDirection() == Direction.LEFT){
                    snake.addCurve("curveLeftUp");
                } else if(snake.getDirection() == Direction.RIGHT){
                    snake.addCurve("curveRightUp");
                }
                snake.setDirection(Direction.UP);
                stearing = true;
            } else if(e.getCode() == KeyCode.DOWN && snake.getDirection() != Direction.UP && stearing == false){
                if(snake.getDirection() == Direction.LEFT){
                    snake.addCurve("curveLeftDown");
                } else if(snake.getDirection() == Direction.RIGHT){
                    snake.addCurve("curveLeftUp");
                }
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
        context.fillText(Integer.toString(points), Math.round(WINDOW_WIDTH  / 2),WINDOW_HEIGHT + GRID_BLOCK_SIZE + 3);
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
        javafx.scene.image.Image head = new Image("/game/snakehead.png");
        javafx.scene.image.Image body = new Image("/game/snakebody.png");
        context.setFill(Color.PAPAYAWHIP);
        context.fillRect(snake.getHeadLocation().getX(), snake.getHeadLocation().getY(), snake.getBlockSize(), snake.getBlockSize());
        for (Point tailSegment : snake.getTail()) {
            context.setFill(Color.PAPAYAWHIP);
            context.fillRect(tailSegment.getX(), tailSegment.getY(), snake.getBlockSize(), snake.getBlockSize());
        }
        if(snake.getTail().size() == 0){

        } else {
            for(int i = 0; i < snake.getCurve().size(); i++){
                Point[] keys = (Point[]) snake.getCurve().keySet().toArray(new Point[0]);
                    if (snake.getCurve().containsValue("curveLeftUp")) {
                        context.setFill(Color.GREEN);
                        context.fillRect(keys[i].getX(), keys[i].getY(), snake.getBlockSize(), snake.getBlockSize());

                    }
                    if (snake.getCurve().containsValue("curveLeftDown")) {
                        context.setFill(Color.PINK);
                        context.fillRect(keys[i].getX(), keys[i].getY(), snake.getBlockSize(), snake.getBlockSize());
                    }
                    if (snake.getCurve().containsValue("curveRightUp")) {
                        context.setFill(Color.YELLOW);
                        context.fillRect(keys[i].getX(), keys[i].getY(), snake.getBlockSize(), snake.getBlockSize());
                    }
                    if (snake.getCurve().containsValue("curveRightDown")) {
                        context.setFill(Color.BLACK);
                        context.fillRect(keys[i].getX(), keys[i].getY(), snake.getBlockSize(), snake.getBlockSize());
                    }
                    if (snake.getTail().get(snake.getTail().size() - 1).equals(keys[i])) {
                        snake.getCurve().remove(keys[i]);
                }
            }
        }
    }
    private void colorBlock(Color color){
        context.setFill(color);
    }

    private void drawFood() {
        context.setFill(Color.GREEN);
        context.fillOval(board.getFood().getLocation().getX(), board.getFood().getLocation().getY(), GRID_BLOCK_SIZE, GRID_BLOCK_SIZE);
    }




}
