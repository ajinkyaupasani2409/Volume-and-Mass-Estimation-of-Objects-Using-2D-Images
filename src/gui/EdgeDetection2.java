package gui;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

public class EdgeDetection2 {

	public static void writeLengthOfFile(File f) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat im = Imgcodecs.imread(f.getName()), edges = new Mat(), im2 = new Mat();

		Photo.fastNlMeansDenoisingColored(im, im, 10, 10, 7, 21);
		Imgproc.GaussianBlur(im, im2, new Size(0, 0), 10);
		Core.addWeighted(im, 1.5, im2, 0.5, 0.2, im);

		Imgcodecs.imwrite("DeNoisedImage1.jpg", im);
		Imgproc.Canny(im, edges, 20, 100);

		Imgcodecs.imwrite("EdgeDetectedImage1.jpg", edges);

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("EdgeDetectedImage1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file = new File("width.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(image.getHeight()+" "+ image.getWidth());

		int image_width = image.getWidth(), image_ht = image.getHeight();
		for (int i = 0; i < image_ht; i++) {
			int lower = -1, upper = -1;
			int j = 0, k = image_width - 1;
			for (; j < image_width; j++, k--) {
				int pixel = image.getRGB(j, i);
				int g = (pixel >> 8) & 0xff;
				if (g > 150 && lower == -1) {
					lower = j;
				}
				pixel = image.getRGB(k, i);
				g = (pixel >> 8) & 0xff;
				if (g > 150 && upper == -1) {
					upper = k;
				}
				if (upper != -1 && lower != -1) {
					break;

				}
			}// inner for ends.
			try {
				fw.write(String.valueOf(upper - lower) + "\t");// +
																// String.valueOf(upper)
																// + "  " +
																// String.valueOf(lower)
																// );//+"\t"+
																// String.valueOf(i)
																// + "\t"
																// +String.valueOf(j)
																// + "\t"+
																// String.valueOf(k));
				fw.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}// outer for ends.s
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
