package smartgrid.impactanalysis.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class AbstractLaunchConfigurationTabGroup1 extends AbstractLaunchConfigurationTabGroup {

    @Override
    public void createTabs(final ILaunchConfigurationDialog dialog, final String mode) {
        final ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] { new LaunchConfigurationTab1(), new CommonTab() };
        this.setTabs(tabs);
    }
}
