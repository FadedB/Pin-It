import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.awt.image.BufferedImage;

import javax.swing.*;

/**
 * @author faded
 * @aim Object that handles screen selection for making pins.
 */

public class screenSelection implements MouseListener, MouseMotionListener
{
	// ATTRIBUTES //
	mainInterface mainInterface;
	JFrame screenShotFrame;
	JLayeredPane mainPane;
	JPanel selectionPanel;
	JLabel screenShot;
	
	BufferedImage screenCapture;
	
	Point startLocation;
	Point endLocation;
	boolean dragging;
	
	Robot robot;
	
	// MAIN //
	public screenSelection(mainInterface mainInterface)
	{
		//Initialisation
		this.mainInterface = mainInterface; 
		dragging = false;
		
		screenShotFrame = new JFrame("Screen Selection");
		mainPane = new JLayeredPane();
		selectionPanel = new JPanel();
	    
		//get bounds of screen grab
	    Rectangle screenSizeRectangle = new Rectangle();
	    GraphicsEnvironment localGE = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    for (GraphicsDevice gd : localGE.getScreenDevices()) //loop through all displays
	    {
	    	for (GraphicsConfiguration graphicsConfiguration : gd.getConfigurations()) // loop through all display configurations
	    	{
	    		//combine all regions to create region that covers all displays 
	    		Rectangle.union(screenSizeRectangle, graphicsConfiguration.getBounds(), screenSizeRectangle);
	    	}
	    }
	    
	    int boundsWidth = (int) screenSizeRectangle.getWidth();
	    int boundsHeight = (int) screenSizeRectangle.getHeight();

	    //ugly ui stuff
		mainPane.setLayout(null);
		mainPane.setSize(boundsWidth, boundsHeight);

		screenShotFrame.setAlwaysOnTop(true);
		screenShotFrame.setUndecorated(true);
		screenShotFrame.setSize(boundsWidth, boundsHeight);
		screenShotFrame.setLayout(null);
		
		screenShotFrame.add(mainPane);
		
		selectionPanel.setLayout(null);
		selectionPanel.setBounds(0,0,0,0);
		mainPane.add(selectionPanel, 0);
		selectionPanel.setOpaque(true);
		selectionPanel.setBackground(new Color(0, 0, 0, 0.25f));
		selectionPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 5, true));
		
		try {
			// take a screenshot
			robot = new Robot();
			screenCapture = robot.createScreenCapture(screenSizeRectangle);
			
			// put screenshot in JLabel, display on screen
			screenShot = new JLabel(new ImageIcon(screenCapture));
			screenShot.setLayout(null);
			
			mainPane.add(screenShot, 1);
			screenShot.setLocation(0,0);
			screenShot.setSize(boundsWidth, boundsHeight);
			
			screenShotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			screenShotFrame.setBounds(screenSizeRectangle);
			screenShotFrame.addMouseListener(this);
			screenShotFrame.addMouseMotionListener(this);
			screenShotFrame.setVisible(true);
		} catch (Exception e) 
		{
			// TODO: handle exception, i'll get round to it !! nothing breaks at the moment if this fails.
		}

	}

	// EVENTS //
	
	@Override
	public void mousePressed(MouseEvent e) {
		startLocation = e.getPoint();
		
		dragging = true;		
		selectionPanel.setLocation(startLocation);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragging)
		{
			endLocation = e.getPoint();
			
			//stop selection being dragged outside of screenshot bounds
			boolean valid = true;
			if (selectionPanel.getX() + selectionPanel.getWidth() > screenCapture.getWidth())
			{
				valid = false;
			}
			
			if (selectionPanel.getY() + selectionPanel.getHeight() > screenCapture.getHeight())
			{
				valid = false;
			}
			
			if (valid)
			{
				//crop image and make a new pinned image
				BufferedImage croppedImage = screenCapture.getSubimage(selectionPanel.getX(), selectionPanel.getY(),  selectionPanel.getWidth(), selectionPanel.getHeight());
				mainInterface.newCapture(croppedImage, selectionPanel.getLocationOnScreen());
			}
			
			screenShotFrame.dispose();
			dragging = false;
			
			mainInterface.showImages();
		}	
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point currentMousePos = e.getPoint();
		
		int posX = (int) startLocation.getX();
		int posY = (int) startLocation.getY();
		
		int sizeX = (int) (currentMousePos.getX() - startLocation.getX());
		int sizeY = (int)(currentMousePos.getY() - startLocation.getY());
		
		if (sizeX < 0)
		{
			posX = (int) currentMousePos.getX();
			sizeX = -sizeX;
		}
		
		if (sizeY < 0)
		{
			posY = (int) currentMousePos.getY();
			sizeY = -sizeY;
		}
	
		selectionPanel.setLocation(new Point(posX, posY));
		
		Dimension selectionDimensions = new Dimension(sizeX, sizeY);
		
//		System.out.println(selectionDimensions.toString());
		selectionPanel.setSize(selectionDimensions);	
	}
	
	
	// UNUSED EVENTS //
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}
}
