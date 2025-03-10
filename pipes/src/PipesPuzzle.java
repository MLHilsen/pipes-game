public class PipesPuzzle
{   
    public static void main(String[] args) throws Exception
    {
        Grid grid = new Grid();
        GUI gui = new GUI();

        while (true)
        {
            try
            {
                grid.createGrid(16);
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
