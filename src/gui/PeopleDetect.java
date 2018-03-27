package gui;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

/**
 * Histogram of Oriented Gradients ([Dalal2005]) object detector.
 * 
 * args[0] = source file or will default to "../resources/walking.mp4" if no
 * args passed.
 * 
 * @author sgoldsmith
 * @version 1.0.0
 * @since 1.0.0
 */
// This is demo code, not worried about magic numbers, etc.
@SuppressWarnings({ "checkstyle:magicnumber", "PMD.LawOfDemeter",
		"PMD.AvoidLiteralsInIfCondition",
		"PMD.AvoidInstantiatingObjectsInLoops", "PMD.AvoidUsingNativeCode",
		"PMD.AvoidFinalLocalVariable", "PMD.CommentSize",
		"PMD.AvoidPrintStackTrace", "PMD.UseProperClassLoader",
		"PMD.AvoidPrefixingMethodParameters", "PMD.DataflowAnomalyAnalysis" })
final class PeopleDetect {
	/**
	 * Logger.
	 */
	// Logger is not a constant
	@SuppressWarnings({ "checkstyle:constantname",
			"PMD.VariableNamingConventions" })
	private static final Logger logger = Logger.getLogger(PeopleDetect.class
			.getName());
	/* Load the OpenCV system library */
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * Suppress default constructor for noninstantiability.
	 */
	private PeopleDetect() {
		throw new AssertionError();
	}

	/**
	 * Create window, frame and set window to visible.
	 * 
	 * args[0] = source file or will default to "../resources/walking.mp4" if no
	 * args passed.
	 * 
	 * @param args
	 *            String array of arguments.
	 */
	public static void main(final String... args) {
		String url = null;
		final String outputFile = "../output/people-detect-java.avi";
		// Check how many arguments were passed in
		if (args.length == 0) {
			// If no arguments were passed then default to local file
			url = "../resources/walking.mp4";
		} else {
			url = args[0];
		}
		// Custom logging properties via class loader

		/*
		 * logger.log(Level.INFO, String.format("OpenCV %s", Core.VERSION));
		 * logger.log(Level.INFO, String.format("Input file: %s", url));
		 * logger.log(Level.INFO, String.format("Output file: %s", outputFile));
		 * final VideoCapture videoCapture = new VideoCapture(url); final Size
		 * frameSize = new Size((int)
		 * videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH), (int)
		 * videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
		 * logger.log(Level.INFO, String.format("Resolution: %s", frameSize));
		 * // final FourCC fourCC = new FourCC("X264");
		 *//*
			 * final VideoWriter videoWriter = new VideoWriter(outputFile,
			 * fourCC.toInt(), videoCapture.get(Videoio.CAP_PROP_FPS),
			 * frameSize, true);
			 */

		final Mat mat = Imgcodecs.imread("test.jpg");
		// final HOGDescriptor hog = new HOGDescriptor(new Size(128, 64),
		// new Size(16, 16), new Size(8, 8), new Size(8, 8), 9, 0, -1, 0,
		// 0.2, false, 64);
		final HOGDescriptor hog = new HOGDescriptor();
		final MatOfFloat descriptors = HOGDescriptor.getDefaultPeopleDetector();
		hog.setSVMDetector(descriptors);
		final MatOfRect foundLocations = new MatOfRect();
		final MatOfDouble foundWeights = new MatOfDouble();
		final Size winStride = new Size(8, 8);
		final Size padding = new Size(32, 32);
		final Point rectPoint1 = new Point();
		final Point rectPoint2 = new Point();
		final Point fontPoint = new Point();
		int frames = 0;
		int framesWithPeople = 0;
		final Scalar rectColor = new Scalar(0, 255, 0);
		final Scalar fontColor = new Scalar(255, 255, 255);
		final long startTime = System.currentTimeMillis();

		hog.detectMultiScale(mat, foundLocations, foundWeights, 0.0, winStride,
				padding, 1.05, 2.0, false);
		if (foundLocations.rows() > 0) {
			framesWithPeople++;
			final List<Double> weightList = foundWeights.toList();
			final List<Rect> rectList = foundLocations.toList();
			int index = 0;
			for (final Rect rect : rectList) {
				rectPoint1.x = rect.x;
				rectPoint1.y = rect.y;
				rectPoint2.x = rect.x + rect.width;
				rectPoint2.y = rect.y + rect.height;
				// Draw rectangle around fond object
				Imgproc.rectangle(mat, rectPoint1, rectPoint2, rectColor, 2);
				fontPoint.x = rect.x;
				// illustration
				fontPoint.y = rect.y - 4;
				// Print weight
				// illustration
				Imgproc.putText(mat,
						String.format("%1.2f", weightList.get(index)),
						fontPoint, Core.FONT_HERSHEY_PLAIN, 1.5, fontColor, 2,
						Core.LINE_AA, false);
				index++;
			}
		}

		final long estimatedTime = System.currentTimeMillis() - startTime;
		final double seconds = (double) estimatedTime / 1000;
		logger.log(Level.INFO, String.format(
				"%d frames, %d frames with people", frames,
				foundLocations.rows()));
		logger.log(
				Level.INFO,
				String.format("%4.1f FPS, elapsed time: %4.2f seconds", frames
						/ seconds, seconds));
		// Release native memory
		/*
		 * videoCapture.free(); videoWriter.free(); hog.free();
		 * descriptors.free(); foundLocations.free(); foundWeights.free();
		 * mat.free();
		 */
	}
}