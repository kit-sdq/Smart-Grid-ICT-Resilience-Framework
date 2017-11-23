package smartgrid.simcontrol.coupling;

import java.util.List;
import java.util.Map;

/**
 * @author Misha
 */
public interface IPowerLoadSimulation {

  /**
   * @param kritisDemands
   *          Map von Node ID (String) auf Map von CI ID (String) auf PowerSpecs
   * @param smartMeterStates
   *          Map von Node ID auf Map von CI ID auf SmartMeterState
   * @return Map von Node ID (String) auf Map von CI ID (String) auf Leistung
   *         (MW)
   */
  Map<String,Map<String,Double>> run(Map<String,Map<String,PowerSpec>> kritisDemands, Map<String,Map<String,ISmartMeterState>> smartMeterStates);

  void initializeNodeIDs(List<String> _nodeIDs);
}
