package buildMap;

import javafx.scene.layout.Pane;

public class Tile {
    private Pane pane;
    private int x;
    private int y;
    private int state;

    public Tile(Pane pane, int x, int y,int state){
        this.pane = pane;
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public Pane getPane(){ return pane; }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getState() { return state;}

    public void setPane(Pane pane){
        this.pane=pane;
    }

    public void setX(int x){ this.x=x; }

    public void setY(int y){
        this.y=y;
    }

    public void setState(int state) { this.state=state; }
}
