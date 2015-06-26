/**
 * 
 */
package smartgrid.simcontrol.interfaces;

import smartgrid.simcontrol.baselib.inputgenerator.GenerationStyle;

/**
 * This Interface is used for Input Model Generation for the Simcontrol Approach
 * 
 * @author Christian
 *
 */
public interface IInputGenerator {

	/**
	 * Inits the Generator
	 */
	public void init(); // TODO Parameters needed ?

	/**
	 *  Generates a Input Model according the given Parameters
	 * 
	 * 
	 * @param desiredStyle sets the "shape" of the Input @see {@link smartgrid.simcontrol.baselib.inputgenerator.GenerationStyle}
	 * @param SmartMeterCount How many Smart Meters are in the generated Input
	 * @param ControlCenterCount How many Control Centers are in the generated Input
	 * @return Returns a @see {@link InputModelDTO} with the generated Input Model
	 */
	public InputModelDTO generate(GenerationStyle desiredStyle,
			int SmartMeterCount, int ControlCenterCount);

}
