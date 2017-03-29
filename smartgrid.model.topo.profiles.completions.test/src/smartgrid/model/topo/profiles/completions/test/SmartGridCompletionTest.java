package smartgrid.model.topo.profiles.completions.test;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;

import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import topoextension.ExtensionRepository;

public class SmartGridCompletionTest {

	protected SmartGridTopology smartGridTopology;
	protected ExtensionRepository smartGridTopologyExtension;
	protected ResourceSet resourceSet;

	private static final String SMART_GRID_TEST_TOPOLOGY = "Test.smartgridtopo";
	private static final String SMART_GRID_TEST_EXTENSION_TOPOLOGY = "Test.topoextension";
	
	@Before
	public void before() {
		resourceSet = new ResourceSetImpl();
		final Resource resource = resourceSet.getResource(URI.createFileURI(SMART_GRID_TEST_TOPOLOGY), true);
		final Resource resourceExt = resourceSet.getResource(URI.createFileURI(SMART_GRID_TEST_EXTENSION_TOPOLOGY),
				true);
	
		smartGridTopology = (SmartGridTopology) resource.getContents().get(0);
		smartGridTopologyExtension = (ExtensionRepository) resourceExt.getContents().get(0);
	}

	protected SmartMeter getFirstSmartMeter() {
		SmartMeter smartMeter = (SmartMeter) smartGridTopology.getContainsNE().get(0);
		return smartMeter;
	}

}
