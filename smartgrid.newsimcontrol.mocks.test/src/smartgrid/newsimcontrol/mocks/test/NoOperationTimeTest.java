package smartgrid.newsimcontrol.mocks.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.simcontrol.test.mocks.NoOperationTimeProgressor;

public class NoOperationTimeTest {

	static NoOperationTimeProgressor noOperationTimeProgressor;
	@BeforeClass
    public static void setup() {
		noOperationTimeProgressor = new NoOperationTimeProgressor();
    }

    @Test
    public void testInit() {
    	Map<InitializationMapKeys, String> initMap = null;
		noOperationTimeProgressor.init(initMap);
    }
    @Test
    public void testProgress() {
		noOperationTimeProgressor.progress();
    }
    
    @Test
    public void testGetName() {
    	Map<InitializationMapKeys, String> initMap = null;
		noOperationTimeProgressor.init(initMap);
		 Assert.assertTrue(noOperationTimeProgressor.getName().equals("No Operation"));
    }

}
