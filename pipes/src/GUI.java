import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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


        ImageIcon straight = new ImageIcon("src/imgs/Straight.png");
        ImageIcon fork = new ImageIcon("src/imgs/Fork.png");
        ImageIcon corner = new ImageIcon("src/imgs/Corner.png");
        ImageIcon end = new ImageIcon("src/imgs/End.png");

        int buttonSize = 80;

        // Add buttons to the grid panel corresponding to the grid
        for (int i = 0; i < grid.dimensions; i++)
        {
            for (int j = 0; j < grid.dimensions; j++)
            {
                Cell.Segment segment = grid.grid[i][j].getSegment();

                RotatedButton button;
                switch (segment) {
                    case Straight -> {button = new RotatedButton(straight);}
                    case Fork -> {button = new RotatedButton(fork);}
                    case Corner -> {button = new RotatedButton(corner);}
                    case End -> {button = new RotatedButton(end);}
                    default -> throw new AssertionError();
                }
                
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));

    
                // Add action listener to update the button's label on click
                button.addActionListener(e ->
                {
                    button.rotate(90); // Rotate the button by 90 degrees
                    button.repaint(); // Repaint the button to reflect the rotation
                });

                button.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if (SwingUtilities.isRightMouseButton(e))
                        {
                            button.rotateReverse(90); // Rotate the button by 90 degrees counterclockwise
                        }
                    }
                });
    
                // Add the button to the grid panel
                gridPanel.add(button);
            }
        }
    
        // Add the grid panel to the frame
        frame.add(gridPanel);

        int totalWidth = grid.dimensions * buttonSize;
        int totalHeight = grid.dimensions * buttonSize;

        int decorationWidth = 0; // Adjust based decoration needs
        int decorationHeight = 0; // Adjust based decoration needs
        totalWidth += decorationWidth;
        totalHeight += decorationHeight;

        frame.setSize(totalWidth, totalHeight);
        frame.setLocationRelativeTo(null);
    
        // Make the frame visible
        frame.setVisible(true);
    }
}

