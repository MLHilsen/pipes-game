public class PipesPuzzle
{   
    public static void main(String[] args) throws Exception
    {
        Grid grid = new Grid();

        while (true)
        {
            try
            {
                grid.createGrid(8);
                grid.breakWalls();
                break;
            }
            catch (NullPointerException e)
            {
                System.err.println("Retrying Map Generation...");
            }
        }

        GUI gui = new GUI();
        gui.createGUI(grid);
    }
}
