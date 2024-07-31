import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author faded
 * @aim Object that keeps track of active pinned images.
 */

public class PinnedImages 
{
	ArrayList<ImagePin> pinnedImages ;
	
	public PinnedImages()
	{
		pinnedImages = new ArrayList<ImagePin>();
	}
	
	public void createImage(BufferedImage image, Point imagePosition)
	{
		ImagePin newImagePin = new ImagePin(image, imagePosition, this);
		pinnedImages.add(newImagePin);
	}
	
	public void removedImage(ImagePin image)
	{
		pinnedImages.remove(image);
	}
	
	public void showAllImages()
	{
		for (ImagePin imagePin : pinnedImages) {
			imagePin.frame.setVisible(true);
		}
	}
	
	public void hideAllImages()
	{
		for (ImagePin imagePin : pinnedImages) {
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
