package buildMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BuildMap {

    private Stage stage;
    static BuildMapController buildMapControllerHandle;

    public BuildMap(Stage primaryStage){
        this.stage = primaryStage;
    }

    public void start() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("buildMap.fxml"));
        Parent root = loader.load();
        buildMapControllerHandle = (BuildMapController)loader.getController();
        buildMapControllerHandle.setStage(stage);
        buildMapControllerHandle.setGrid();
        buildMapControllerHandle.setMouseClickedListener();
        buildMapControllerHandle.setMouseDraggedListener();

        Scene scene = new Scene(root,800,800);
        scene.getStylesheets().add("buildMap/buildMap.css");
        Font.loadFont(BuildMap.class.getResource("/fonts/AdventPro-Light.ttf").toExternalForm(),10);
        stage.setTitle("Snake");
        stage.getIcons().add(new Image("/image/icon.jpg"));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}