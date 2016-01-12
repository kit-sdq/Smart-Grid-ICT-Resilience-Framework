package smartgrid.log4j;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class InitializeLogger {

	private static final Logger LOG = Logger.getLogger(InitializeLogger.class);

	private InitializeLogger() {

	}

	public static void initialize() {
		URL url = InitializeLogger.class.getResource("/log4j/log4j.properties");
		PropertyConfigurator.configure(url);
		LOG.info("Log4j Logger initialized");
	}
}
