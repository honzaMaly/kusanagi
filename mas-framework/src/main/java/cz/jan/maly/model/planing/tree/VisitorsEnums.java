package cz.jan.maly.model.planing.tree;

/**
 * Enumerates visitors of planning tree.
 * Created by Jan on 21-Feb-17.
 */
public enum VisitorsEnums implements TreeVisitorInterface {

    //todo implement

    COMMAND_EXECUTOR {
        @Override
        public void visit(LeafNodeWithPlan.WithOwnDesire leafNodeWithOwnDesire) {

        }

        @Override
        public void visit(LeafNodeWithPlan.WithAnotherAgentDesire leafNodeWithAnotherAgentDesire) {

        }

        @Override
        public void visit(LeafNodeWithDesireForOtherAgents leafNodeWithDesireForOtherAgents) {

        }

        @Override
        public void visit(WithOwnDesireIntermediateNode withOwnDesireIntermediateNode) {

        }

        @Override
        public void visit(WithAnotherAgentDesireIntermediateNode withAnotherAgentDesireIntermediateNode) {

        }
    },
    COMMITMENT_EXECUTOR {
        @Override
        public void visit(LeafNodeWithPlan.WithOwnDesire leafNodeWithOwnDesire) {

        }

        @Override
        public void visit(LeafNodeWithPlan.WithAnotherAgentDesire leafNodeWithAnotherAgentDesire) {

        }

        @Override
        public void visit(LeafNodeWithDesireForOtherAgents leafNodeWithDesireForOtherAgents) {

        }

        @Override
        public void visit(WithOwnDesireIntermediateNode withOwnDesireIntermediateNode) {

        }

        @Override
        public void visit(WithAnotherAgentDesireIntermediateNode withAnotherAgentDesireIntermediateNode) {

        }
    },
    REMOVE_COMMITMENT_EXECUTOR {
        @Override
        public void visit(LeafNodeWithPlan.WithOwnDesire leafNodeWithOwnDesire) {

        }

        @Override
        public void visit(LeafNodeWithPlan.WithAnotherAgentDesire leafNodeWithAnotherAgentDesire) {

        }

        @Override
        public void visit(LeafNodeWithDesireForOtherAgents leafNodeWithDesireForOtherAgents) {

        }

        @Override
        public void visit(WithOwnDesireIntermediateNode withOwnDesireIntermediateNode) {

        }

        @Override
        public void visit(WithAnotherAgentDesireIntermediateNode withAnotherAgentDesireIntermediateNode) {

        }
    }
}
