import java.util.Arrays;
import java.util.List;

public class Cell {
    private Segment segment;
    private boolean visited = false;
    public boolean hasLeftWall = true, hasRightWall = true, hasTopWall = true, hasBottomWall = true;
    public int[] coords = new int[2];
    public int rotation = 0;
    public boolean locked = false;
    public boolean isSource = false;
    public boolean isFilled = false;
    
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

    public boolean isSource() {
        return isSource;
    }

    public void setSource(boolean isSource) {
        this.isSource = isSource;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public void visit()
    {
        this.visited = true;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setRotation(int rotation)
    {
        this.rotation = rotation;
    }

    public void rotate(int degrees)
    {
        this.rotation = (this.rotation + degrees) % 360; // Normalize to 0-360 degrees
        if (this.rotation < 0) {
            this.rotation += 360; // Ensure rotation is positive
        }
    }

    public int getRotation()
    {
        return rotation;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isCorrectlyRotated()
    {
        switch (segment) {
            case Straight -> {
                if (hasBottomWall && hasTopWall && !hasLeftWall && !hasRightWall) // If horizontal straight
                {
                    if (this.rotation == 90 || this.rotation == 270) {return true;}
                }
                if (!hasBottomWall && !hasTopWall && hasLeftWall && hasRightWall) // If vertical straight
                {
                    if (this.rotation == 0 || this.rotation == 180) {return true;}
                }
            }

            case Fork -> {
                if (!hasBottomWall && !hasTopWall && !hasLeftWall && hasRightWall)
                {
                    if (this.rotation == 0) {return true;}
                }
                if (hasBottomWall && !hasTopWall && !hasLeftWall && !hasRightWall)
                {
                    if (this.rotation == 90) {return true;}
                }
                if (!hasBottomWall && !hasTopWall && hasLeftWall && !hasRightWall)
                {
                    if (this.rotation == 180) {return true;}
                }
                if (!hasBottomWall && hasTopWall && !hasLeftWall && !hasRightWall)
                {
                    if (this.rotation == 270) {return true;}
                }
            }
            
            case End -> {
                if (hasBottomWall && hasTopWall && !hasLeftWall && hasRightWall)
                {
                    if (this.rotation == 0) {return true;}
                }
                if (hasBottomWall && !hasTopWall && hasLeftWall && hasRightWall)
                {
                    if (this.rotation == 90) {return true;}
                }
                if (hasBottomWall && hasTopWall && hasLeftWall && !hasRightWall)
                {
                    if (this.rotation == 180) {return true;}
                }
                if (!hasBottomWall && hasTopWall && hasLeftWall && hasRightWall)
                {
                    if (this.rotation == 270) {return true;}
                }
            }
            
            case Corner -> {
                if (hasBottomWall && !hasTopWall && !hasLeftWall && hasRightWall)
                {
                    if (this.rotation == 0) {return true;}
                }
                if (hasBottomWall && !hasTopWall && hasLeftWall && !hasRightWall)
                {
                    if (this.rotation == 90) {return true;}
                }
                if (!hasBottomWall && hasTopWall && hasLeftWall && !hasRightWall)
                {
                    if (this.rotation == 180) {return true;}
                }
                if (!hasBottomWall && hasTopWall && !hasLeftWall && hasRightWall)
                {
                    if (this.rotation == 270) {return true;}
                }
            }
                
            default -> throw new AssertionError();
        }

        return false;
    }
}
