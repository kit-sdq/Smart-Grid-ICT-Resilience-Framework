package smartgrid.model.topo.profiles.completions.test;

import org.junit.Test;

import smartgrid.model.topo.profiles.completions.SmartGridCompletionExecuter;

public class SmartGridCompletionExecuterTest extends SmartGridCompletionTest {

	@Test
	public void testSmartGridCompletionExecuter() {
		SmartGridCompletionExecuter smartGridCompletionExecuter = new SmartGridCompletionExecuter();
		smartGridCompletionExecuter.executeCompletions(this.smartGridTopology);
		//todo: assert
	}

}
