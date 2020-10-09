package smartgrid.newsimcontrol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import smartgridoutput.EntityState;
import smartgridoutput.On;
import smartgridoutput.ScenarioResult;

public class ReportGenerator {
	
	static final  String TOTAL = "Total";
    static final  String HACKED_TITLE = "Hacked";
    
    /**
     * Saves scenario result
     * @param resultFile resultFile
     * @param scenarioResult scenarioResult
     */
    public static void saveScenarioResult(final File resultFile, final ScenarioResult scenarioResult) {
        try {
            if (resultFile.exists()) {
                return;
            }
            resultFile.createNewFile();
            final FileWriter fileWriter = new FileWriter(resultFile);

            fileWriter.write(getScenarioResultStats(scenarioResult));

            fileWriter.close();
        } catch (IOException e) {
            System.err.print("Could not write ScenarioResult report to " + resultFile.getAbsolutePath());
        }
    }

    /**
     * Gathers stats and returns them as CSV formatted String
     * 
     * @return String of stats in CSV format
     */
    private static String getScenarioResultStats(ScenarioResult scenarioResult) {

        String headlines = "";
        String content = "";

        Map<String, Integer> stats = new HashMap<>();
        stats.put(TOTAL, 0);
        stats.put(HACKED_TITLE, 0);

        for (EntityState state : scenarioResult.getStates()) {
            String name = state.getClass().getSimpleName();
            if (stats.get(name) == null) {
                stats.put(name, 0);
            }
            stats.replace(name, stats.get(name).intValue() + 1);
            
            //Count hacked
			if(state instanceof On && ((On)state).isIsHacked()){
				stats.replace(HACKED_TITLE, stats.get(HACKED_TITLE) + 1);
			}
            //Count Total
            stats.replace(TOTAL, stats.get(TOTAL).intValue() + 1);
        }
        for (String key : stats.keySet()) {
            if (content != "" && headlines != "") {
                content += ";";
                headlines += ";";
            }
            headlines += key;
            content += stats.get(key).intValue();
        }
        return headlines + "\n" + content;
    }
}
