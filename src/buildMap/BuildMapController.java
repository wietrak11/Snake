package buildMap;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import gameMenu.GameMenu;

public class BuildMapController {
    private int BOARD_TILE_WIDTH = 40;
    private int BOARD_TILE_HEIGHT = 40;
    private double TILE_WIDTH = 720/BOARD_TILE_WIDTH;
    private double TILE_HEIGHT = 600/BOARD_TILE_HEIGHT;

    private Stage stage;
    private CustomPane[][] paneArray = new CustomPane[BOARD_TILE_WIDTH][BOARD_TILE_HEIGHT];
    private int selectedSizeOfBoard = 3;
    private boolean snakeSetting = false;

    EventHandler<MouseEvent> mouseAddSnakeOnEnter = null;
    EventHandler<MouseEvent> mouseRemoveSnakeOnExit = null;
    EventHandler<MouseEvent> mouseDraw = null;

    @FXML private GridPane board;
    @FXML private Button smallerButton;
    @FXML private Button biggerButton;
    @FXML private Button setSnakeButton;
    @FXML private Label sizeInfo;

    public void initiateEventHandlers(){

        Image image = null;
        image = new Image("/image/snakebody.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(TILE_HEIGHT);
        imageView.setFitWidth(TILE_WIDTH);
        imageView.setPreserveRatio(true);
        imageView.setTranslateX(2.0);
        imageView.setScaleX(1.3);

        Image image2 = null;
        image2 = new Image("/image/snakehead.png");
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitHeight(TILE_HEIGHT);
        imageView2.setFitWidth(TILE_WIDTH);
        imageView2.setPreserveRatio(true);

        Image image3 = null;
        image3 = new Image("/image/snakebody.png");
        ImageView imageView3 = new ImageView(image3);
        imageView3.setFitHeight(TILE_HEIGHT);
        imageView3.setFitWidth(TILE_WIDTH);
        imageView3.setPreserveRatio(true);
        imageView3.setTranslateX(1.8);
        imageView3.setScaleX(1.2);

        mouseAddSnakeOnEnter = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent mouseEvent) {
                CustomPane x = (CustomPane) mouseEvent.getTarget();

                if(x.getX()>0 && x.getX()<BOARD_TILE_WIDTH-1){
                    if(x.getState()==0 && paneArray[x.getX()+1][x.getY()].getState()==0 && paneArray[x.getX()-1][x.getY()].getState()==0){
                        x.getChildren().add(imageView);
                        paneArray[x.getX()+1][x.getY()].getChildren().add(imageView2);
                        paneArray[x.getX()-1][x.getY()].getChildren().add(imageView3);
                    }
                }
                else if(x.getX()==0 || x.getX()==BOARD_TILE_WIDTH-1){
                    if(x.getY()>0 && x.getY()<BOARD_TILE_HEIGHT-1){
                        if(x.getState()==0 && paneArray[x.getX()][x.getY()+1].getState()==0 && paneArray[x.getX()][x.getY()-1].getState()==0){
                            x.getChildren().add(imageView);
                            paneArray[x.getX()][x.getY()+1].getChildren().add(imageView2);
                            paneArray[x.getX()][x.getY()-1].getChildren().add(imageView3);
                        }
                    }
                }
            }
        };

