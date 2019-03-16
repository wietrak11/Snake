package game;

import java.util.Random;

public class Board {
    private int height;
    private int width;
    private int pixelsPerSquare;
    Food food;

    public Board(int width, int height, int pixelsPerSquare) {
        this.width = width;
        this.height = height;
        this.pixelsPerSquare = pixelsPerSquare;
        food = new Food(width / 2, height / 2);
    }

    public void reset() {
        food = new Food(width / 2, height / 2);
    }

    public boolean foundFood(Snake snake) {
        boolean isIntersected = false;

        if (snake.getHeadLocation().equals(food.getLocation())) {
            isIntersected = true;
        }
        return isIntersected;
    }


    public void addFood() {
        Random rand = new Random();
        int y = rand.nextInt(height);
        int x = rand.nextInt(width);

        y = Math.round(y/pixelsPerSquare) * pixelsPerSquare;
        x = Math.round(x/pixelsPerSquare) * pixelsPerSquare;
        food = new Food(x, y);
    }

    public Food getFood() {
        return food;
    }

}
