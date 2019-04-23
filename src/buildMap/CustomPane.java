package buildMap;

import javafx.scene.layout.Pane;

public class CustomPane extends Pane {
    int x;
    int y;
    int state;

    public CustomPane(int x, int y){
        this.x=x;
        this.y=y;
        this.state=0;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state = state;
    }
}
