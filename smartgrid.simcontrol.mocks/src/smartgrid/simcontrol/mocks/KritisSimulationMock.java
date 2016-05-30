package smartgrid.simcontrol.mocks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import smartgrid.simcontrol.baselib.ErrorCodeEnum;
import smartgrid.simcontrol.baselib.coupling.AbstractCostFunction;
import smartgrid.simcontrol.baselib.coupling.IKritisSimulationWrapper;
import smartgrid.simcontrol.baselib.coupling.PowerPerNode;

public class KritisSimulationMock implements IKritisSimulationWrapper {

    @Override
    public List<AbstractCostFunction> run(List<PowerPerNode> power) {
        return new ArrayList<AbstractCostFunction>();

//        List<AbstractCostFunction> list = new ArrayList<AbstractCostFunction>();
//        list.add(new LinearCostFunction(power.get(0).getPowerNodeID(), 1));
//        return list;
    }

    @Override
    public ErrorCodeEnum init(ILaunchConfiguration config) throws CoreException {
        return ErrorCodeEnum.SUCCESS;
    }

    @Override
    public String getName() {
        return "Mock";
    }
}
