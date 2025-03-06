import java.util.ArrayList;
import java.util.List;

public class Grid {
    @SuppressWarnings("FieldMayBeFinal")
    private int dimentions = 5;
    @SuppressWarnings("FieldMayBeFinal")
    private Cell[][] grid = new Cell[dimentions][dimentions];
    
    public void createGrid(int dimentions)
    {
        for (int i = 0; i < dimentions; i++) {
            for (int j = 0; j < dimentions; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public List<Cell> getNeighbors(int i, int j) // row, col
    {
        List<Cell> neighbors = new ArrayList<>();

        // Check Up
        if ((i - 1 >= 0) && (!grid[i - 1][j].isVisited()))
        {
            neighbors.add(grid[i - 1][j]);
        }
        // Check Down
        if ((i + 1 <= dimentions) && (!grid[i + 1][j].isVisited()))
        {
            neighbors.add(grid[i + 1][j]);
        }
        // Check Left
        if ((j - 1 >= 0) && (!grid[i][j - 1].isVisited()))
        {
            neighbors.add(grid[i][j - 1]);
        }
        // Check Right
        if ((j + 1 <= dimentions) && (!grid[i][j + 1].isVisited()))
        {
            neighbors.add(grid[i][j + 1]);
        }

        return neighbors;
    }

    public void breakWalls_r()
    {
        /*
         * Visit intro cell
         * Get all valid directions
         * Choose one at random
         * Break walls between cells
         * Rerun func from target cell
         * Repeat until back to start
         */
        grid[0][0].visit();

    }

    public void printGrid()
    {
        for (int i = 0; i < dimentions; i++) {
            for (int j = 0; j < dimentions; j++) {
                System.out.print(grid[i][j].printCell());
            }
            System.out.println();
        }
    }

    public void testing()
    {
        System.out.print(getNeighbors(1, 1));
    }
}







/*
 * Create a grid of cells of dimention X dimention size
 * Use random algorithm to 'break walls' to create a loop
 * Place correct pipe tile in each cell given how many walls are broken
 * Shuffle rotation of every cell
 * Choose either a random cell or the center cell as the water source
 */