        mouseRemoveSnakeOnExit = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent mouseEvent) {
                CustomPane x = (CustomPane) mouseEvent.getTarget();

                if(x.getChildren().contains(imageView)){
                    if(x.getX()==0 || x.getX()==BOARD_TILE_WIDTH-1) {
                        if (x.getY() > 0 && x.getY() < BOARD_TILE_HEIGHT - 1) {
                            x.getChildren().remove(imageView);
                            paneArray[x.getX()][x.getY() + 1].getChildren().remove(imageView2);
                            paneArray[x.getX()][x.getY() - 1].getChildren().remove(imageView3);
                        }
                    }
                    else{
                        x.getChildren().remove(imageView);
                        paneArray[x.getX()+1][x.getY()].getChildren().remove(imageView2);
                        paneArray[x.getX()-1][x.getY()].getChildren().remove(imageView3);
                    }
                }
            }
        };

        mouseDraw = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent event) {
                if(event.getX()>=0 && event.getX()<720 && event.getY()>=0 && event.getY()<600) {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        paneArray[(int) (event.getX() / TILE_WIDTH)][(int) (event.getY() / TILE_HEIGHT)].setStyle("-fx-background-color: #162630;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                        paneArray[(int) (event.getX() / TILE_WIDTH)][(int) (event.getY() / TILE_HEIGHT)].setState(1);
                    }
                    if (event.getButton().equals(MouseButton.SECONDARY)) {
                        paneArray[(int) (event.getX() / TILE_WIDTH)][(int) (event.getY() / TILE_HEIGHT)].setStyle("-fx-background-color: lightgrey;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                        paneArray[(int) (event.getX() / TILE_WIDTH)][(int) (event.getY() / TILE_HEIGHT)].setState(0);
                    }
                }
            }
        };

    }

    public void setGrid(){
        for(int i = 0; i< BOARD_TILE_WIDTH; i++){
            for(int j = 0; j< BOARD_TILE_HEIGHT; j++){
                CustomPane pane = new CustomPane(i,j);
                paneArray[i][j] = pane;
                pane.setStyle("-fx-background-color: lightgrey;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                pane.setPrefWidth(TILE_WIDTH);
                pane.setPrefHeight(TILE_HEIGHT);
                GridPane.setConstraints(pane,i,j);
                board.getChildren().add(pane);
            }
        }
    }

    public void enableMouseEnterListener(){
        for(int i=0 ; i<BOARD_TILE_HEIGHT ; i++){
            for(int j=0 ; j<BOARD_TILE_WIDTH ; j++){
                paneArray[i][j].addEventHandler(MouseEvent.MOUSE_ENTERED, mouseAddSnakeOnEnter);
                paneArray[i][j].addEventHandler(MouseEvent.MOUSE_EXITED, mouseRemoveSnakeOnExit);
                disableMouseClickedDraw();
                disableMouseDraggedDraw();
            }
        }
    }

    public void disableMouseEnterListener(){
        for(int i=0 ; i<BOARD_TILE_HEIGHT ; i++){
            for(int j=0 ; j<BOARD_TILE_WIDTH ; j++){
                paneArray[i][j].removeEventHandler(MouseEvent.MOUSE_ENTERED, mouseAddSnakeOnEnter);
                paneArray[i][j].removeEventHandler(MouseEvent.MOUSE_EXITED, mouseRemoveSnakeOnExit);
                enableMouseDraggedDraw();
                enableMouseClickedDraw();
            }
        }
    }

    public void enableMouseClickedDraw(){
        board.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseDraw);
    }

    public void disableMouseClickedDraw(){
        board.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseDraw);
    }

    public void enableMouseDraggedDraw(){
        board.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraw);
        /*
        board.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                me -> {
                    if(me.getX()>=0 && me.getX()<720 && me.getY()>=0 && me.getY()<600){
                        if(me.isPrimaryButtonDown()) {
                            paneArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].setStyle("-fx-background-color: #162630;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            paneArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].setState(1);
                        }
                        if(me.isSecondaryButtonDown()) {
                            paneArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].setStyle("-fx-background-color: lightgrey;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            paneArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].setState(0);
                        }
                    }
                });
         */
    }

    public void disableMouseDraggedDraw(){
        board.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraw);
    }

    public void back(MouseEvent mouseEvent){
        try {
            new GameMenu(stage).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set20(){
        if(BOARD_TILE_WIDTH!=20 && BOARD_TILE_HEIGHT!=20){
            selectedSizeOfBoard = 1;
            BOARD_TILE_HEIGHT=20;
            BOARD_TILE_WIDTH=20;
            TILE_WIDTH = 720/BOARD_TILE_WIDTH;
            TILE_HEIGHT = 600/BOARD_TILE_HEIGHT;
            board.getChildren().clear();
            setGrid();
            initiateEventHandlers();
            if(snakeSetting){
                setSnake();
            }
        }
    }

    public void set30(){
        if(BOARD_TILE_WIDTH!=30 && BOARD_TILE_HEIGHT!=30){
            selectedSizeOfBoard = 2;
            BOARD_TILE_HEIGHT=30;
            BOARD_TILE_WIDTH=30;
            TILE_WIDTH = 720/BOARD_TILE_WIDTH;
            TILE_HEIGHT = 600/BOARD_TILE_HEIGHT;
            board.getChildren().clear();
            setGrid();
            initiateEventHandlers();
            if(snakeSetting){
                setSnake();
            }
        }
    }

    public void set40(){
        if(BOARD_TILE_WIDTH!=40 && BOARD_TILE_HEIGHT!=40){
            selectedSizeOfBoard = 3;
            BOARD_TILE_HEIGHT=40;
            BOARD_TILE_WIDTH=40;
            TILE_WIDTH = 720/BOARD_TILE_WIDTH;
            TILE_HEIGHT = 600/BOARD_TILE_HEIGHT;
            board.getChildren().clear();
            setGrid();
            initiateEventHandlers();
            if(snakeSetting){
                setSnake();
            }
        }
    }

    public void setSmallerSize(MouseEvent mouseEvent){
        switch(selectedSizeOfBoard){
            case 1:
                set30();
                smallerButton.setText("20x20");
                biggerButton.setText("40x40");
                sizeInfo.setText("Size - " + BOARD_TILE_WIDTH + "x" + BOARD_TILE_HEIGHT);
                break;
            case 2:
                set20();
                smallerButton.setText("30x30");
                biggerButton.setText("40x40");
                sizeInfo.setText("Size - " + BOARD_TILE_WIDTH + "x" + BOARD_TILE_HEIGHT);
                break;
            case 3:
                set20();
                smallerButton.setText("30x30");
                biggerButton.setText("40x40");
                sizeInfo.setText("Size - " + BOARD_TILE_WIDTH + "x" + BOARD_TILE_HEIGHT);
                break;
        }
    }

    public void setBiggerSize(MouseEvent mouseEvent){
        switch(selectedSizeOfBoard){
            case 1:
                set40();
                smallerButton.setText("20x20");
                biggerButton.setText("30x30");
                sizeInfo.setText("Size - " + BOARD_TILE_WIDTH + "x" + BOARD_TILE_HEIGHT);
                break;
            case 2:
                set40();
                smallerButton.setText("20x20");
                biggerButton.setText("30x30");
                sizeInfo.setText("Size - " + BOARD_TILE_WIDTH + "x" + BOARD_TILE_HEIGHT);
                break;
            case 3:
                set30();
                smallerButton.setText("20x20");
                biggerButton.setText("40x40");
                sizeInfo.setText("Size - " + BOARD_TILE_WIDTH + "x" + BOARD_TILE_HEIGHT);
                break;
        }
    }

    public void clearBoard(MouseEvent mouseEvent){
        board.getChildren().clear();
        setGrid();
    }

    public void setSnake(MouseEvent mouseEvent){
        setSnake();
    }

    public void setSnake(){
        if(snakeSetting == false){
            snakeSetting = true;
            setSnakeButton.setStyle("-fx-text-fill: #E8521E;");
            enableMouseEnterListener();
        }
        else{
            snakeSetting=false;
            setSnakeButton.setStyle("-fx-text-fill: #223D35;");
            disableMouseEnterListener();
        }
    }

    public void load(MouseEvent mouseEvent){

    }

    public void save(MouseEvent mouseEvent){

    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

}