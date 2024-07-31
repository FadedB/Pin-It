import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

/**
 * @author faded
 * @aim Object that handles the main window. Also contains Main.
 */

public class MainInterface implements ActionListener
{
	// ATTRIBUTES //
	PinnedImages pinnedImages;
	JPanel panel;
	JFrame frame;
	
	PopupMenu trayMenu;
	
    Clipboard clipboard;

	// CONSTRUCTOR //
	public MainInterface()
	{
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		//very ugly ui stuff
		//create ui components
		pinnedImages = new PinnedImages();
		
		frame = new JFrame("Pin-It!");
		panel = new JPanel();
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbConstraints = new GridBagConstraints();
		
		gbConstraints.weightx = 1;
		gbConstraints.weighty = 0.2;
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 0;
		gbConstraints.insets = new Insets(10, 0, 10, 0);
		gbConstraints.fill = GridBagConstraints.VERTICAL;
//		FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
		panel.setLayout(layout);

		JLabel title = new JLabel("Pin-It!");
		title.setForeground(new Color(104, 2, 212));
		title.setFont(new Font("Century Gothic", Font.BOLD, 60));
		title.setHorizontalTextPosition(JLabel.CENTER);
		panel.add(title, gbConstraints);

		JButton pinSomethingButton = new JButton("From Screen");
		pinSomethingButton.setBackground(new Color(136, 55, 222));
		pinSomethingButton.setForeground(new Color(255,255,255));
		pinSomethingButton.setFont(new Font("Century Gothic", Font.BOLD, 30));
		pinSomethingButton.setBorder(BorderFactory.createLineBorder(new Color(136, 55, 222), 10));
		pinSomethingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		gbConstraints.gridy = 1;
		gbConstraints.weighty = 0.1;
		gbConstraints.ipadx = 5;

		gbConstraints.insets = new Insets(5, 0, 5, 0);
		panel.add(pinSomethingButton, gbConstraints);

		JButton pinSomethingClipboardButton = new JButton("From Clipboard");
		pinSomethingClipboardButton.setBackground(new Color(136, 55, 222));
		pinSomethingClipboardButton.setForeground(new Color(255,255,255));
		pinSomethingClipboardButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
		pinSomethingClipboardButton.setBorder(BorderFactory.createLineBorder(new Color(136, 55, 222), 10, false));
		pinSomethingClipboardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		gbConstraints.gridy = 2;
		panel.add(pinSomethingClipboardButton, gbConstraints);

		JButton deleteAllButton = new JButton("Remove All Pins");
		deleteAllButton.setBackground(new Color(255, 84, 127));
		deleteAllButton.setForeground(new Color(255,255,255));
		deleteAllButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
		deleteAllButton.setBorder(BorderFactory.createLineBorder(new Color(255, 84, 127), 10, true));
		deleteAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		gbConstraints.gridy = 3;
		gbConstraints.insets = new Insets(15, 0, 5, 0);

		panel.add(deleteAllButton, gbConstraints);

		JLabel footer = new JLabel("Made with hatred by FadedB");
		footer.setFont(new Font("Century Gothic", Font.ITALIC, 12));
		footer.setBackground(new Color(228, 227, 255));
		footer.setHorizontalAlignment(SwingConstants.CENTER);
		footer.setOpaque(true);
		footer.setPreferredSize(new Dimension(390, 30));
		footer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		gbConstraints.gridy = 4;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.insets = new Insets(15, 0, 0, 0);
		gbConstraints.weighty = 0;

		panel.add(footer, gbConstraints);

		JLabel versionNum = new JLabel("Version 0.4");
		versionNum.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		versionNum.setBackground(new Color(199, 196, 255));
		versionNum.setHorizontalAlignment(SwingConstants.CENTER);
		versionNum.setOpaque(true);
		versionNum.setPreferredSize(new Dimension(390, 20));
		versionNum.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		gbConstraints.gridy = 5;
		gbConstraints.insets = new Insets(0,0,0,0);
		panel.add(versionNum, gbConstraints);

		//add components to frame
		frame.add(panel);
		
		//add listeners to buttons
		pinSomethingButton.setActionCommand("pinSomething");
		pinSomethingButton.addActionListener(this);
		pinSomethingClipboardButton.setActionCommand("pinSomethingClipboard");
		pinSomethingClipboardButton.addActionListener(this);
		deleteAllButton.setActionCommand("deleteAllPins");
		deleteAllButton.addActionListener(this);
		
		//fetch program icon
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("pinItPin.png"));
		
