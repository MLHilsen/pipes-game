public class Grid {
    private int dimentions = 5;
    private Cell[][] grid = new Cell[dimentions][dimentions];
    
    public void createGrid(int dimentions)
    {
        for (int i = 0; i < dimentions; i++) {
            for (int j = 0; j < dimentions; j++) {
                grid[i][j] = new Cell();
            }
        }
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
}







/*
 * Create a grid of cells of dimention X dimention size
 * Use random algorithm to 'break walls' to create a loop
 * Place correct pipe tile in each cell given how many walls are broken
 * Shuffle rotation of every cell
 * Choose either a random cell or the center cell as the water source
 */