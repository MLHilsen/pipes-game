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
                RotatedButton button = new RotatedButton(grid.grid[i][j].printCell());
                button.setPreferredSize(new Dimension(80, 80));



    
                // Add action listener to update the button's label on click
                button.addActionListener(e -> {
                    button.rotate(); // Rotate the button by 90 degrees
                    button.repaint(); // Repaint the button to reflect the rotation
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

class RotatedButton extends JButton {
    private double angle = 0; // Current rotation angle in radians

    public RotatedButton(String text) {
        super(text);
        setContentAreaFilled(false); // Make the button background transparent
        setBorderPainted(false); // Remove the border
        setFocusPainted(false); // Remove the focus border
    }

    public void rotate() {
        angle += Math.toRadians(90); // Increment the angle by 90 degrees
        if (angle >= Math.toRadians(360)) {
            angle = 0; // Reset the angle after a full rotation
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Apply rotation transformation
        g2d.rotate(angle, getWidth() / 2.0, getHeight() / 2.0);

        // Paint the button
        super.paintComponent(g2d);

        g2d.dispose();
    }
}