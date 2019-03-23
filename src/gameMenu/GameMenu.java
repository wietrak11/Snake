package gameMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameMenu {

    private Stage menuStage;
    static GameMenuController gameMenuControllerHandle;

    public GameMenu(Stage primaryStage){
        this.menuStage = primaryStage;
    }

    public void start() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameMenu.fxml"));
        Parent root = loader.load();
        gameMenuControllerHandle = (GameMenuController)loader.getController();
        gameMenuControllerHandle.setStage(menuStage);

        Scene scene = new Scene(root,400,450);
        scene.getStylesheets().add("menu/menu.css");
        Font.loadFont(GameMenu.class.getResource("/fonts/Gamer.ttf").toExternalForm(),10);
        Font.loadFont(GameMenu.class.getResource("/fonts/AdventPro-Light.ttf").toExternalForm(),10);
        menuStage.setTitle("Snake");
        menuStage.getIcons().add(new Image("/image/icon.jpg"));
        menuStage.setScene(scene);
        menuStage.centerOnScreen();
        menuStage.setResizable(false);
        menuStage.show();
    }
}