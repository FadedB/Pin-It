
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ImagePin implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener
{
	JFrame frame;
	JLayeredPane mainPane;
	JPanel optionsPanel;
	JLabel capture;
	
	BufferedImage captureImage;
	
	boolean dragging;
	Point lastMousePos;
	
	double scale;
	Dimension originalDimensions;
	Point originalLocation;
	
	PinnedImages pinnedImagesList;
	
    Clipboard clipboard;
	
	public ImagePin(BufferedImage capture, Point position, PinnedImages pinnedImagesList)
	{
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		dragging = false;
		scale = 1.0;
		captureImage = capture;
		this.pinnedImagesList = pinnedImagesList;
				
		frame = new JFrame("Pinned Image");
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(capture.getWidth(), capture.getHeight());
		frame.setLocation(position);
		originalDimensions = frame.getSize();
		originalLocation = position;
		
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(0, 0, 0)));
		
		mainPane = new JLayeredPane();
		mainPane.setLayout(null);
		mainPane.setSize(frame.getSize());
		
		this.capture = new JLabel(new ImageIcon(capture));
		this.capture.setSize(frame.getSize());

		optionsPanel = new JPanel();
		optionsPanel.setBackground(new Color(0, 0, 0, 0.255f));
		optionsPanel.setOpaque(true);
		optionsPanel.setSize(frame.getSize());
		optionsPanel.setVisible(false);
		optionsPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));

		ImageIcon copyIcon = new ImageIcon(getClass().getClassLoader().getResource("copyIcon.png"));
		ImageIcon resetIcon = new ImageIcon(getClass().getClassLoader().getResource("resetIcon.png"));
		
		JButton saveToClipboardButton = new JButton(copyIcon);

		saveToClipboardButton.setBackground(null);
		saveToClipboardButton.setBorder(null);
		saveToClipboardButton.setRolloverEnabled(false);
		saveToClipboardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		optionsPanel.add(saveToClipboardButton);
		
		JButton resetButton = new JButton(resetIcon);
		resetButton.setBackground(null);
		resetButton.setBorder(null);
		resetButton.setRolloverEnabled(false);
		resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		optionsPanel.add(resetButton);

		saveToClipboardButton.setActionCommand("copyToClipboard");
		saveToClipboardButton.addActionListener(this);
		
		resetButton.setActionCommand("resetImage");
		resetButton.addActionListener(this);
		
		mainPane.add(this.capture, 1);
		mainPane.add(optionsPanel, 0);
		frame.add(mainPane);
		
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseWheelListener(this);
		
//		frame.setType(Window.Type.UTILITY);
		frame.setType(Type.UTILITY);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}

	public void updateImageSize()
	{
		frame.setSize((int)(originalDimensions.width * scale), (int)(originalDimensions.height * scale));
		
		Image scaledImage = captureImage.getScaledInstance((int)frame.getSize().getWidth(), (int)frame.getSize().getHeight(), Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);
		
		capture.setIcon(scaledIcon);
		
		mainPane.setSize(frame.getSize());
		capture.setSize(frame.getSize());
		optionsPanel.setSize(frame.getSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		String command = event.getActionCommand();
		switch (command)
		{
			case "resetImage":
				scale = 1;
				updateImageSize();
				frame.setLocation(originalLocation);
				return;
			
			case "copyToClipboard":
				updateImageSize();

				TransferableImage transferable = new TransferableImage(captureImage);
				clipboard.setContents(transferable, null);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		Point currentMousePos = e.getLocationOnScreen();
		int movedX = (int)(currentMousePos.getX() - lastMousePos.getX());
		int movedY = (int)(currentMousePos.getY() - lastMousePos.getY());
		
		frame.setLocation(frame.getX() + movedX, frame.getY() + movedY);
		lastMousePos = currentMousePos;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) 
		{
			pinnedImagesList.removedImage(this);
			frame.dispose();
			
//			frame = null;
//			captureImage = null;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		dragging = true;
		lastMousePos = e.getLocationOnScreen();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		optionsPanel.setVisible(true);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		Rectangle checkFrame = new Rectangle(frame.getLocationOnScreen(), frame.getSize());
		if (!checkFrame.contains(e.getLocationOnScreen()))
		{
			optionsPanel.setVisible(false);		
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) 
	{		
		scale = scale - e.getPreciseWheelRotation() * 0.1f;
		
		if (scale > 2)
		{
			scale = 2;
		}
		else if (scale < 0.2)
		{
			scale = 0.2;
		}
		
		updateImageSize();

	}
	
}
