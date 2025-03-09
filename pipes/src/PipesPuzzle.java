public class PipesPuzzle
{   
    public static void main(String[] args) throws Exception
    {
        Grid grid = new Grid();
        grid.createGrid(8);
        grid.breakWalls();

        GUI gui = new GUI();
        gui.createGUI(grid);
    }
}
