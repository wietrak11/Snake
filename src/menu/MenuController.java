package menu;

import gameMenu.GameMenu;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class MenuController {
    Stage stage = new Stage();

    public void game(MouseEvent mouseEvent) {
        try {
            Thread.sleep(100);
            new GameMenu(stage).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void option(MouseEvent mouseEvent) {

    }

    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void hyperlinkMateusz(MouseEvent mouseEvent){
        try {
            Desktop.getDesktop().browse(new URL("https://www.linkedin.com/in/mateusz-wietrak-2935b0166/").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void hyperlinkDamian(MouseEvent mouseEvent){
        try {
            Desktop.getDesktop().browse(new URL("https://www.linkedin.com/in/damian-wicik-098b41175/").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

}
