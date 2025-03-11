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
        System.out.println(this.rotation); // Debug
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

    public boolean hasTopWallAtRotation(int rotation) {
        switch (segment) {
            case Straight -> {if (rotation == 90 || rotation == 270) {return true;}}
            case Fork -> {if (rotation == 270) {return true;}}
            case Corner -> {if (rotation == 180 || rotation == 270) {return true;}}
            case End -> {if (rotation == 0 || rotation == 180 || rotation == 270) {return true;}}
            default -> throw new IllegalArgumentException("Invalid rotation: " + rotation);
        }

        return false;
    }
    
    public boolean hasBottomWallAtRotation(int rotation) {
        switch (segment) {
            case Straight -> {if (rotation == 90 || rotation == 270) {return true;}}
            case Fork -> {if (rotation == 90) {return true;}}
            case Corner -> {if (rotation == 0 || rotation == 90) {return true;}}
            case End -> {if (rotation == 0 || rotation == 90 || rotation == 180) {return true;}}
            default -> throw new IllegalArgumentException("Invalid rotation: " + rotation);
        }

        return false;
    }
    
    public boolean hasLeftWallAtRotation(int rotation) {
        switch (segment) {
            case Straight -> {if (rotation == 0 || rotation == 180) {return true;}}
            case Fork -> {if (rotation == 180) {return true;}}
            case Corner -> {if (rotation == 90 || rotation == 180) {return true;}}
            case End -> {if (rotation == 90 || rotation == 180 || rotation == 270) {return true;}}
            default -> throw new IllegalArgumentException("Invalid rotation: " + rotation);
        }

        return false;
    }
    
    public boolean hasRightWallAtRotation(int rotation) {
        switch (segment) {
            case Straight -> {if (rotation == 0 || rotation == 180) {return true;}}
            case Fork -> {if (rotation == 0) {return true;}}
            case Corner -> {if (rotation == 0 || rotation == 270) {return true;}}
            case End -> {if (rotation == 0 || rotation == 90 || rotation == 270) {return true;}}
            default -> throw new IllegalArgumentException("Invalid rotation: " + rotation);
        }

        return false;
    }

    public boolean neighborsLink(Cell current, Cell neighbor) {
        int currentRotation = current.getRotation();
        int neighborRotation = neighbor.getRotation();
    
        // Determine the relative positions of the cells
        int dx = neighbor.coords[0] - current.coords[0]; // Row difference
        int dy = neighbor.coords[1] - current.coords[1]; // Column difference
    
        // Check if the cells are adjacent
        if (Math.abs(dx) + Math.abs(dy) != 1) {
            return false; // Cells are not adjacent
        }
    
        // Determine which walls to check based on the relative positions
        if (dx == -1) { // Neighbor is above
            // Check if the current cell's top wall and the neighbor's bottom wall are open
            return !current.hasTopWallAtRotation(currentRotation) && !neighbor.hasBottomWallAtRotation(neighborRotation);
        } else if (dx == 1) { // Neighbor is below
            // Check if the current cell's bottom wall and the neighbor's top wall are open
            return !current.hasBottomWallAtRotation(currentRotation) && !neighbor.hasTopWallAtRotation(neighborRotation);
        } else if (dy == -1) { // Neighbor is to the left
            // Check if the current cell's left wall and the neighbor's right wall are open
            return !current.hasLeftWallAtRotation(currentRotation) && !neighbor.hasRightWallAtRotation(neighborRotation);
        } else if (dy == 1) { // Neighbor is to the right
            // Check if the current cell's right wall and the neighbor's left wall are open
            return !current.hasRightWallAtRotation(currentRotation) && !neighbor.hasLeftWallAtRotation(neighborRotation);
        }
    
        return false;
    }

    public boolean hasExposedWallToFilledNeighbor(Grid grid, Cell cell)
    {
        int i = cell.coords[0];
        int j = cell.coords[1];
    
        // Check Up
        if (i - 1 >= 0 && grid.grid[i - 1][j].isFilled() && neighborsLink(cell, grid.grid[i - 1][j])) {
            return true;
        }
        // Check Down
        else if (i + 1 < grid.dimensions && grid.grid[i + 1][j].isFilled() && neighborsLink(cell, grid.grid[i + 1][j])) {
            return true;
        }
        // Check Left
        else if (j - 1 >= 0 && grid.grid[i][j - 1].isFilled() && neighborsLink(cell, grid.grid[i][j - 1])) {
            return true;
        }
        // Check Right
        else if (j + 1 < grid.dimensions && grid.grid[i][j + 1].isFilled() && neighborsLink(cell, grid.grid[i][j + 1])) {
            return true;
        }
    
        return false;
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
