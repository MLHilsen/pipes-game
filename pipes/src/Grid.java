import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Grid
{
    public Cell[][] grid;
    public int dimensions;
    
    public void createGrid(int dimensions)
    {
        Random random = new Random();
        this.dimensions = dimensions;
        this.grid = new Cell[dimensions][dimensions];
        
        for (int i = 0; i < dimensions; i++)
        {
            for (int j = 0; j < dimensions; j++)
            {
                grid[i][j] = new Cell();
                grid[i][j].setCoords(i, j);
            }
        }

        // Set Water Source
        int i = random.nextInt(dimensions);
        int j = random.nextInt(dimensions);
        grid[i][j].setSource(true);
        grid[i][j].setFilled(true);

        breakWalls(random);
    }

    public boolean verifyRotations()
    {
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                Cell cell = grid[i][j];

                if (cell.isLocked() && !cell.isCorrectlyRotated()) {return false;} // Check if cell is locked and correctly rotated
            }
        }
        return true;
    }

    public boolean isComplete()
    {
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                Cell cell = grid[i][j];

                if (!cell.isLocked()) {return false;} // Check if cell is locked
            }
        }

        return true;
    }

    public void drainCells(GUI gui) {
        // Create a copy of the grid to track changes
        boolean[][] wasFilled = new boolean[dimensions][dimensions];
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                wasFilled[i][j] = grid[i][j].isFilled();
            }
        }
    
        // Drain cells that no longer touch a flooded cell
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                Cell cell = grid[i][j];
                if (cell.isFilled() && !cell.isSource()) {
                    // Check if the cell still touches a flooded cell
                    boolean shouldDrain = true;
                    if (i - 1 >= 0 && wasFilled[i - 1][j] && cell.neighborsLink(cell, grid[i - 1][j])) {
                        shouldDrain = false; // Connected to a flooded cell above
                    }
                    if (i + 1 < dimensions && wasFilled[i + 1][j] && cell.neighborsLink(cell, grid[i + 1][j])) {
                        shouldDrain = false; // Connected to a flooded cell below
                    }
                    if (j - 1 >= 0 && wasFilled[i][j - 1] && cell.neighborsLink(cell, grid[i][j - 1])) {
                        shouldDrain = false; // Connected to a flooded cell to the left
                    }
                    if (j + 1 < dimensions && wasFilled[i][j + 1] && cell.neighborsLink(cell, grid[i][j + 1])) {
                        shouldDrain = false; // Connected to a flooded cell to the right
                    }
    
                    if (shouldDrain) {
                        cell.setFilled(false); // Drain the cell
                        updateButtonIcon(cell, gui); // Update the button icon
                    }
                }
            }
        }
    }

    public void floodNeighbors_r(Cell cell, GUI gui) {
        /* 
        * Get a list of all immediate neighbors
        * Check if walls connect with flooded cell
        * If do, flood and add THAT cell's neighbors to list
        * Repeat until list is empty
        */
        // Get the coordinates of the current cell
        int i = cell.coords[0];
        int j = cell.coords[1];
    
        // Check all four neighbors (up, down, left, right)
        // Up
        if (i - 1 >= 0) {
            Cell neighbor = grid[i - 1][j];
            if (!neighbor.isFilled() && cell.neighborsLink(cell, neighbor)) {
                neighbor.setFilled(true);
                updateButtonIcon(neighbor, gui);
                floodNeighbors_r(neighbor, gui); // Recursively flood the neighbor
            }
        }
        // Down
        if (i + 1 < dimensions) {
            Cell neighbor = grid[i + 1][j];
            if (!neighbor.isFilled() && cell.neighborsLink(cell, neighbor)) {
                neighbor.setFilled(true);
                updateButtonIcon(neighbor, gui);
                floodNeighbors_r(neighbor, gui); // Recursively flood the neighbor
            }
        }
        // Left
        if (j - 1 >= 0) {
            Cell neighbor = grid[i][j - 1];
            if (!neighbor.isFilled() && cell.neighborsLink(cell, neighbor)) {
                neighbor.setFilled(true);
                updateButtonIcon(neighbor, gui);
                floodNeighbors_r(neighbor, gui); // Recursively flood the neighbor
            }
        }
        // Right
        if (j + 1 < dimensions) {
            Cell neighbor = grid[i][j + 1];
            if (!neighbor.isFilled() && cell.neighborsLink(cell, neighbor)) {
                neighbor.setFilled(true);
                updateButtonIcon(neighbor, gui);
                floodNeighbors_r(neighbor, gui); // Recursively flood the neighbor
            }
        }
    }

    public void revalidateFlooding(GUI gui) {
        // Clear the flooded state of all cells (except the source)
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                Cell cell = grid[i][j];
                if (!cell.isSource()) {
                    cell.setFilled(false);
                    updateButtonIcon(cell, gui); // Update the button icon
                }
            }
        }
    
        // Re-flood the grid starting from the source
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                Cell cell = grid[i][j];
                if (cell.isSource()) {
                    floodNeighbors_r(cell, gui); // Flood from the source
                }
            }
        }
    }

    private void updateButtonIcon(Cell cell, GUI gui) {
        RotatedButton button = gui.getCellButtonMap().get(cell);
        if (button != null) {
            button.updateIcon(cell.isFilled()); // Update the icon based on the filled state
            button.repaint(); // Repaint the button to reflect the change
        }
    }

    private List<Cell> getNeighbors(int i, int j, boolean visited) // row, col
    {
        List<Cell> neighbors = new ArrayList<>();

        // Check Up
        if ((i - 1 >= 0) && (grid[i - 1][j].isVisited() == visited))
        {
            neighbors.add(grid[i - 1][j]);
        }
        // Check Down
        if ((i + 1 < dimensions) && (grid[i + 1][j].isVisited() == visited))
        {
            neighbors.add(grid[i + 1][j]);
        }
        // Check Left
        if ((j - 1 >= 0) && (grid[i][j - 1].isVisited() == visited))
        {
            neighbors.add(grid[i][j - 1]);
        }
        // Check Right
        if ((j + 1 < dimensions) && (grid[i][j + 1].isVisited() == visited))
        {
            neighbors.add(grid[i][j + 1]);
        }

        return neighbors;
    }

    private void breakWall(Cell first, Cell second)
    {
        if (first.coords[0] == second.coords[0]) // If the ROW is the same
        {
            if (first.coords[1] > second.coords[1]) // If first is to the RIGHT of second
            {
                first.hasLeftWall = false;
                second.hasRightWall = false;
            }
            else // If first is to the LEFT of second
            {
                first.hasRightWall = false;
                second.hasLeftWall = false;
            }
        }
        else if (first.coords[1] == second.coords[1]) // If the COL is the same
        {
            if (first.coords[0] < second.coords[0]) // If first is ABOVE second
            {
                first.hasBottomWall = false;
                second.hasTopWall = false;
            }
            else // If first is BELOW second
            {
                    first.hasTopWall = false;
                    second.hasBottomWall = false;
            }
        }        
    }

    public void breakWalls(Random random)
    {
        _breakWalls_r(random, random.nextInt(dimensions), random.nextInt(dimensions));
        setCellSegments();
    }

    private void _breakWalls_r(Random random, int i, int j)
    {
        Cell current = grid[i][j];
        current.visit();

        while (true)
        { 
            List<Cell> frontier = new ArrayList<>();
            frontier.addAll(getNeighbors(current.coords[0], current.coords[1], false));

            if (frontier.isEmpty())
            {
                return;
            }

            Cell moveTo = frontier.get(random.nextInt(frontier.size()));
            breakWall(current, moveTo);

            _breakWalls_r(random, moveTo.coords[0], moveTo.coords[1]);
        }
    }
    
    private void setCellSegments()
    {
        for (Cell[] row : grid)
        {
            for (Cell current : row)
            {
                int numWalls = current.numWalls();

                switch (numWalls) {
                    case 1 -> // One Wall = Fork
                        current.setSegment(Cell.Segment.Fork);

                    case 3 -> // Three Walls = End
                        current.setSegment(Cell.Segment.End);

                    case 2 -> {
                        // Two Walls = Straight & Corner
                        if ((current.hasBottomWall && current.hasTopWall) || (current.hasLeftWall && current.hasRightWall))
                        {
                            current.setSegment(Cell.Segment.Straight);
                        }
                        else
                        {
                            current.setSegment(Cell.Segment.Corner);
                        }
                    }

                    default -> {
                        System.out.println("Didnt work");
                        System.out.println(Arrays.toString(current.coords));
                        System.out.println(numWalls);
                    }
                }
            }
        }
        
    }
}