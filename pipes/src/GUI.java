import java.awt.*;
import javax.swing.*;

public class GUI
{
    public void createGUI(Grid grid)
    {
        JFrame frame = new JFrame("Grid Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);


        // Create a panel with GridLayout to hold the grid of cells
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(grid.dimensions, grid.dimensions)); // Layout for a grid of buttons

        // Add buttons to the grid panel corresponding to the grid
        for (int i = 0; i < grid.dimensions; i++) {
            for (int j = 0; j < grid.dimensions; j++) {
                JButton button = new JButton(grid.grid[i][j].printCell());
                button.setPreferredSize(new Dimension(80, 80));

                final int row = i;
                final int col = j;
    
                // Add action listener to update the button's label on click
                button.addActionListener(e -> {
                    Cell clickedCell = grid.grid[row][col];
                    //clickedCell.toggleWallState(); // Toggle some state in the cell (for example)
                    button.setText(clickedCell.toString()); // Update the button text to show the cell's state
                });
    
                // Add the button to the grid panel
                gridPanel.add(button);
            }
        }
    
        // Add the grid panel to the frame
        frame.add(gridPanel);
    
        // Make the frame visible
        frame.setVisible(true);
    }
}