package smartgrid.model.topo.profiles.completions.test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;
import topoextension.Replication;

public class AssertHelper {

    private AssertHelper() {
    }

    public static void assertReplicationCompletion(SmartGridTopology smartGridTopology, SmartMeter smartMeter, Replication replication, long smartMetersBeforeReplication) {
        long smartMetersAfterCompletion = countSmartMetersInTopology(smartGridTopology);
        assertEquals("Completion did not replicate the correct number of smart meters", smartMetersBeforeReplication + replication.getNrOfReplicas(), smartMetersAfterCompletion);
        ensureReplicaOfSmartMeter(smartGridTopology, smartMeter, replication.getNrOfReplicas() + 1);
    }

    public static long countSmartMetersInTopology(final SmartGridTopology smartGridTopology) {
        return smartGridTopology.getContainsNE().stream().filter(ne -> ne instanceof SmartMeter).count();
    }

    private static void ensureReplicaOfSmartMeter(SmartGridTopology smartGridTopology, SmartMeter smartMeter, int expectedReplicas) {
        List<SmartMeter> smartMeters = smartGridTopology.getContainsNE().stream().filter(ne -> ne instanceof SmartMeter).map(ne -> (SmartMeter) ne).collect(Collectors.toList());
        int foundSmartMeters = 0;
        for (SmartMeter smartM : smartMeters) {
            if (smartMeterEquals(smartMeter, smartM)) {
                foundSmartMeters++;
            }
        }
        assertEquals("Even though the replication number seems to be correct the actual replications do not match the original.", expectedReplicas, foundSmartMeters);
    }

    private static boolean smartMeterEquals(SmartMeter smartMeter, SmartMeter smartM) {
        return smartMeter.getName().equals(smartM.getName()) && smartMeter.getAggregation() == smartM.getAggregation() && smartMeter.getCommunicatesBy().equals(smartM.getCommunicatesBy())
                && smartMeter.getConnectedTo().equals(smartM.getConnectedTo()) && smartMeter.getId() == smartM.getId() && smartMeter.getIsA() == smartM.getIsA()
                && smartMeter.getLinkedBy().equals(smartM.getLinkedBy());
    }

}
