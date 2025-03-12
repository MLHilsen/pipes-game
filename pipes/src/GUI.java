import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

public class GUI
{
    private final Map<Cell, RotatedButton> cellButtonMap = new HashMap<>();

    public Map<Cell, RotatedButton> getCellButtonMap() {
        return cellButtonMap;
    }

    public void createGUI(Grid grid)
    {
        JFrame frame = new JFrame("Pipes Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel with GridLayout to hold the grid of cells
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(grid.dimensions, grid.dimensions)); // Layout for a grid of buttons


        ImageIcon straight = new ImageIcon(GUI.class.getResource("/imgs/Straight.png"));
        ImageIcon fork = new ImageIcon(GUI.class.getResource("/imgs/Fork.png"));
        ImageIcon corner = new ImageIcon(GUI.class.getResource("/imgs/Corner.png"));
        ImageIcon end = new ImageIcon(GUI.class.getResource("/imgs/End.png"));

        ImageIcon straight_source = new ImageIcon(GUI.class.getResource("/imgs/Straight_Source.png"));
        ImageIcon fork_source = new ImageIcon(GUI.class.getResource("/imgs/Fork_Source.png"));
        ImageIcon corner_source = new ImageIcon(GUI.class.getResource("/imgs/Corner_Source.png"));
        ImageIcon end_source = new ImageIcon(GUI.class.getResource("/imgs/End_Source.png"));

        // Get Screen Size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Calculate maximum button size based on screen dimensions and grid size
        double maxButtonWidth = (screenWidth * 0.8) / grid.dimensions;
        double maxButtonHeight = (screenHeight * 0.8) / grid.dimensions;
        int buttonSize = (int) Math.min(maxButtonWidth, maxButtonHeight); // Use the smaller value to ensure the grid fits

        int totalWidth = grid.dimensions * buttonSize;
        int totalHeight = grid.dimensions * buttonSize;

        int decorationWidth = frame.getInsets().left + frame.getInsets().right; // Adjust based decoration needs
        int decorationHeight = frame.getInsets().top + frame.getInsets().bottom; // Adjust based decoration needs

        totalWidth += decorationWidth;
        totalHeight += decorationHeight;

        // Set the preferred size of the grid panel
        gridPanel.setPreferredSize(new Dimension(totalWidth - decorationWidth, totalHeight - decorationHeight));

        // Add buttons to the grid panel corresponding to the grid
        for (int i = 0; i < grid.dimensions; i++)
        {
            for (int j = 0; j < grid.dimensions; j++)
            {
                Cell cell = grid.grid[i][j];
                Cell.Segment segment = grid.grid[i][j].getSegment();

                RotatedButton button;
                switch (segment) {
                    case Straight -> {
                        if (grid.grid[i][j].isSource()) {button = new RotatedButton(straight_source, cell, buttonSize, grid, () -> displayVerifierMessage(frame, grid), this);}
                        else {button = new RotatedButton(straight, cell, buttonSize, grid, () -> displayVerifierMessage(frame, grid), this);}
                    }
                    case Fork -> {
                        if (grid.grid[i][j].isSource()) {button = new RotatedButton(fork_source, cell, buttonSize, grid, () -> displayVerifierMessage(frame, grid), this);}
                        else {button = new RotatedButton(fork, cell, buttonSize, grid, () -> displayVerifierMessage(frame, grid), this);}
                    }
                    case Corner -> {
                        if (grid.grid[i][j].isSource()) {button = new RotatedButton(corner_source, cell, buttonSize, grid, () -> displayVerifierMessage(frame, grid), this);}
                        else {button = new RotatedButton(corner, cell, buttonSize, grid, () -> displayVerifierMessage(frame, grid), this);}
                    }
                    case End -> {
                        if (grid.grid[i][j].isSource()) {button = new RotatedButton(end_source, cell, buttonSize, grid, () -> displayVerifierMessage(frame, grid), this);}
                        else {button = new RotatedButton(end, cell, buttonSize, grid, () -> displayVerifierMessage(frame, grid), this);}
                    }
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
    
                cellButtonMap.put(cell, button); // Add to hashmap

                // Add the button to the grid panel
                gridPanel.add(button);
                randomlyRotateButton(button);
            }
        }

        // Add a verifier button
        JButton verifyButton = new JButton("Verify Rotations");
        verifyButton.addActionListener(e -> {
            displayVerifierMessage(frame, grid);
        });
    
        // Add the elements to the frame
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(verifyButton, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false); 

        frame.setSize(totalWidth, totalHeight);
        frame.setLocationRelativeTo(null);
    
        // Make the frame visible
        frame.setVisible(true);
    }

    public void randomlyRotateButton(RotatedButton button)
    {
        Random random = new Random();
        // Generate a random multiple of 90 (0, 90, 180, or 270)
        int rotation = random.nextInt(4) * 90;
        // Apply the rotation to the cell
        button.rotate(rotation);
    }

    public void displayVerifierMessage(JFrame frame, Grid grid)
    {
        boolean isValid = grid.verifyRotations();
        boolean isComplete = grid.isComplete();

        if (isValid && isComplete)
        {
            JOptionPane.showMessageDialog(frame, "Puzzle complete!");
        }
        else if (isValid)
        {
            JOptionPane.showMessageDialog(frame, "So far so good!");
        } else
        {
            JOptionPane.showMessageDialog(frame, "Some rotations are incorrect.");
        }
    }
}

