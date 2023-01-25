import java.awt.*;

public class Cell {

    final int width;
    final int height;
    int xPosition;
    int yPosition;
    Color color;
    private boolean isAlive;


    public Cell(int x, int y) {
        this.width = Grid.cellWidth;
        this.height = Grid.cellHeight;
        this.xPosition = x;
        this.yPosition = y;
        this.color = Color.white;
        this.isAlive = false;
    }

    //method responsible for drawing the cell on the screen
    public void draw(Graphics2D g2D) {
        g2D.setColor(color);
        g2D.fillRect(this.xPosition, this.yPosition, this.width, this.height);
        g2D.setColor(Color.decode("#cfcccc"));
        g2D.drawRect(this.xPosition, this.yPosition, this.width, this.height);
    }

    //this method will check all it's neighbouring cells and find how many are alive it will then pass on that number
    //to the age method
    public void checkNeighbours() {
        //getting ordinal position of cell in the grid
        int ordinalX = this.xPosition / width;
        int ordinalY = this.yPosition / height;

        //getting the positions of the cells who's state needs to be checked
        int[] ordinalXToCheck;
        int[] ordinalYToCheck;

        //if cell is on the right or left of the square don't check ordinalX that are bigger or smaller to avoid error
        if (ordinalX == 0) {
            ordinalXToCheck = new int[2];
            ordinalXToCheck[0] = ordinalX;
            ordinalXToCheck[1] = ordinalX + 1;
        } else if (ordinalX == Grid.grid.length - 1) {
            ordinalXToCheck = new int[2];
            ordinalXToCheck[0] = ordinalX;
            ordinalXToCheck[1] = ordinalX - 1;
        } else {
            ordinalXToCheck = new int[]{ordinalX - 1, ordinalX, ordinalX + 1};//if the square is not on an edge then check all x coordinates
        }

        //if cell is on top or bottom then don't check ordinalY that are bigger or smaller to avoid errors
        if (ordinalY == 0) {
            ordinalYToCheck = new int[2];
            ordinalYToCheck[0] = ordinalY;
            ordinalYToCheck[1] = ordinalY + 1;
        } else if (ordinalY == Grid.grid.length - 1) {
            ordinalYToCheck = new int[2];
            ordinalYToCheck[0] = ordinalY;
            ordinalYToCheck[1] = ordinalY - 1;
        } else {
            ordinalYToCheck = new int[]{ordinalY - 1, ordinalY, ordinalY + 1};
        }

        int numberOfAliveCellsAdjacent = 0;

        //looping through all the coordinates to check
        for (int x : ordinalXToCheck) {
            for (int y : ordinalYToCheck) {
                //if not checking itself
                if (!(x == ordinalX && y == ordinalY)) {
                    //checking to see if neighbours are alive
                    if (Grid.grid[x][y].getState()) {
                        numberOfAliveCellsAdjacent++;
                    }
                }
            }
        }
        age(numberOfAliveCellsAdjacent);
    }


    //this function encapsulates the rules of the game of life and will change the state of the cell in the futureGrid
    public void age(int aliceCellsInVicinity) {
        //future grid cell equivalent
        Cell futureCell = Grid.futureGrid[this.xPosition / width][this.yPosition / width];
        //if the cell is alive then
        if (this.isAlive) {
            if (aliceCellsInVicinity >= 2 && aliceCellsInVicinity <= 3) {
                futureCell.setAlive();
            } else {
                futureCell.setDead();
            }
        }//if the cell is dead
        else {
            if (aliceCellsInVicinity == 3) {
                futureCell.setAlive();
            }
        }
    }

    public boolean getState() {
        return this.isAlive;
    }

    public void setDead() {
        this.isAlive = false;
        this.color = Color.white;
    }

    public void setAlive() {
        this.isAlive = true;
        this.color = Color.decode("#e04ff0");
    }


}
