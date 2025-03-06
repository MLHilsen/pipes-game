public class Cell {
    private Segment segment = Segment.Straight;
    private boolean visited = false;
    public boolean hasLeftWall = true, hasRightWall = true, hasTopWall = true, hasBottomWall = true;
    public int[] coords = new int[2];
    
    enum Segment
    {
        Straight,   // 2 Walls
        Corner,     // 2 Walls
        Fork,       // 1 Wall
        End         // 3 Walls
    }
    
    public void setSegment(Segment segment)
    {
        this.segment = segment;
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
