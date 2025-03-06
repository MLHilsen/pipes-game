import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Grid
{
    public int dimensions = 5;
    public Cell[][] grid = new Cell[dimensions][dimensions];
    
    public void createGrid(int dimentions)
    {
        for (int i = 0; i < dimentions; i++)
        {
            for (int j = 0; j < dimentions; j++)
            {
                grid[i][j] = new Cell();
                grid[i][j].setCoords(i, j);
            }
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
        if (first.coords[0] == 0 && first.coords[1] == 0)
        {
            System.out.println("Checking (0, 0), Top Wall: " + first.hasTopWall);
        }
        
    }

    public void breakWalls()
    {
        Random random = new Random();
        _breakWalls(random, random.nextInt(dimensions), random.nextInt(dimensions));
    }

    private void _breakWalls(Random random, int i, int j)
    {
        // Scanner scanner = new Scanner(System.in);
        /*
         * Visit intro cell
         * Get all valid directions
         * Choose one at random
         * Break walls between cells
         * Rerun func from target cell
         * Repeat until back to start
         */

        grid[i][j].visit();
        List<Cell> frontier = new ArrayList<>();
        frontier.addAll(getNeighbors(i, j, false));

        // System.out.println(Arrays.toString(grid[i][j].coords));
        // System.out.println("");
        // for (Cell cell : frontier) {
        //     System.out.println(Arrays.toString(cell.coords));
        // }
        // System.out.println("");

        while (!frontier.isEmpty())
        {
            Cell current = frontier.remove(random.nextInt(frontier.size()));

            List<Cell> visitedNeighbors = getNeighbors(current.coords[0], current.coords[1], true);

            if (!visitedNeighbors.isEmpty())
            {
                Cell visitedNeighbor = visitedNeighbors.get(random.nextInt(visitedNeighbors.size()));

                // Break wall between current and visitedNeighbor
                System.out.printf("Breaking wall between: (%d, %d) and (%d, %d)%n", current.coords[0], current.coords[1], visitedNeighbor.coords[0], visitedNeighbor.coords[1]);
                breakWall(current, visitedNeighbor);
            }

            current.visit();
            frontier.addAll(getNeighbors(current.coords[0], current.coords[1], false));
            
            Set<Cell> frontierSet = new HashSet<>(frontier);
            frontier = new ArrayList<>(frontierSet);

            // System.out.println("");
            // for (Cell cell : frontier) {
            //     System.out.println(Arrays.toString(cell.coords));
            // }
            // String userInput = scanner.nextLine();
        }
    }

    public void printGrid()
    {
        for (int i = 0; i < dimensions; i++)
        {
            for (int j = 0; j < dimensions; j++)
            {
                System.out.print(grid[i][j].printCell());
            }
            System.out.println();
        }
    }

    public void testing()
    {
        System.out.printf("Has Bottom Wall: %b%n", grid[0][0].hasBottomWall);
        System.out.printf("Has Top Wall: %b%n", grid[0][0].hasTopWall);
        System.out.printf("Has Left Wall: %b%n", grid[0][0].hasLeftWall);
        System.out.printf("Has Right Wall: %b%n", grid[0][0].hasRightWall);
    }
}







/*
 * Create a grid of cells of dimention X dimention size
 * Use random algorithm to 'break walls' to create a loop
 * Place correct pipe tile in each cell given how many walls are broken
 * Shuffle rotation of every cell
 * Choose either a random cell or the center cell as the water source
 */