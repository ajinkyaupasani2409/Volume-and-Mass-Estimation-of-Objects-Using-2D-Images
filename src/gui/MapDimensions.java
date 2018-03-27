package gui;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class MapDimensions {

	public static void Mapper(File f1,File f2)	 throws IOException
	{
		File f_l = new File("newLen.txt");
		FileWriter fw1 = new FileWriter(f_l);
		Scanner sc1 = new Scanner(f1) ,sc2 = new Scanner(f2);
		while(sc1.hasNext())
		{
			int val1 = sc1.nextInt();
			if(val1 != 0)
			{
				fw1.write(""+String.valueOf(val1));
				fw1.write("\n");
			}
		}
		f_l = new File("newWid.txt");
		fw1.close();
		fw1 = new FileWriter(f_l);
		
		while(sc2.hasNext())
		{
			int val1 = sc2.nextInt();
			if(val1 != 0)
			{
				fw1.write(""+String.valueOf(val1));
				fw1.write("\n");
			}
		}
		sc1.close();
		sc2.close();
		fw1.close();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
