package smartgrid.model.topo.profiles.completions.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import smartgrid.model.topo.profiles.smartgridprofiles.api.SmartGridProfilesAPI;
import smartgrid.model.topo.profiles.smartgridprofiles.api.SmartGridStereotypesAPI;
import smartgridtopo.SmartMeter;
import topoextension.Replication;

public class SmartGridAPITest extends SmartGridCompletionTest {

    @Test
    public void testEnsureProfileAndStereotype() {
        assertTrue("SmartGridProfileAPI is not applied", SmartGridProfilesAPI.isSmartGridProfileApplied(smartGridTopology.eResource()));
        SmartMeter smartMeter = getFirstSmartMeter();
        Replication replication = SmartGridStereotypesAPI.getReplicationStereotype(smartMeter);
        assertTrue("replication should not be a proxy", !replication.eIsProxy());
        assertEquals("Nr of replication does not match expected replications", replication.getNrOfReplicas(), 4);
    }
}
