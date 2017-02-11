package cz.jan.maly.model.agent;

import cz.jan.maly.sflo.FormulaInterface;

import java.util.List;

/**
 * Abstract class to be used as template for each concrete AgentActionCycle. Each implementation should define method
 * executedAction() which is called when conditions to run this action are met
 * Created by Jan on 14-Dec-16.
 */
public abstract class AgentActionCycleAbstract {
    private final List<FormulaInterface> formulasWhenActionCanBeExecuted;
    protected final Agent agent;

    public AgentActionCycleAbstract(Agent agent, List<FormulaInterface> formulasWhenActionCanBeExecuted) {
        this.agent = agent;
        this.formulasWhenActionCanBeExecuted = formulasWhenActionCanBeExecuted;
    }

    /**
     * OR - if any of the formulas evaluates as true, returns true
     * @return
     */
    public boolean areConditionForExecutionMet(){
        return formulasWhenActionCanBeExecuted.stream()
                .anyMatch(FormulaInterface::evaluate);
    }

    /**
     * Method try to execute action implemented by concrete AgentActionCycle and returns if execution was successful
     * @return
     */
    public abstract boolean executedAction();

}
