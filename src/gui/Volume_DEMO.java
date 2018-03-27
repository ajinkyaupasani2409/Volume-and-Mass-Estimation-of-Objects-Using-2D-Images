package gui;
import java.io.File;
import java.util.Date;

public class Volume_DEMO {

	public static double getVolume(File file1, File file2) throws Exception {
		
		@SuppressWarnings("deprecation")
		int sT = new Date().getSeconds();
		System.out.println("Start Time is " + sT);
		File f1 = file1;
		File f2 = file2;

		int myScale1 = CalcHeight.getHeightOfScale(f1);
		int myScale2 = CalcHeight.getHeightOfScale(f2);

		System.out.println(myScale1);
		System.out.println(myScale2);
		EdgeDetection1.writeWidthOfFile(f1);
		EdgeDetection2.writeLengthOfFile(f2);

		MapDimensions.Mapper(new File("length.txt"), new File("width.txt"));
		
		int objHeight1 = HeightCheck.returnObjectHeight(new File("EdgeDetectedImage2.jpg"));
		
		int objHeight2 = HeightCheck.returnObjectHeight(new File("EdgeDetectedImage1.jpg"));

		System.out.println("object ht1 is " + objHeight1);
		// System.out.println("object ht2 is "+ objHeight2*15/myScale2);

		// System.out.println(objHeight1 + " " + objHeight2);
		int t = (objHeight1);// + objHeight2)/2 ,
		int t1 = (myScale1);

		System.out.println(t + " " + t1);

		float finalHeight = (float) t / t1;

		System.out.println(finalHeight);

		double vol = Volume.calcVolume(myScale1, myScale1) * finalHeight * 15 / ((objHeight1));// + objHeight2) /2 );
		System.out.println(vol + "cc");
		double density = 0.84762;

		System.out.println("Mass is " + vol * density);

		@SuppressWarnings("deprecation")
		int eT = new Date().getSeconds();
		if(eT < sT)
			eT = eT + 60;
		System.out.println("End Time is " + eT);
		System.out.println("Total execution time is " + (eT- sT) );
		return vol;
	}

	public static void main(String[] args) throws Exception {

		System.out.println(getVolume(new File("po1.jpg"), new File("po2.jpg")));
		// TODO Auto-generated method stub
	}

}
