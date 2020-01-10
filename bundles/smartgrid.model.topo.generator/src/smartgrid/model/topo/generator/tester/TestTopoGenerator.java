package smartgrid.model.topo.generator.tester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import couplingToICT.SmartComponentGeoData;
import couplingToICT.SmartGridTopoContainer;
import smartgrid.model.topo.generator.MicroGridsGenerator;
import smartgrid.model.topo.generator.TopologyGenerator;
import smartgrid.model.topo.generator.data.SmartMeterAddonData;

public class TestTopoGenerator {
	
	/**
	 * A Test to check the scalability of the Algorithms Implementation
	 * @param writer  used to write to logging File
	 */
	public static void checkScalibility(BufferedWriter writer) {
		/*
		checkScalibility(writer, 200, 2);
		checkScalibility(writer, 500, 2);
		checkScalibility(writer, 1000, 2);
		checkScalibility(writer, 1500, 2);
		checkScalibility(writer, 2000, 2);
		checkScalibility(writer, 2500, 2);
		checkScalibility(writer, 3000, 2);
		checkScalibility(writer, 3500, 2);
		checkScalibility(writer, 4000, 2);
		checkScalibility(writer, 4500, 2);
		checkScalibility(writer, 5000, 2);
		*/
		checkScalibility(writer, 1500, 10);
	}
	
	private static void checkScalibility(BufferedWriter writer, int size, int number) {
		long resultsSum = 0;
		for (int i = 0; i < 20; i++) {
			TopologyContainerAddon topo = createRandomTopology(size, i + 123, number);
			TopologyGenerator gen = new TopologyGenerator();
			MicroGridsGenerator mggen = new MicroGridsGenerator();
			mggen.setMaxSize(40);
			long startTime = System.currentTimeMillis();
			gen.setSmartMeterAddonData(topo.getAddonData());
			gen.generateTopo(topo.getTopologyContainer());
			long endTime = System.currentTimeMillis();
			resultsSum += (endTime - startTime);
		}
		try {
			writer.write("Grid size " + size + " average time: " + resultsSum / 20 + "ms");
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createRandomConnections(int number, int size, PrintWriter writer) {
		int counter = 1;
		for (int i = 2; i <= size; i++) {
			/* Old Random connections Code
			int number = random.nextInt(4);
			number = Math.min(number, i - 1);
			int lower = 1;
			int upper = i;
			for (int j = 1; j <= number; j++) {
				int target = random.nextInt(upper - lower) + lower;
				if (random.nextBoolean())
					upper = target;
				else
					lower = target + 1;
				String line = counter + "," + target + ",," + i + ",,1,test,1,1,1,1,1,1";
				writer.println(line);
				counter++;
				if (upper <= lower)
					break;
			} */
			String line = counter + "," + (i - 1) + ",," + i + ",,1,test,1,1,1,1,1,1";
			writer.println(line);
			counter++;
			ArrayList<Integer> random = new ArrayList<Integer>();
			for (int j = 1; j < i - 1; j++) {
				random.add(j);
			}
			Collections.shuffle(random);
			for (int j = 0; j < Math.min(number - 1, i - 2); j++) {
				int target = random.get(j);
				line = counter + "," + target + ",," + i + ",,1,test,1,1,1,1,1,1";
				writer.println(line);
			}
			writer.println(line);
			counter++;
		}
	}
	
	/**
	 * Generate a pseudo random TopologyContainer
	 * @param size number of High Voltage Nodes in the network
	 * @param seed seed to use for random generation
	 * @return the random TopologyContainer
	 */
	public static TopologyContainerAddon createRandomTopology(int size, int seed, int connections) {
    	SmartGridTopoContainer topo = null;// = new TopologyContainer(); currently no public constructor, needs file
    	File opfFile = null;
		File hvnFile = null;
		File hvnconFile = null;
    	try {
			opfFile = File.createTempFile("opf", "_tmp");
			hvnFile = File.createTempFile("hvnFile", "_tmp");
			hvnconFile = File.createTempFile("hvnconFile", "_tmp");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	Random random = new Random(seed);
    	
    	try {
			PrintWriter writer = new PrintWriter(hvnFile);
			writer.println();
			for (int i = 1; i <= size; i++) {
				String line = i + "," + "T" + i + "," + "test" + i + ","; //name and ID
				line += (random.nextDouble() * 100) + "," + (random.nextDouble() * 100) + ","; //coordinates
				line += ",,0," + (random.nextDouble() * 100) + "," + (random.nextDouble() * 100) + ",";
				writer.println(line);
			}
			writer.close();
			writer = new PrintWriter(hvnconFile);
			writer.println();
			createRandomConnections(connections, size, writer);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	//TODO:MEB:Auskommentiert um später zu wissen was mit High Voltage nodes zu machen
    	//topo = SmartGridTopoContainer.build(hvnFile, hvnconFile, opfFile);
    	
    	//TODO:MEB:Auskommentiert um später zu wissen was mit High Voltage nodes zu machen
    	//Generate Smart meters for testing (random with seed)
    	LinkedHashMap<String, SmartMeterAddonData> addonData = new LinkedHashMap<String, SmartMeterAddonData>();
//    	LinkedHashMap<String, Map<String, SmartComponentGeoData>> _mapCItoHVN = new LinkedHashMap<String, Map<String,SmartComponentGeoData>>();
//    	int i = 1;
//    	Random r = new Random(seed);
//    	for (HighVoltageNode node : topo.getSmartMeterContainer().values()) {
//    		Map<String, SmartComponentGeoData> smartMeters = new LinkedHashMap<String, SmartComponentGeoData>();
//    		for (int num = r.nextInt(3) + 1; num > 0; num--) {
//    			SmartComponentGeoData sm = new SmartComponentGeoData();
//				sm.setCompName("testSM " + i);
//				sm.setCompID(Integer.toString(i));
//				sm.setCompType("testType");
//				sm.setLat(node.getLat() + r.nextDouble() * 0.02 - 0.01);
//				sm.setLon(node.getLon() + r.nextDouble() * 0.02 - 0.01);
//				smartMeters.put(sm.getCompID(), sm);
//				SmartMeterAddonData data = new SmartMeterAddonData();
//				data.setPower(r.nextDouble() * 200 - 120); //TODO: Do something else instead of pure Random?
//				addonData.put(sm.getCompID(), data);
//				i++;
//    		}
//    		_mapCItoHVN.put(node.getId(), smartMeters);
//    	}
//		topo.setSmartMeterContainer(_mapCItoHVN);
		
		
		//clean up Temporary Files
		opfFile.delete();
		hvnFile.delete();
		hvnconFile.delete();
		
		return new TopologyContainerAddon(topo, addonData);
	}
}
