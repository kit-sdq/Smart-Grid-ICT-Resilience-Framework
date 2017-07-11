/**
 *
 */
package smartgrid.simcontrol.mocks;

import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.ITimeProgressor;

/**
 * Mocks the TimeProgressor
 *
 * @author Christian
 *
 */
public class NoOperationTimeProgressor implements ITimeProgressor {

    /**
     * {@inheritDoc}
     * <p>
     * This mock does nothing at all
     */
    @Override
    public void progress() {
    }

    @Override
    public ErrorCodeEnum init(final ILaunchConfiguration config) {
        // Nothing to do here..
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "No Operation";
    }

}
