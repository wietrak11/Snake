package game;

import com.sun.javafx.scene.traversal.Direction;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;


public class Snake {

    private Direction direction;
    private Point headLocation = new Point(0, 0);
    private List<Point> tail = new ArrayList<Point>();
    public  ListIterator<Point> it = getTail().listIterator();
    private int height;
    private int width;
    private int blockSize;
    private boolean isCollidedWithWall  = false;

    public Snake(int width, int height, int blockSize){
        this.width = width;
        this.height = height;
        this.blockSize = blockSize;
        this.direction = Direction.RIGHT;
    }

    public void snakeUpdate(){
        if (tail.size() > 0) {
            tail.remove(tail.size() - 1);
            tail.add(0, new Point(headLocation.getX(), headLocation.getY()));
        }

        switch (direction) {
            case UP:
                headLocation.setY(headLocation.getY() - blockSize);
                if (headLocation.getY() < 0) {
                    isCollidedWithWall = true;
                    headLocation.setY(0);
                }
                break;

            case DOWN:
                headLocation.setY(headLocation.getY() + blockSize);
                if (headLocation.getY() >= height) {
                    isCollidedWithWall = true;
                    headLocation.setY(height - blockSize);
                }
                break;

            case LEFT:
                headLocation.setX(headLocation.getX() - blockSize);
                if (headLocation.getX() < 0) {
                    isCollidedWithWall = true;
                    headLocation.setX(0);
                }
                break;

            case RIGHT:
                headLocation.setX(headLocation.getX() + blockSize);
                if (headLocation.getX() >= width) {
                    isCollidedWithWall = true;
                    headLocation.setX(width - blockSize);
                }
                break;

            default:
                break;
        }
    }


    public boolean colidedWithWall(){
        return isCollidedWithWall;
    }

    public boolean colidedWithTail(){
        boolean isCollision = false;

        for (Point tailSegment : tail) {
            if (headLocation.equals(tailSegment)) {
                isCollision = true;
                break;
            }
        }

        return isCollision;
    }
    public void addTailSegment() {
        if(tail.isEmpty()){
            tail.add(new Point(headLocation.getX(), headLocation.getY()));
        } else{
            tail.add(new Point(tail.get(tail.size() - 1).getX(), tail.get(tail.size() - 1).getY()));
        }
/*
        System.out.println("Add tail segment");
*/
    }

    public void setDirection(Direction myDirection) {
        this.direction = myDirection;
    }

    public Direction getDirection(){
        return direction;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setHeadLocation(int x, int y) {
        headLocation.setX(x);
        headLocation.setY(y);
    }

    public Point getHeadLocation() {
        return headLocation;
    }

    public List<Point> getTail() {
        return tail;
    }




}
