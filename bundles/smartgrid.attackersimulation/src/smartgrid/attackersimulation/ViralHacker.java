package smartgrid.attackersimulation;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.attackersimulation.strategies.BFSStrategy;
import smartgrid.attackersimulation.strategies.FullyMeshedStrategy;
import smartgrid.simcontrol.test.baselib.Constants;
import smartgrid.simcontrol.test.baselib.HackingType;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;
import smartgridtopo.SmartGridTopology;

/**
 * This Class represents an viral operating Hacker.
 *
 * Viral hacking means that every Node that has been hacked can hack other Nodes
 * that have a logical Connection to them.
 */
public class ViralHacker implements IAttackerSimulation {

	private static final Logger LOG = Logger.getLogger(ViralHacker.class);

	// state variables
	private boolean initDone;

	// config variables
	private int hackingSpeed;
	private HackingType hackingStyle;
	private boolean ignoreLogicalConnections;

	/**
	 * For ExtensionPoints .. use this together with the init() Method
	 */
	public ViralHacker() {
		this.initDone = false;
	}

	@Override
	public boolean enableHackingSpeed() {
		return true;
	}

	@Override
	public boolean enableLogicalConnections() {
		return true;
	}

	@Override
	public boolean enableRootNode() {
		return false;
	}

	/**
	 * 
	 *
	 * @param scenario
	 */
	private void fullyMeshedHacking(final ScenarioResult scenario) {
		final var hackedNodes = this.getHackedNodes(scenario);
		final var strategy = new FullyMeshedStrategy(hackingSpeed);
		for (final var rootNode : hackedNodes) {
			strategy.hackNextNode(rootNode);
		}
	}

	/**
	 * helper method to get the hacked nodes before every timeStep
	 *
	 * @param scenario
	 * @return list of hacked nodes
	 */
	private List<On> getHackedNodes(final ScenarioResult scenario) {
		return scenario.getClusters().stream().flatMap(e -> e.getHasEntities().stream()).filter(e -> e.isIsHacked())
				.collect(Collectors.toList());
	}

	@Override
	public String getName() {
		return "Viral Hacker";
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 *
	 * Remark Root NodeIDs {@link smartgrid.simcontrol.baselib.Constants} have to be
	 * List of String!
	 */
	@Override
	public void init(final ILaunchConfiguration config) throws CoreException {

		this.hackingSpeed = Integer
				.parseInt(config.getAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED));
		this.ignoreLogicalConnections = Boolean
				.valueOf(config.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.FALSE));

		this.hackingStyle = HackingType
				.valueOf(config.getAttribute(Constants.HACKING_STYLE_KEY, Constants.DEFAULT_HACKING_STYLE));

		LOG.info("Hacking speed is: " + this.hackingSpeed);
		LOG.info("Hacking style is: " + this.hackingStyle);
		LOG.debug("Init done");

		this.initDone = true;
	}

	@Override
	public ScenarioResult run(final SmartGridTopology topo, final ScenarioResult scenario) {

		if (!initDone)
			throw new IllegalStateException("LocalHacker not initialization. Run init()");

		LOG.debug("Start Hacking with Viral Hacker");
		switch (this.hackingStyle) {
		case STANDARD_HACKING:
			this.standardHacking(topo, scenario);
			break;
		case FULLY_MESHED_HACKING:
			this.fullyMeshedHacking(scenario);
			break;
		default:
			throw new IllegalStateException(this.hackingStyle + " not applicable for ViralHacker");
		}

		return scenario;
	}

	/**
	 * Standard hacking means that every node will search for the nearest node to it
	 * to hack
	 *
	 * @param topo                 the topology
	 * @param impactAnalysisOutput the output of the impact analysis
	 */
	private void standardHacking(final SmartGridTopology topo, final ScenarioResult impactAnalysisOutput) {
		final var hackedNodes = this.getHackedNodes(impactAnalysisOutput);
		final var strategy = new BFSStrategy(ignoreLogicalConnections, hackingSpeed);
		for (final var rootNode : hackedNodes) {
			strategy.hackNextNode(rootNode);
		}
	}
}
