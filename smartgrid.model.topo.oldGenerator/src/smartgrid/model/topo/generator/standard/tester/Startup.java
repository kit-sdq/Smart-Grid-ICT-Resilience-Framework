package smartgrid.model.topo.generator.standard.tester;

import java.io.File;
//import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.ui.IStartup;

import couplingToICT.SmartComponentGeoData;
import couplingToICT.SmartGridTopoContainer;
import smartgrid.helper.FileSystemHelper;
import smartgridtopo.SmartGridTopology;

/**
 * Startup class for the Topology generation
 * Used only for Testing and Evaluation of the generator
 * @author Eric Klappert
 *
 */
public class Startup implements IStartup {

	private static final String path = "L:";
	private static final String inputFilesPath = "L:/KIT/Bachelorarbeit/";
	
	public Startup() {
		super();
	}

    @Override
    public void earlyStartup() {
    	TestLogger.startTests();
    	
    	/* Old Code Currently not used
    	TopologyGenerator gen = new TopologyGenerator();
		SmartGridTopology topo = gen.generateTopo(generateBasicTestTopology());
    	FileSystemHelper.saveToFileSystem(topo, path + "\\generated.smartgridtopo");
    	*/
    }

    private SmartGridTopoContainer generateBasicTestTopology() {
    	SmartGridTopoContainer topo = null;// = new TopologyContainer(); currently no public constructor, needs file
//    	topo = SmartGridTopoContainer.build(new File(inputFilesPath +"HighVoltageNodes.txt"), new File(inputFilesPath + "HighVoltageNodeConnections.txt"), new File(inputFilesPath + "opfdummy.txt"));
//    	
//    	//Generate Smart meters for testing (random with seed)
//    	LinkedHashMap<String, Map<String, SmartComponentGeoData>> _mapCItoHVN = new LinkedHashMap<String, Map<String,SmartComponentGeoData>>();
//    	int i = 1;
//    	Random r = new Random(12345);
//    	for (HighVoltageNode node : topo.getHighVoltageNodes().values()) {
//    		Map<String, SmartComponentGeoData> smartMeters = new LinkedHashMap<String, SmartComponentGeoData>();
//    		for (int num = r.nextInt(3) + 1; num > 0; num--) {
//    			SmartComponentGeoData sm = new SmartComponentGeoData();
//				sm.setCompName("testSM " + i);
//				sm.setCompID(Integer.toString(i));
//				sm.setCompType("testType");
//				sm.setLat(node.getLat() + r.nextDouble() * 0.02 - 0.01);
//				sm.setLon(node.getLon() + r.nextDouble() * 0.02 - 0.01);
//				smartMeters.put(sm.getCompSmartID(), sm);
//				i++;
//    		}
//    		_mapCItoHVN.put(node.getId(), smartMeters);
//    	}
//		topo.setMapCItoHVN(_mapCItoHVN);
    	return topo;
    }

}
