import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

/**
 * @author faded
 * @aim Object that handles the main window. Also contains Main.
 */

public class mainInterface implements ActionListener
{
	// ATTRIBUTES //
	pinnedImages pinnedImages;
	JPanel panel;
	JFrame frame;
	
	JButton pinSomethingButton;
	JButton deleteAllButton;
	
	// CONSTRUCTOR //
	public mainInterface()
	{
		//very ugly ui stuff
		//create components
		pinnedImages = new pinnedImages();
		
		frame = new JFrame("Pin-It!");
		panel = new JPanel();
		
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 10);
		panel.setLayout(layout);

		JLabel title = new JLabel("Pin-It!");
		
		title.setForeground(new Color(104, 2, 212));
		title.setFont(new Font("Century Gothic", Font.BOLD, 60));
		title.setHorizontalTextPosition(JLabel.CENTER);
		
		JLabel footer = new JLabel("Made with hatred by @FadedBFox.");
		footer.setFont(new Font("Century Gothic", Font.BOLD, 15));
		footer.setBackground(new Color(228, 227, 255));
		footer.setHorizontalAlignment(SwingConstants.CENTER);
		footer.setOpaque(true);
		footer.setPreferredSize(new Dimension(390, 50));
		footer.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		JLabel versionNum = new JLabel("Version 0.3	");
		versionNum.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		versionNum.setBackground(new Color(199, 196, 255));
		versionNum.setHorizontalAlignment(SwingConstants.CENTER);
		versionNum.setOpaque(true);
		versionNum.setPreferredSize(new Dimension(390, 20));
		versionNum.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		pinSomethingButton = new JButton("Pin Something");
		pinSomethingButton.setBackground(new Color(136, 55, 222));
		pinSomethingButton.setForeground(new Color(255,255,255));
		pinSomethingButton.setFont(new Font("Century Gothic", Font.BOLD, 30));
		pinSomethingButton.setBorder(BorderFactory.createLineBorder(new Color(136, 55, 222), 10, true));

		deleteAllButton = new JButton("Remove All Pins");
		deleteAllButton.setBackground(new Color(255, 84, 127));
		deleteAllButton.setForeground(new Color(255,255,255));
		deleteAllButton.setFont(new Font("Century Gothic", Font.BOLD, 20));
		deleteAllButton.setBorder(BorderFactory.createLineBorder(new Color(255, 84, 127), 10, true));
		
		//add components to frame
		panel.add(title);
		panel.add(pinSomethingButton);
		panel.add(deleteAllButton);
		panel.add(footer);
		panel.add(versionNum);
		frame.add(panel);
		
		//add listeners to buttons
		pinSomethingButton.setActionCommand("pinSomething");
		pinSomethingButton.addActionListener(this);
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
			PopupMenu trayMenu = new PopupMenu();
			
			MenuItem openMainWindowButton = new MenuItem("Open Main Window");
			MenuItem quitButton = new MenuItem("Quit");
			MenuItem pinSomethingTrayButton = new MenuItem("Pin Something");
			MenuItem deleteAllPinsTrayButton = new MenuItem("Remove All pins");

			//tray menu listeners
			openMainWindowButton.setActionCommand("openMainWindow");
			openMainWindowButton.addActionListener(this);
			quitButton.setActionCommand("quitProgram");
			quitButton.addActionListener(this);
			pinSomethingTrayButton.setActionCommand("pinSomething");
			pinSomethingTrayButton.addActionListener(this);
			deleteAllPinsTrayButton.setActionCommand("deleteAllPins");
			deleteAllPinsTrayButton.addActionListener(this);
			
			//add items to tray menu
			trayMenu.add(pinSomethingTrayButton);
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
		frame.setSize(new Dimension(400, 335));
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
			Thread.sleep(200);
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
				new screenSelection(this); //start screen selection
				return;
			case "deleteAllPins":
				deleteAllImages();
				return;
		}
	}
	
	// MAIN //
	public static void main(String[] args) {
		 new mainInterface();
	}
}
