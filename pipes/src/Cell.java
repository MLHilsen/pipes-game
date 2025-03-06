import java.util.Arrays;
import java.util.List;

public class Cell {
    private Segment segment;
    private boolean visited = false;
    public boolean hasLeftWall = true, hasRightWall = true, hasTopWall = true, hasBottomWall = true;
    public int[] coords = new int[2];
    
    public enum Segment
    {
        Straight,   // 2 Walls
        Corner,     // 2 Walls
        Fork,       // 1 Wall
        End         // 3 Walls
    }
    
    public int numWalls()
    {
        List<Boolean> walls = Arrays.asList(hasBottomWall, hasTopWall, hasLeftWall, hasRightWall);

        int numWalls = 0;
        for (Boolean wall : walls) {
            if (wall) {
                numWalls++;
            }
        }

        return numWalls;
    }

    public void setSegment(Segment segment)
    {
        this.segment = segment;
    }

    public Segment getSegment()
    {
        return segment;
    }

    public void setCoords(int i, int j)
    {
        coords[0] = i;
        coords[1] = j;
    }

    public void visit()
    {
        this.visited = true;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public String printCell()
    {
        return switch (segment) {
            case Straight -> "|";
            case Corner -> "L";
            case Fork -> "T";
            case End -> "O";
        };
    }
}
