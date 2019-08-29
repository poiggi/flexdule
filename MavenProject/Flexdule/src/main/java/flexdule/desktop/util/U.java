package flexdule.desktop.util;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class U {

	public static void labelToImage(JLabel label, String imagePath, Class loaderClass) {
		ImageIcon im = new ImageIcon(loaderClass.getResource(imagePath));
		Icon im2 = new ImageIcon(
				im.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
		label.setIcon(im2);
		label.setText("");
	}

}
