package smartgrid.model.topo.profiles.completions.test;

import org.junit.Test;

import smartgrid.model.topo.profiles.completions.ReplicationCompletionForSmartMeter;
import smartgrid.model.topo.profiles.smartgridprofiles.api.SmartGridStereotypesAPI;
import smartgridtopo.SmartMeter;
import topoextension.Replication;

public class ReplicationCompletionTest extends SmartGridCompletionTest {

    @Test
    public void testReplicationCompletion() {
        SmartMeter smartMeter = getFirstSmartMeter();
        Replication replication = SmartGridStereotypesAPI.getReplicationStereotype(smartMeter);
        long smartMetersBeforeReplication = AssertHelper.countSmartMetersInTopology(smartGridTopology);

        ReplicationCompletionForSmartMeter replicationCompletion = new ReplicationCompletionForSmartMeter();
        replicationCompletion.executeCompletion(smartMeter, replication);

        AssertHelper.assertReplicationCompletion(smartGridTopology, smartMeter, replication, smartMetersBeforeReplication);
    }

}
