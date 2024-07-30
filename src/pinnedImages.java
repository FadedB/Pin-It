import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author faded
 * @aim Object that keeps track of active pinned images.
 */

public class pinnedImages 
{
	ArrayList<imagePin> pinnedImages ;
	
	public pinnedImages()
	{
		pinnedImages = new ArrayList<imagePin>();
	}
	
	public void createImage(BufferedImage image, Point imagePosition)
	{
		imagePin newImagePin = new imagePin(image, imagePosition, this);
		pinnedImages.add(newImagePin);
	}
	
	public void removedImage(imagePin image)
	{
		pinnedImages.remove(image);
	}
	
	public void showAllImages()
	{
		for (imagePin imagePin : pinnedImages) {
			imagePin.frame.setVisible(true);
		}
	}
	
	public void hideAllImages()
	{
		for (imagePin imagePin : pinnedImages) {
			imagePin.frame.setVisible(false);
		}
	}
	
	public void deleteAll()
	{
		for (int i = 0; i < pinnedImages.size(); i++) {
			pinnedImages.get(i).frame.dispose();
		}
		pinnedImages.clear();
	}
}
