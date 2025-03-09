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
                
                button.setPreferredSize(new Dimension(80, 80));

    
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
    
        // Make the frame visible
        frame.setVisible(true);
    }
}

class RotatedButton extends JButton
{
    private double angle = 0; // Current rotation angle in radians
    private final ImageIcon icon;

    public RotatedButton(ImageIcon originalIcon)
    {
        // Scale the image to fit the button size
        this.icon = scaleIcon(originalIcon, 80, 80); // Replace 80 with your button size
        setIcon(this.icon); // Set the scaled icon

        setContentAreaFilled(false); // Make the button background transparent
        setBorderPainted(false); // Remove the border
        setFocusPainted(false); // Remove the focus border

        setFocusable(false);
    }

    @Override
    protected void processMouseEvent(MouseEvent e)
    {
        // Allow right-clicks to be processed without gaining focus
        if (SwingUtilities.isRightMouseButton(e))
        {
            super.processMouseEvent(e); // Process the event
        }
        else
        {
            super.processMouseEvent(e); // Process other mouse events normally
        }
    }

    public void rotate(double degrees)
    {
        angle += Math.toRadians(degrees); // Increment the angle by 90 degrees
        normalizeAngle();
        repaint();
    }

    public void rotateReverse(double degrees)
    {
        angle -= Math.toRadians(degrees); // Decrement the angle by the specified degrees
        normalizeAngle();
        repaint(); // Repaint the button to reflect the rotation
    }

    private void normalizeAngle() {
        // Normalize the angle to stay within 0-360 degrees
        angle = angle % (2 * Math.PI); // Use modulo to keep the angle within 0-360 degrees
        if (angle < 0) {
            angle += 2 * Math.PI; // Ensure the angle is positive
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();

        // Apply rotation transformation
        g2d.rotate(angle, getWidth() / 2.0, getHeight() / 2.0);

        // Paint the button
        super.paintComponent(g2d);

        g2d.dispose();
    }

    private ImageIcon scaleIcon(ImageIcon icon, int width, int height)
    {
        Image image = icon.getImage(); // Get the image from the icon
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Scale the image
        return new ImageIcon(scaledImage); // Return the scaled image as an ImageIcon
    }
}