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