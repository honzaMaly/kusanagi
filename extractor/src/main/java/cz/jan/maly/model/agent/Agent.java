package cz.jan.maly.model.agent;

import cz.jan.maly.service.AgentsManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Agent abstraction class handles execution of main routine (in form of sequence) in agent.
 * Concrete agent has to describe creation of sequence of actions to execute, type of agent's knowledge and method to decided
 * if agent is still alive
 * Created by Jan on 09-Dec-16.
 */
@Getter
@AllArgsConstructor
public abstract class Agent {
    protected final AgentsKnowledge agentsKnowledge;
    protected boolean isAlive = true;

    //todo on creation. register to agent's manager and do own specific stuff

    //todo manage it subscribers (other agents) - it needs to be synchronized

    //todo workflow -> using actions. use thread to handle actions (sleep, wait, execution)

    //todo as it is abstract child should describe how to create action sequence, common knowledge and when is agent terminated

    /**
     * Method to be called when one want to terminate agent
     */
    public void terminateAgent() {
        this.isAlive = false;
        AgentsManager.getInstance().removeAgent(this);
    }

}
