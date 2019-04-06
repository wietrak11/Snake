package buildMap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import gameMenu.GameMenu;

public class BuildMapController {
    private int BOARD_TILE_WIDTH = 40;
    private int BOARD_TILE_HEIGHT = 40;
    private double TILE_WIDTH = 720/BOARD_TILE_WIDTH;
    private double TILE_HEIGHT = 600/BOARD_TILE_HEIGHT;

    private Stage stage;
    private Tile[][] tileArray = new Tile[BOARD_TILE_WIDTH][BOARD_TILE_HEIGHT];
    private int selectedSizeOfBoard = 3;
    private boolean snakeSetting = false;
    @FXML private GridPane board;
    @FXML private Button smallerButton;
    @FXML private Button biggerButton;
    @FXML private Button setSnakeButton;
    @FXML private Label sizeInfo;

    public void setGrid(){
        for(int i = 0; i< BOARD_TILE_WIDTH; i++){
            for(int j = 0; j< BOARD_TILE_HEIGHT; j++){
                Tile tile = new Tile(new Pane(), i, j, 0);
                tileArray[i][j] = tile;
                tileArray[i][j].getPane().setStyle("-fx-background-color: lightgrey;" +
                                                   "-fx-border-color: black;" +
                                                   "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                tileArray[i][j].getPane().setPrefWidth(TILE_WIDTH);
                tileArray[i][j].getPane().setPrefHeight(TILE_HEIGHT);
                GridPane.setConstraints(tileArray[i][j].getPane(),i,j);

                board.getChildren().add(tileArray[i][j].getPane());
            }
        }
    }

    public void setMouseEnterListener(){
        board.addEventHandler(MouseEvent.MOUSE_ENTERED,
                me -> {
                    //tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].getPane().
                });
    }

    public void setMouseClickedListener(){
        board.addEventHandler(MouseEvent.MOUSE_CLICKED,
                me -> {
                    if(me.getX()>=0 && me.getX()<720 && me.getY()>=0 && me.getY()<600) {
                        if (me.getButton().equals(MouseButton.PRIMARY)) {
                            tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: #162630;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].setState(1);
                        }
                        if (me.getButton().equals(MouseButton.SECONDARY)) {
                            tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: lightgrey;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].setState(0);
                        }
                    }
                });
    }

    public void setMouseDraggedListener(){
        board.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                me -> {
                    if(me.getX()>=0 && me.getX()<720 && me.getY()>=0 && me.getY()<600){
                        if(me.isPrimaryButtonDown()) {
                            tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: #162630;" +
                                                                                                                          "-fx-border-color: black;" +
                                                                                                                          "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].setState(1);
                        }
                        if(me.isSecondaryButtonDown()) {
                            tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: lightgrey;" +
                                                                                                                          "-fx-border-color: black;" +
                                                                                                                          "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getX() / TILE_WIDTH)][(int) (me.getY() / TILE_HEIGHT)].setState(0);
                        }
                    }
                });
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
        if(snakeSetting == false){
            snakeSetting = true;
            setSnakeButton.setStyle("-fx-text-fill: #E8521E;" +
                    "-fx-text-effect: dropshadow(one-pass-box, #917528, 10, 10.0, 20, 40);");
        }
        else{
            snakeSetting=false;
            setSnakeButton.setStyle("-fx-text-fill: #223D35;");
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