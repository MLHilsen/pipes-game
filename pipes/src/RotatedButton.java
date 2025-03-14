import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RotatedButton extends JButton
{
    private double angle = 0; // Current rotation angle in radians
    private final ImageIcon icon;
    private boolean locked = false;
    private Timer holdTimer;
    private static boolean isMousePressed = false;
    private static boolean isLockingOperation = false;
    private final Cell cell;
    private final Grid grid;
    private final Runnable onGridComplete;
    private final int buttonSize;
    private final GUI gui;

    public RotatedButton(ImageIcon originalIcon, Cell cell, int buttonSize, Grid grid, Runnable onGridComplete, GUI gui)
    {
        // Scale the image to fit the button size
        this.cell = cell;
        this.grid = grid;
        this.onGridComplete = onGridComplete;
        this.buttonSize = buttonSize;
        this.gui = gui;

        this.icon = scaleIcon(originalIcon, buttonSize, buttonSize); // Replace 80 with your button size
        setIcon(this.icon); // Set the scaled icon

        setContentAreaFilled(false); // Make the button background transparent
        setBorderPainted(false); // Remove the border
        setFocusPainted(false); // Remove the focus border
        setFocusable(false); // Remove button focusability

        holdTimer = new Timer(250, e -> {
            isLockingOperation = !locked; // Set the operation type based on the initial cell's state
            setLocked(!locked);
        });
        holdTimer.setRepeats(false); // Ensure the timer only fires once

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    holdTimer.start(); // Start the timer on mouse press
                    isMousePressed = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    holdTimer.stop(); // Stop the timer on mouse release
                    isMousePressed = false;
                    isLockingOperation = false;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isMousePressed && holdTimer.isRunning()) {
                    // Ignore drag events until the hold timer completes
                    return;
                }

                if (isMousePressed) {
                    // Get the component under the mouse pointer
                    Component component = SwingUtilities.getDeepestComponentAt(
                            getParent(), e.getX() + getX(), e.getY() + getY());

                    if (component instanceof RotatedButton button) {
                        if (isLockingOperation && !button.isLocked()) {
                            button.setLocked(true); // Lock the cell
                        } else if (!isLockingOperation && button.isLocked()) {
                            button.setLocked(false); // Unlock the cell
                        }
                    }
                }
            }
        });
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

    public void rotate(double degrees) {
        if (!locked) {
            angle += Math.toRadians(degrees); // Increment the angle by 90 degrees
            normalizeAngle();
            cell.rotate((int) degrees);
            repaint();

            grid.revalidateFlooding(gui);
        }
    }

    public void rotateReverse(double degrees)
    {
        if (!locked)
        {
            angle -= Math.toRadians(degrees); // Decrement the angle by the specified degrees
            normalizeAngle();
            cell.rotate(-(int) degrees); 
            repaint(); // Repaint the button to reflect the rotation

            grid.revalidateFlooding(gui);
        }
    }

    private void normalizeAngle() {
        // Normalize the angle to stay within 0-360 degrees
        angle = angle % (2 * Math.PI); // Use modulo to keep the angle within 0-360 degrees
        if (angle < 0) {
            angle += 2 * Math.PI; // Ensure the angle is positive
        }
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
        cell.setLocked(locked);

        // Check if the grid is complete
        if (grid.isComplete())
        {
            // Execute the callback
            if (onGridComplete != null)
            {
                onGridComplete.run();
            }
        }

        repaint();
    }

    public boolean isLocked(){
        return locked;
    }

    public void updateIcon(boolean filled)
    {
        ImageIcon newIcon;
        switch (cell.getSegment()) {
            case Straight -> newIcon = filled ? new ImageIcon(RotatedButton.class.getResource("/imgs/Straight_Filled.png")) : new ImageIcon(RotatedButton.class.getResource("/imgs/Straight.png"));
            case Fork -> newIcon = filled ? new ImageIcon(RotatedButton.class.getResource("/imgs/Fork_Filled.png")) : new ImageIcon(RotatedButton.class.getResource("/imgs/Fork.png"));
            case Corner -> newIcon = filled ? new ImageIcon(RotatedButton.class.getResource("/imgs/Corner_Filled.png")) : new ImageIcon(RotatedButton.class.getResource("/imgs/Corner.png"));
            case End -> newIcon = filled ? new ImageIcon(RotatedButton.class.getResource("/imgs/End_Filled.png")) : new ImageIcon(RotatedButton.class.getResource("/imgs/End.png"));
            default -> throw new AssertionError();
        }
        setIcon(scaleIcon(newIcon, buttonSize, buttonSize));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();

        if (locked) {
            g2d.setColor(new Color(0, 0, 0, 50)); // Semi-transparent black overlay
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

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