		frame.setIconImage(icon.getImage()); //set frame icon
		
		//system tray stuff
		if (SystemTray.isSupported() == true) //check if supported
		{
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			SystemTray sysTray = SystemTray.getSystemTray();
			
			//create tray items
			TrayIcon trayIcon = new TrayIcon(icon.getImage()); //set tray icon
			trayIcon.setImageAutoSize(true);
			trayMenu = new PopupMenu();
			PopupMenu pinSomethingMenu = new PopupMenu("Pin-It!");

			MenuItem openMainWindowButton = new MenuItem("Open Main Window");
			MenuItem quitButton = new MenuItem("Quit");
			MenuItem pinSomethingTrayButton = new MenuItem("From Screen");
			MenuItem pinClipboardTrayButton = new MenuItem("From Clipboard");
			MenuItem deleteAllPinsTrayButton = new MenuItem("Remove All pins");

			//tray menu listeners
			openMainWindowButton.setActionCommand("openMainWindow");
			openMainWindowButton.addActionListener(this);
			quitButton.setActionCommand("quitProgram");
			quitButton.addActionListener(this);
			pinSomethingTrayButton.setActionCommand("pinSomething");
			pinSomethingTrayButton.addActionListener(this);
			pinClipboardTrayButton.setActionCommand("pinSomethingClipboard");
			pinClipboardTrayButton.addActionListener(this);
			deleteAllPinsTrayButton.setActionCommand("deleteAllPins");
			deleteAllPinsTrayButton.addActionListener(this);
			
			//add items to tray menu
			pinSomethingMenu.add(pinSomethingTrayButton);
			pinSomethingMenu.add(pinClipboardTrayButton);

			trayMenu.add(pinSomethingMenu);
			trayMenu.add(deleteAllPinsTrayButton);
			trayMenu.addSeparator();
			trayMenu.add(openMainWindowButton);
			trayMenu.add(quitButton);
			trayIcon.setPopupMenu(trayMenu);
			
			try {
				sysTray.add(trayIcon);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else //if system tray isn't available, exit program on close
		{
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		frame.setResizable(false);
		frame.setSize(new Dimension(400, 400));
		frame.setVisible(true);
	}
	
	// METHODS //
	// called when a new pinned image is to be created (after screen selection).
	public void newCapture(BufferedImage capture, Point imagePosition)
	{
		pinnedImages.createImage(capture, imagePosition);
	}
	
	// removes all pinned images
	public void deleteAllImages()
	{
		pinnedImages.deleteAll();
	}
	
	// makes all pinned images and main window visible 
	public void showImages()
	{
		pinnedImages.showAllImages();
		frame.setVisible(true);
	}
	
	// hides all pinned images and main window. used for when selection is taking place
	public void hideImages()
	{
		pinnedImages.hideAllImages();
		frame.setVisible(false);

		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// EVENTS //
	
	@Override
	// button clicks
	public void actionPerformed(ActionEvent event)
	{
		String command = event.getActionCommand();
		switch (command)
		{
			case "quitProgram":
				System.exit(0);
				return;
				
			case "openMainWindow":
				frame.setVisible(true);
				return;
				
			case "pinSomething":
				hideImages();
				new ScreenSelection(this); //start screen selection
				return;
				
			case "pinSomethingClipboard":
				Transferable content = clipboard.getContents(null);
			    
				boolean hasImage = (content != null) & content.isDataFlavorSupported(DataFlavor.imageFlavor);
				
				if (hasImage)
				{
					try {
						Image image = (Image) content.getTransferData(DataFlavor.imageFlavor);
						newCapture((BufferedImage) image, new Point());
					} catch (UnsupportedFlavorException | IOException e) {
						e.printStackTrace();
					}
				}
				return;
				
			case "deleteAllPins":
				deleteAllImages();
				return;
		}
	}
	
	// MAIN //
	public static void main(String[] args) {
		 new MainInterface();
	}
}
