package smartgrid.impactanalysis;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import smartgridinput.ScenarioState;
import smartgridinput.impl.SmartgridinputPackageImpl;
import smartgridtopo.SmartGridTopology;
import smartgridtopo.impl.SmartgridtopoPackageImpl;

/**
 * This Class is the Delegate that launches an Impact Analysis.
 *
 * @implements ILaunchConfigurationDelegate
 *
 * @author Torsten
 * @author Christian (modified)
 *
 */
public class LaunchConfigurationDelegate1 implements ILaunchConfigurationDelegate {

    private static final Logger LOG = Logger.getLogger(LaunchConfigurationDelegate1.class);

    /**
     * {@inheritDoc}
     * <p>
     *
     * Launches an Impact Analysis with the given Launch Configuration See {@link GraphAnalyzer} for
     * Details about the Analysis
     */
    @Override
    public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch,
            final IProgressMonitor monitor) throws CoreException {

        LOG.debug("Loaded");
        LOG.debug("Find parameters");

        final String inputPath = configuration.getAttribute("inputPath", "");
        final String outputPath = configuration.getAttribute("outputPath", "");
        final String topoPath = configuration.getAttribute("topologyPath", "");
        LOG.debug("Input : " + inputPath);
        LOG.debug("Topology : " + topoPath);

        final ScenarioState initialState = this.loadInput(inputPath);
        final SmartGridTopology topo = this.loadScenario(topoPath);

        @SuppressWarnings("deprecation")
        final GraphAnalyzer ana = new GraphAnalyzer(outputPath);
        ana.analyze(topo, initialState);
    }

    private SmartGridTopology loadScenario(final String path) {
        SmartGridTopology s = null;
        SmartgridtopoPackageImpl.init();

        final ResourceSet resSet = new ResourceSetImpl();
        final Resource resource = resSet.getResource(URI.createFileURI(path), true);

        try {
            final EObject r = resource.getContents().get(0);
            LOG.debug("class: " + r.getClass());
            s = (SmartGridTopology) resource.getContents().get(0);

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private ScenarioState loadInput(final String path) {
        ScenarioState input = null;
        SmartgridinputPackageImpl.init();

        final ResourceSet resSet = new ResourceSetImpl();
        final Resource resource = resSet.getResource(URI.createFileURI(path), true);

        final EObject r = resource.getContents().get(0);
        LOG.debug("class: " + r.getClass());
        input = (ScenarioState) resource.getContents().get(0);

        return input;
    }
}