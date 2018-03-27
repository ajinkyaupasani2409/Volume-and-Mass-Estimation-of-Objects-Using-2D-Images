package gui;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HeightCheck {

	public static int returnObjectHeight(File f) throws IOException {
		BufferedImage image = ImageIO.read(f);

		int maxHeight = 0;
		int image_width = image.getWidth(), image_ht = image.getHeight();
		for (int i = 0; i < image_width; i++) {
			int lower = -1, upper = -1;
			int j = 0, k = image_ht - 1;
			for (j = 0, k = image_ht - 1; j < image_ht; j++, k--) {

				if (lower == -1) {
					int pixel = image.getRGB(i, j);
					int g1 = (pixel >> 8) & 0xff;
					if (g1 > 100) {
						lower = j;
					}
				}
				if (upper == -1) {
					int pixel = image.getRGB(i, k);
					int g2 = (pixel >> 8) & 0xff;
					if (g2 > 100) {
						upper = k;
					}
				}

				if (upper != -1 && lower != -1) {
					if (maxHeight < (upper - lower)) {
						maxHeight = (upper - lower);

					}
					break;
				}

			}
			// System.out.println(i+"\t"+k+"\t"+j);

			// System.out.println(maxHeight);
		}
		// System.out.println("max height is "+ maxHeight);
		return maxHeight;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		returnObjectHeight(new File("ba.jpg"));
	}
}
