import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class Grid extends JPanel implements Runnable {

    public static final int cellWidth = 20;
    public static final int cellHeight = 20;
    private static final int width = 800;
    private static final int height = 800;
    public static Cell[][] grid;
    public static Cell[][] futureGrid;

    boolean gameStopped = false;

    int generation;
    JLabel generationText;


    public Grid() {

        this.setPreferredSize(new Dimension(width, height));
        this.setFocusable(true);

        setUpGrid();

        //adding mouse and key listeners to the JPanel
        this.addKeyListener(new KeyboardListener());
        this.addMouseListener(new MouseDetector());

        //creating generation text at the top of the screen
        this.generation = 0;
        this.generationText = new JLabel();
        generationText.setFont(new Font("Helvetica", Font.PLAIN, 20));
        generationText.setText("generation number: "+generation);
        this.add(generationText);

    }

    //this method is responsible for drawing to the JPanel and is called every time the repaint() method is called
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        drawCells(g2D);
    }

    //iterating through all the cells and drawing them on the jpanel
    public void drawCells(Graphics2D g2D) {
        for (Cell[] cells : grid) {
            for (int y = 0; y < grid.length; y++) {
                cells[y].draw(g2D);
            }
        }
    }

    //this method is responsible for creating the grid and the future grid arrays
    private void setUpGrid() {
        setGrid();
        setFutureGrid();
    }

    public void setFutureGrid() {
        futureGrid = new Cell[width / cellWidth][height / cellHeight]; //doesn't work without this line and i don't understand why
        for (int x = 0; x < width; x += cellWidth) {
            for (int y = 0; y < height; y += cellHeight) {
                futureGrid[x / cellWidth][y / cellHeight] = new Cell(x, y);//setting up the future grid
            }
        }
    }

    public void setGrid() {
        grid = new Cell[width / cellWidth][height / cellHeight];
        for (int x = 0; x < width; x += cellWidth) {
            for (int y = 0; y < height; y += cellHeight) {
                grid[x / cellWidth][y / cellHeight] = new Cell(x, y);//setting up the grid
            }
        }
    }


    public void startGameOfLife() {
        Thread game = new Thread(this);
        game.start();
    }

    //this method will associate the position where mouse was clicked to the cell that occupies that position on screen
    //it will then set that cell to alive or dead and repaint the panel to show the change
    //FUTURE CHANGE: CAN BE CHANGED SO IT REPAINTS ONLY THE CELL THAT WAS CLICKED AND NOT ALL THE CELLS
    public void clickCell(int clickedX, int clickedY) {
        int numberOfCellOnXAxis = clickedX / cellWidth;
        int numberOfCellOnYAxis = clickedY / cellHeight;

        Cell cellClicked = grid[numberOfCellOnXAxis][numberOfCellOnYAxis];

        //check to see if cell clicked is already alive if so set it to dead()
        if (cellClicked.getState()) {
            cellClicked.setDead();
        } else {
            cellClicked.setAlive();
        }
        repaint();//repainting the screen so the clicked cells are visible
    }

    //this method loops through every cell runs the checkNeighbours method()
    //once all the cells have checked their neighbours and updated their state on the future grid
    //making grid a copy of future grid an resetting the future grid
    //then finally repainting the grid
    public void runChecks() {
        for (Cell[] cells : grid) {
            for (int y = 0; y < grid.length; y++) {
                cells[y].checkNeighbours();
            }
        }
        grid = Arrays.copyOf(futureGrid, futureGrid.length);
        setFutureGrid();
        repaint();
    }

    //updating the game of life every 200ms
    @Override
    public void run() {
        while (!gameStopped) {
            runChecks();
            updateGeneration();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateGeneration() {
        generation++;
        generationText.setText("generation number: " + generation);
    }

    //this method is run when the user presses the key "c" and will reset the game
    protected void clear() {
        setUpGrid();
        runChecks();
        generation = 0;
        generationText.setText("generation number: " + generation);
    }

    //setting up a mouse listener to see if a cell is clicked
    public class MouseDetector implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //checking the coordinates of the click and drawing the particular square
            clickCell(e.getX(), e.getY());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    //setting up keyboard listener
    public class KeyboardListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                runChecks();
            }
            switch (e.getKeyCode()) {
//                case KeyEvent.VK_SPACE:
//                    runChecks();
//                    break;
                case KeyEvent.VK_S:
                    gameStopped = false;
                    startGameOfLife();
                    break;
                case KeyEvent.VK_P:
                    gameStopped = true;
                    break;
                case KeyEvent.VK_C:
                    gameStopped = true;
                    clear();
                    break;
            }
        }
    }

}
