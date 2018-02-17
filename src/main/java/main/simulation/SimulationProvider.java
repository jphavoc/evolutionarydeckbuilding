package main.simulation;

import net.demilich.metastone.AbstractSimulationProvider;
import net.demilich.metastone.SimulationConfig;
import net.demilich.metastone.gui.simulationmode.SimulationResult;

/**
 * @author created by Jens Plümer on 23.11.17
 */
public class SimulationProvider extends AbstractSimulationProvider {

    private AbstractSimulationConfig config;

    public SimulationProvider(AbstractSimulationConfig config) {
        this.config = config;
    }

    @Override
    public SimulationConfig getConfig() {
        return config;
    }

    @Override
    public int handle(SimulationResult simulationResult) {

        if(simulationResult != null) {
            this.simulationResult = simulationResult;
            return 1;
        }

        return 0;
    }
}
