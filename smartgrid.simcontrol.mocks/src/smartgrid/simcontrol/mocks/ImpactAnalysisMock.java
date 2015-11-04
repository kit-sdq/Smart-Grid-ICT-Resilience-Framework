/**
 * 
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.IImpactAnalysis;
import smartgridinput.ScenarioState;
import smartgridoutput.Cluster;
import smartgridoutput.Online;
import smartgridoutput.ScenarioResult;
import smartgridoutput.SmartgridoutputFactory;
import smartgridoutput.impl.SmartgridoutputPackageImpl;
import smartgridtopo.ControlCenter;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.SmartMeter;

/**
 * This Class mocks the Imapct Analysis
 * 
 * It mocks the impact analysis in that way that regardless of the input states
 * every output state is marked as 'Online' and all states are packed in one
 * single cluster
 * 
 * @author Christian, Michael
 *
 */
public class ImpactAnalysisMock implements IImpactAnalysis {

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * 
	 * NOT yet implemented!
	 * 
	 */
	@Override
	public ScenarioResult run(SmartGridTopology smartGridTopo, ScenarioState impactAnalysisInput) {
		SmartgridoutputPackageImpl.init();
		SmartgridoutputFactory factory = SmartgridoutputFactory.eINSTANCE;
		ScenarioResult result = factory.createScenarioResult();

		result.setScenario(smartGridTopo);

		int smartmetercount = 0;
		int controlcentercount = 0;

		Cluster cl = factory.createCluster();
		for (smartgridinput.EntityState entity : impactAnalysisInput.getEntityStates()) {
			Online on = factory.createOnline();
			on.setOwner(entity.getOwner());
			on.setBelongsToCluster(cl);

			result.getStates().add(on);

			// Count smartMeters and controlCenters
			if (entity.getOwner() instanceof SmartMeter) {
				smartmetercount++;
			} else if (entity.getOwner() instanceof ControlCenter) {
				controlcentercount++;
			}
		}

		cl.setSmartMeterCount(smartmetercount);
		cl.setControlCenterCount(controlcentercount);

		result.getClusters().add(cl);

		return result;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 */
	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		return ErrorCodeEnum.SUCCESS;
	}

	@Override
	public String getName() {
		return "Impact Analyses Mock";
	}

}
