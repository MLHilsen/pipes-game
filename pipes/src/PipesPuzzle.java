public class PipesPuzzle
{   
    public static void main(String[] args) throws Exception
    {
        Grid grid = new Grid();
        grid.createGrid(5);
        // grid.printGrid();
        grid.breakWalls();

        System.out.println();
        grid.testing();


        GUI gui = new GUI();
        gui.createGUI(grid);
    }
}
