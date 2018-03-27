package gui;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FeatureDetection {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = Imgcodecs.imread("foto2.jpg"), n = Imgcodecs.imread("ball.jpg"), result = new Mat();
		MatOfKeyPoint keypoints1 = new MatOfKeyPoint(), keypoints2 = new MatOfKeyPoint();

		// Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY,4);
		Mat hist = new Mat();
		MatOfInt histSize = new MatOfInt(), channels = new MatOfInt();
		MatOfFloat ranges = new MatOfFloat();

		Mat desc1 = new Mat(), desc2 = new Mat();
		// FeatureDetector fd = FeatureDetector.create(FeatureDetector.HARRIS);
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.ORB);
		DescriptorExtractor descExtractor = DescriptorExtractor
				.create(DescriptorExtractor.ORB);

		fd.detect(m, keypoints1);
		descExtractor.compute(m, keypoints1, desc1);

		fd.detect(n, keypoints2);
		descExtractor.compute(n, keypoints2, desc2);

		List<MatOfDMatch> matches1to2 = null;

		DescriptorMatcher dMatch = DescriptorMatcher
				.create(DescriptorMatcher.BRUTEFORCE);

		dMatch.knnMatch(desc2, desc1, matches1to2, 5);

		Features2d.drawMatchesKnn(m, keypoints1, n, keypoints2, matches1to2,
				result);

		// List matches1to2 = null;

		/*
		 * System.out.println(keypoints1.rows());
		 * System.out.println(keypoints2.rows());
		 * 
		 * KeyPoint[] kp1 = keypoints1.toArray(); KeyPoint[] kp2 =
		 * keypoints2.toArray();
		 * 
		 * for(int i=0;i<kp1.length/50;i++) { for(int j = 0; j<
		 * kp2.length/50;j++) { int data[] = null , data2[] = null;
		 * keypoints1.get(i, j, data); keypoints1.get(i, j,data2);
		 * 
		 * System.out.print(kp1[i].octave + "  "+ kp2[j].octave);
		 * System.out.println(kp1[i].pt); System.out.print(" "+kp1[i].response +
		 * "  "+ kp2[j].response); System.out.println(" "+kp1[i].angle + "  "+
		 * kp2[j].angle);
		 * 
		 * } }
		 * 
		 * Features2d.drawKeypoints(m, keypoints1, n);
		 * Features2d.drawMatchesKnn(m, keypoints1, n, keypoints2, matches1to2,
		 * result);
		 * 
		 * //Features2d.drawMatchesKnn(m, keypoints1, n, keypoints2,
		 * matches1to2, result);
		 */Imgcodecs.imwrite("Feature.jpg", result);
	}

}
