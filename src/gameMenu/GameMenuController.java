package gameMenu;

import game.Game;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import menu.Menu;

public class GameMenuController {
    Stage stage = new Stage();

    public void startGame(MouseEvent mouseEvent) {

        try {
            new Game(stage).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void buildMap(MouseEvent mouseEvent) {

    }

    public void simulation(MouseEvent mouseEvent) {

    }

    public void back(MouseEvent mouseEvent){

        try {
            new Menu(stage).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

}
