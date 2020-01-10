package smartgrid.model.topo.generator.tester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class that runs the Tests and writes the outputs to a File
 * @author Eric Klappert
 *
 */
public class TestLogger {
	private static final String savePath = "L:/";
	
	/**
	 * Run Tests and write results to a File at savePath
	 */
	public static void startTests() {
		BufferedWriter writer;
		File file = new File(savePath + "testlog.txt");
		try {
			writer = new BufferedWriter(new FileWriter(file));
			//TestTopoGenerator.checkScalibility(writer);
			MicroGridTester.compareBruteforce(writer);
			/*
			int unsaturated = 0;
			for (int i = 0; i < 20; i++) {
				unsaturated += MicroGridTester.averageQuality(writer, i * 100);
			}
			unsaturated /= 20;
			writer.write("avr unsaturated" + unsaturated);
			*/
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
