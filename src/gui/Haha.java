package gui;
import java.awt.LinearGradientPaint;

import java.util.LinkedList;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

public class Haha {

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
		Mat img2 = Imgcodecs.imread("ball.jpg");

		// Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
		// Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGR2GRAY);

		Size s = img.size();
		double ksize = Double.max(s.height, s.width);
		double scale = 700 / ksize;
		// Imgproc.resize(img, img, new Size(7, scale));

		// Imgproc.GaussianBlur(img, img, new Size(7, 7), 0);
		/*
		 * Mat image_blur_hsv = new Mat(); Imgproc.cvtColor(img, image_blur_hsv,
		 * Imgproc.COLOR_RGB2HSV); Imgcodecs.imwrite("blur.jpg",
		 * image_blur_hsv);
		 */
		FeatureDetector featureDetector = FeatureDetector
				.create(FeatureDetector.ORB);

		DescriptorExtractor extractor = DescriptorExtractor
				.create(DescriptorExtractor.ORB);

		DescriptorMatcher matcher = DescriptorMatcher
				.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);

		// MatOfKeyPoint keypoints1 = new MatOfKeyPoint() , keypoints2 = new
		// MatOfKeyPoint();

		featureDetector.detect(img, keypoints1);
		featureDetector.detect(img2, keypoints2);

		Features2d.drawKeypoints(img, keypoints1, test);
		Imgcodecs.imwrite("k1.jpg", test);

		Features2d.drawKeypoints(img2, keypoints2, test);
		Imgcodecs.imwrite("k2.jpg", test);

		Mat descriptors1 = new Mat(), descriptors2 = new Mat();
		extractor.compute(img, keypoints1, descriptors1);
		extractor.compute(img2, keypoints2, descriptors2);

		MatOfDMatch matches = new MatOfDMatch(), matches2 = new MatOfDMatch();
		matcher.match(descriptors1, descriptors2, matches);
		matcher.match(descriptors2, descriptors1, matches2);

		System.out.println(keypoints1.rows() + "  " + descriptors1.rows() + " "
				+ matches.rows());
		System.out.println(keypoints2.rows() + "  " + descriptors2.rows() + " "
				+ matches2.rows());

		/*
		 * DMatch matches_array[] = matches.toArray();
		 * 
		 * LinkedList<Float> distance_matches = new LinkedList<>();
		 */

		/* matches = sorted(matches, key = lambda x:x.distance); */

		// MatOfDMatch selectedMatches = filterMatches(matches,matches2);

		DMatch[] d = matches.toArray();
		for (int i = 0; i < d.length; i++)
			for (int j = i; j < d.length - 1; j++) {
				if (d[j].distance > d[j + 1].distance) {
					DMatch s1 = d[j];
					d[j] = d[j + 1];
					d[j + 1] = s1;
				}
			}
		Mat res = new Mat();
		Features2d.drawMatches(img, keypoints1, img2, keypoints2,
				new MatOfDMatch(d), res);
		Imgcodecs.imwrite("res.jpg", res);

	}

	public static MatOfDMatch filterMatches(MatOfDMatch matches,
			MatOfDMatch matches2) {
		DMatch[] m1 = null, m2 = null;

		m1 = (filter_distance(matches));
		m2 = (filter_distance(matches2));
		return filterAssymetric(m1, m2);
	}

	public static MatOfDMatch filterAssymetric(DMatch[] matches,
			DMatch[] matches2) {

		/*
		 * sel_matches = [] for match1 in matches: for match2 in matches2: if
		 * k_ftr[match1.queryIdx] == k_ftr[match2.trainIdx] and
		 * k_scene[match1.trainIdx] == k_scene[match2.queryIdx]:
		 * sel_matches.append(match1) break return sel_matches
		 */
		KeyPoint[] arrOfkeypoint1 = keypoints1.toArray();
		KeyPoint[] arrOfkeypoint2 = keypoints2.toArray();

		System.out.println("hii");

		DMatch[] selMatches = new DMatch[matches.length];
		DMatch[] arrMatches = matches;
		DMatch[] arrMatches2 = matches2;

		System.out.println("arrMatches1 length is " + arrMatches.length);
		System.out.println("arrMatches2 length is " + arrMatches2.length);

		System.out.println("arrofk1 length is " + arrOfkeypoint1.length);
		System.out.println("arrofk2 length is " + arrOfkeypoint2.length);

		if (arrOfkeypoint1 == null)
			System.out.println("arrofk1 is null");

		if (arrOfkeypoint2 == null)
			System.out.println("arrofk2 is null");

		int k = 0;
		for (int i = 0; i < arrMatches.length; i++) {
			for (int j = 0; j < arrMatches2.length; j++) {
				System.out.print("1 ");
				System.out.println(arrMatches2[j].queryIdx + " "
						+ arrMatches2[j].trainIdx);

				if (arrOfkeypoint1[arrMatches[i].queryIdx] == arrOfkeypoint1[arrMatches2[j].trainIdx]) {
					if (arrOfkeypoint2[arrMatches[i].trainIdx] == arrOfkeypoint2[arrMatches2[j].queryIdx])
						selMatches[k++] = arrMatches[i];

				}
			}
		}

		System.out.println("The number of matches are " + selMatches.length);
		return new MatOfDMatch(selMatches);
	}

	public static DMatch[] filter_distance(MatOfDMatch matches) {

		DMatch matches_array[] = matches.toArray();
		DMatch[] selectedMatches = new DMatch[matches_array.length];

		float threshold = 0;
		float ratio = 0.65f;

		for (int i = 0; i < matches_array.length; i++) {
			threshold += matches_array[i].distance;
		}

		threshold = (threshold * ratio) / matches_array.length;
		System.out.println("threshold is " + threshold);
		int i = 0;
		for (int j = 0; j < matches_array.length; j++) {
			if (matches_array[j].distance < threshold)
				selectedMatches[i++] = matches_array[j];

		}
		System.out.println("selected matches #" + selectedMatches.length
				+ " out of #" + matches_array.length);
		return selectedMatches;

	}
}
