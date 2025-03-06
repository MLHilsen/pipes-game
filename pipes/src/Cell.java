public class Cell {
    private Segment segment = Segment.Straight;
    private boolean visited = false;
    private String[] walls = {"up", "down", "left", "right"};
    
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
