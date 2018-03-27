package gui;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CalcHeight {
	public static int getHeightOfScale(File f) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int myScale = 0, flag = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				int pixel = image.getRGB(j, i);
				// int a=(pixel >> 24) &0xff;
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = (pixel) & 0xff;

				// r = 255 , g = [140,165] b = 0;

				if (180 <= r && 0 <= g && g <= 140 && 0 <= b && b <= 100) {
					myScale = image.getHeight() - i;
					// System.out.println(j + "  " + i);
					flag = 1;
					break;
				}
			}
			if (flag != 0) {
				break;
			}
		}
		// System.out.println("height of orange line is 15 cm and pixel length is "
		// + myScale);
		return myScale;
	}

	public static void main(String[] args) {

	}
}
