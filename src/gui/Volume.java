package gui;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.opencv.features2d.FeatureDetector;

public class Volume {

	public static double calcVolume(int myScale1, int myScale2)
			throws IOException {

		File file = new File("length.txt");
		File file2 = new File("width.txt");
		File file3 = new File("vol.txt");

		Scanner sc = new Scanner(file);
		Scanner sc2 = new Scanner(file2);

		FileWriter fw = new FileWriter(file3);

		double volume = 0;

		while (sc.hasNextInt() && sc2.hasNextInt()) {
			double len = sc.nextInt();
			double width = sc2.nextInt();
			len = len * 15 / myScale1;
			width = width * 15 / myScale2;
			volume += (len * width) / 4;
			fw.write(String.valueOf(volume) + "\t" + String.valueOf(len / 2)
					+ "\t" + String.valueOf(width / 2));
			fw.write("\n");

		}
		volume = volume * Math.PI;
		System.out.println(volume);
		sc.close();
		sc2.close();
		fw.close();
		return volume;
	}

	public static void main(String[] args) {

	}
}
