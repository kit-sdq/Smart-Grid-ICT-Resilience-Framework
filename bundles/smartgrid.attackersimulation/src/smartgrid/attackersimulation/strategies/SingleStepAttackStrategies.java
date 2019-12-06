package smartgrid.attackersimulation.strategies;

public abstract class SingleStepAttackStrategies extends AttackStrategies {

    public SingleStepAttackStrategies(final boolean ignoreLogicalConnections, final int hackingSpeed) {
        super(ignoreLogicalConnections, hackingSpeed);
    }

}
