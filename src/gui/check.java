package gui;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class check {
	public static MatOfKeyPoint keypoints1, keypoints2;
	public static Mat descriptors1, descriptors2;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		keypoints1 = new MatOfKeyPoint();
		keypoints2 = new MatOfKeyPoint();

		descriptors1 = new Mat();
		descriptors2 = new Mat();
		Mat test = new Mat();

		Mat img = Imgcodecs.imread("foto2.jpg");
		Mat img2 = Imgcodecs.imread("pear.jpg");

		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2HSV);
		Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGR2HSV);

		Mat mask1 = new Mat();
		Mat mask2 = new Mat();
		Mat mask = new Mat();

		Core.inRange(img, new Scalar(10, 256, 80), new Scalar(20, 256, 80),
				mask1);
		Core.inRange(img, new Scalar(170, 256, 80), new Scalar(180, 256, 80),
				mask2);

		Core.add(mask1, mask2, mask);

		FeatureDetector featureDetector = FeatureDetector
				.create(FeatureDetector.ORB);

		DescriptorExtractor extractor = DescriptorExtractor
				.create(DescriptorExtractor.ORB);

		DescriptorMatcher matcher = DescriptorMatcher
				.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);

		// MatOfKeyPoint keypoints1 = new MatOfKeyPoint() , keypoints2 = new
		// MatOfKeyPoint();

		featureDetector.detect(img, keypoints1, mask);
		featureDetector.detect(img2, keypoints2, mask);
		// featureDetector.detect(img, keypoints1);
		/*
		 * featureDetector.detect(img2, keypoints2);
		 */
		// Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);
		Features2d.drawKeypoints(img, keypoints1, test);
		Imgcodecs.imwrite("k1.jpg", test);

		Features2d.drawKeypoints(img2, keypoints2, test);
		Imgcodecs.imwrite("k2.jpg", test);

		// Imgcodecs.imwrite("result.jpg" ,test);

		KeyPoint[] kp1 = keypoints1.toArray();
	}
}
