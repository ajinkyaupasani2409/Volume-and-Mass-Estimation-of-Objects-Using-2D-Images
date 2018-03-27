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

public class EdgeDetection {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat im = Imgcodecs.imread("ball3.jpg"), im2 = new Mat(), edges = new Mat();
		// Mat im2 = Imgcodecs.imread("banana2.jpg"), edges2 = new Mat();

		// Imgproc.cvtColor(im, im, Imgproc.COLOR_BGR2GRAY);

		/*
		 * Imgproc.cvtColor(im, im, Imgproc.COLOR_BGR2GRAY);
		 * Imgproc.cvtColor(im2, im2, Imgproc.COLOR_BGR2GRAY);
		 */

		Imgproc.GaussianBlur(im, im2, new Size(0, 0), 10);

		Core.addWeighted(im, 1.5, im2, 0.6, 0.2, im);

		// Imgproc.Canny(im2, edges2, 230, 300);

		Imgcodecs.imwrite("hh1.jpg", im);
		Imgproc.Canny(im, edges, 20, 100);
		Imgcodecs.imwrite("hh.jpg", edges);

		/*
		 * Mat lines = new Mat(); //Imgproc.HoughLinesP(edges, lines,
		 * Imgproc.CV_HOUGH_PROBABILISTIC, Math.PI/180, 70);
		 * //Imgproc.HoughLines(edges, lines, 1, Math.PI/360, 100);
		 * 
		 * //Imgproc.HoughLines(edges, lines, 1, Math.PI/360, 50, 10, 20, 30,
		 * Math.PI/180); //Imgproc.HoughLines(image, lines, rho, theta,
		 * threshold, srn, stn, min_theta, max_theta);
		 * //Imgproc.HoughLines(edges, lines, 1, Math.PI/180, 50 );
		 * 
		 * 
		 * //Imgcodecs.imwrite("result3.jpg", lines);
		 */

		/*
		 * System.out.println("lines is "+lines.rows()+" "+lines.cols()); double
		 * data[] = lines.get(0, 0); System.out.println(data.length); for (int i
		 * = 0; i < lines.cols(); i++){
		 * 
		 * data = lines.get(i, 0); double rho = data[0]; double theta = data[1];
		 * double cosTheta = Math.cos(theta); double sinTheta = Math.sin(theta);
		 * double x0 = cosTheta * rho; double y0 = sinTheta * rho; Point pt1
		 * ;//= new Point(Math.round(x0 + 10000 * (-sinTheta) ) , Math.round(y0
		 * + 10000 * cosTheta)); Point pt2 ;//= new Point(Math.round(x0 - 10000
		 * * (-sinTheta)), Math.round(y0 - 10000 * cosTheta)); pt1 = new
		 * Point(data[0], data[1]); pt2 = new Point(data[2], data[3]);
		 * Imgproc.line(edges, pt1, pt2, new Scalar(255, 0, 0), 3);
		 * Imgproc.line(edges, pt1, pt2, new Scalar(255, 0, 0), 2,
		 * Imgproc.LINE_8, 0); } Imgcodecs.imwrite("result123.jpg", edges);
		 */
		// Mat circles = new Mat();
		// Imgproc.HoughCircles(edges, circles, Imgproc.CV_HOUGH_GRADIENT, 2,
		// 100 , 100, 100, 0, 500);

		// System.out.println(edges.cols() + " " + edges.rows());
		/*
		 * for (int i = 0; i < circles.cols(); i++) { double[] vCircle =
		 * circles.get(0, i);
		 * 
		 * Point pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
		 * int radius = (int)Math.round(vCircle[2]);
		 * 
		 * //Core.circle(currentFrame, pt, radius, new Scalar(255, 0, 0), 2);
		 * Imgproc.circle(edges, pt, radius, new Scalar(255, 0, 0), 3); }
		 * 
		 * 
		 * Imgcodecs.imwrite("result4.jpg", edges);
		 */
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("hh.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file = new File("file.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int arr[] = new int[image.getHeight()];
		// System.out.println(image.getHeight()+" "+ image.getWidth());

		int image_width = image.getWidth(), image_ht = image.getHeight();
		for (int i = 0; i < image_ht; i++) {
			int lower = -1, upper = -1;

			for (int j = 0, k = image_width - 1; j < image_width; j++, k--) {
				// System.out.println(k);
				if (lower == -1) {
					int pixel = image.getRGB(j, i);
					int g = (pixel >> 8) & 0xff;
					if (g > 10) {
						lower = j;
					}

				}
				if (upper == -1) {

					int pixel = image.getRGB(k, i);
					int g = (pixel >> 8) & 0xff;
					if (g > 10) {
						upper = k;
					}

				}
				if (upper != -1 && lower != -1) {
					break;
				}

			}// inner for ends.
			arr[i] = upper - lower;
			try {
				fw.write(String.valueOf(arr[i]) + "\t" + String.valueOf(upper)
						+ "  " + String.valueOf(lower));
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
}
