package smartgrid.newsimcontrol.rmi.helper;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import couplingToICT.initializer.InitializationMapKeys;
import smartgrid.simcontrol.test.baselib.Constants;

public class Helper {
	public static Map<InitializationMapKeys, String> getInitMapFromLaunchConfig(ILaunchConfiguration launchConfig) throws CoreException{
		Map<InitializationMapKeys, String> initMap = new HashMap<InitializationMapKeys, String>();
		
		//TODO: automatically copy the elements without identifying their names
        initMap.put(InitializationMapKeys.OUTPUT_PATH_KEY, launchConfig.getAttribute(Constants.OUTPUT_PATH_KEY, ""));
        initMap.put(InitializationMapKeys.INPUT_PATH_KEY, launchConfig.getAttribute(Constants.INPUT_PATH_KEY, ""));
        initMap.put(InitializationMapKeys.TOPO_PATH_KEY, launchConfig.getAttribute(Constants.TOPO_PATH_KEY, ""));
        initMap.put(InitializationMapKeys.TOPO_GENERATION_KEY, launchConfig.getAttribute(Constants.TOPO_GENERATION_KEY, "false"));
        initMap.put(InitializationMapKeys.TIME_STEPS_KEY, launchConfig.getAttribute(Constants.TIME_STEPS_KEY, ""));
		return initMap;
	}
}
