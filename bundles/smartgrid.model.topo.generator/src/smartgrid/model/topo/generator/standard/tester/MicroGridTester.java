package smartgrid.model.topo.generator.standard.tester;

import java.io.BufferedWriter;
import java.io.IOException;

import smartgrid.model.topo.generator.standard.IMicroGridsGenerator;
import smartgrid.model.topo.generator.standard.MicroGridsGenerator;
import smartgrid.model.topo.generator.standard.MicroGridsGeneratorBruteforce;
import smartgrid.model.topo.generator.standard.TopologyGenerator;
import smartgrid.model.topo.generator.standard.data.MicroGrid;
import smartgrid.model.topo.generator.standard.data.SmartMeterAddonData;

/**
 * A Class to evaluate the results of the MicroGridGenerator
 * @author Eric Klappert
 *
 */
public class MicroGridTester {
	
	private static final int compareBruteforceGridSize = 12;
	
	/**
	 * Checks the number of MicroGrids that have less power than they require
	 * @param generator TopologyGenerator containing the microgrids to check
	 * @return number of microgrids not saturated with power
	 */
	public static int checkUnsaturatedMicroGrids(TopologyGenerator generator) {
		int negativeGridCount = 0;
		for (MicroGrid grid : generator.getMicrogrids()) {
			if (grid.getPowerBalance() < 0)
				negativeGridCount++;
		}
		return negativeGridCount;
	}
	
	private static void testGrid(BufferedWriter writer, IMicroGridsGenerator generator, TopologyContainerAddon topo, String Name) {
		TopologyGenerator gen = new TopologyGenerator();
		gen.setMicroGridsGenerator(generator);
		long startTime = System.currentTimeMillis();
		gen.setSmartMeterAddonData(topo.getAddonData());
		gen.generateTopo(topo.getTopologyContainer());
		long endTime = System.currentTimeMillis();
		int result = checkUnsaturatedMicroGrids(gen);
		int numberOfGrids = gen.getMicrogrids().size();
		int largestGrid = 0;
		for (MicroGrid grid : gen.getMicrogrids()) {
			int gridSize = grid.getPGNs().size();
			if (gridSize > largestGrid)
				largestGrid = gridSize;
		}
		try {
			writer.write("Results of "+ Name + " Algorithm:");
			writer.newLine();
			writer.write("Number of Unsaturated Grids: " + result);
			writer.newLine();
			writer.write("Number of Grids: " + numberOfGrids);
			writer.newLine();
			writer.write("Size of largest Grid: " + largestGrid);
			writer.newLine();
			writer.write("Time for Generation: " + (endTime - startTime));
			writer.newLine();
			for (MicroGrid grid : gen.getMicrogrids()) {
				writer.write(grid.getPowerBalance() + "   ");
			}
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int averageQuality(BufferedWriter writer, int seed) {
		TopologyGenerator gen = new TopologyGenerator();
		MicroGridsGenerator generator = new MicroGridsGenerator();
		generator.setMaxSize(75);
		generator.setMinSize(5);
		gen.setMicroGridsGenerator(generator);
		TopologyContainerAddon topo = TestTopoGenerator.createRandomTopology(150, seed, 2);
		gen.setSmartMeterAddonData(topo.getAddonData());
		double totalPower = 0;
		for (SmartMeterAddonData data : topo.getAddonData().values()) {
			totalPower += data.getPower();
		}
		gen.generateTopo(topo.getTopologyContainer());
		
		int result =  generator.getMicroGridsValue(gen.getMicrogrids());
		int numberofGrids = gen.getMicrogrids().size();
		
		int unsaturated = 0;
		
		double totalPowerout = 0;
		for (MicroGrid grid : gen.getMicrogrids()) {
			totalPowerout += grid.getPowerBalance();
			if (grid.getPowerBalance() < 0)
				unsaturated++;
		}
		try {
			writer.write("Total Power of Input = " + totalPower);
			writer.newLine();
			writer.write("Total Power of Output = " + totalPowerout);
			writer.newLine();
			writer.write("Number of Grids:" + numberofGrids);
			writer.newLine();
			writer.write("Results of Algorithm:" + result);
			writer.newLine();
			for (MicroGrid grid : gen.getMicrogrids()) {
				writer.write(generator.getMicroGridValue(grid) + "   ");
			}
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Compare the results of the Bruteforce Generator with the Greedy Algorithm
	 * @param writer used to write to logging file
	 */
	public static void compareBruteforce(BufferedWriter writer) {
		TopologyContainerAddon topo = TestTopoGenerator.createRandomTopology(compareBruteforceGridSize, 218, 2);
		MicroGridsGenerator generator = new MicroGridsGenerator();
		testGrid(writer, generator, topo, "Greedy");
		MicroGridsGeneratorBruteforce generator2 = new MicroGridsGeneratorBruteforce();
		testGrid(writer, generator2, topo, "Bruteforce");
	}
}
