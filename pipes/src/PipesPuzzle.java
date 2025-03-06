public class PipesPuzzle
{   
    public static void main(String[] args) throws Exception
    {
        Grid grid = new Grid();
        grid.createGrid(5);
        grid.breakWalls();

        GUI gui = new GUI();
        gui.createGUI(grid);
    }
}
