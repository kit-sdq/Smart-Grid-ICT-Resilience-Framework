/**
 * 
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.interfaces.ErrorCodeEnum;
import smartgrid.simcontrol.interfaces.ITimeProgressor;

/**
 * Mocks the TimeProgressor
 * 
 * @author Christian
 *
 */
public class TimeProgressorMock implements ITimeProgressor {

	/**
	 * {@inheritDoc}<p>
	 * 
	 * 
	 * This mock does nothing at all
	 * 
	 */
	@Override
	public void progress() {
		
		// No modification of the scenario between time steps

	}

	@Override
	public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
		// Nothing to do here..
		return ErrorCodeEnum.SUCESS;
	}

}
