package it.unicam.cs.followme;

import it.unicam.cs.followme.model.environment.Position;
import it.unicam.cs.followme.model.items.ConditionSignaler;
import it.unicam.cs.followme.model.items.MovingItem;
import it.unicam.cs.followme.io.SimulationLoader;
import it.unicam.cs.followme.model.simulation.SimulationExecutor;

public class Controller<P extends Position<P>, L, I extends MovingItem<P> & ConditionSignaler<L>>{

    SimulationLoader<P, L, I> builder;
    SimulationExecutor<P, I> executor;


}
