public class PipesPuzzle
{   
    public static void main(String[] args) throws Exception
    {
        Grid grid = new Grid();

        while (true)
        {
            try
            {
                grid.createGrid(16);
                grid.breakWalls();

                GUI gui = new GUI();
                gui.createGUI(grid);
                
                break;
            }
            catch (NullPointerException e)
            {
                System.err.println("Retrying Map Generation...");
            }
        }
    }
}
