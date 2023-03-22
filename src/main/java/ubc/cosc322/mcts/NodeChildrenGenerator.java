package ubc.cosc322.mcts;

import ubc.cosc322.actionutil.Action;
import ubc.cosc322.actionutil.ActionFactory;

import java.util.ArrayList;

final class NodeChildrenGenerator {


    static void generateChildrenOfGivenNode(Node node) {


        ActionFactory actionFactory = new ActionFactory(node.getState(), node.getPlayerType());
        ArrayList<Action> actions = actionFactory.getActions();
        if(node.getTerminal()==-1)
            NodeChildrenGenerator.setTerminalIfNotSet(node, actions);
        if(node.getTerminal()==0)
            for (Action action : actions) {
                Node child = node.getCurrentChildren().get(action.getId());
                if (child != null)
                    node.getChildren().add(child);
                else {
                    NodeChildrenGenerator.createChildNode(node, action);

                }
            }
    }

    static void createChildNode(Node parentNode, Action action) {
        int[][] childState = NodeChildrenGenerator.cloneState(parentNode.getState());
        int childPlayerType = NodeChildrenGenerator.getPlayerTypeOfChildren(parentNode.getPlayerType());

        NodeChildrenGenerator.getNewStateUsingAction(childState, parentNode.getPlayerType(), action);
        parentNode.getChildren().add(new Node(childState, childPlayerType, action.getQueenPositionCurrent(), action.getQueenPositionNew(), action.getArrowPosition(), action.getId()));

    }

    static int[][] cloneState(int[][] state)
    {
        int[][] childState = new int[10][10];
        for (int row = 0; row < 10; row++)
            for (int col = 0; col < 10; col++)
                childState[row][col] = state[row][col];
        return childState;
    }

   static int getPlayerTypeOfChildren(int playerType) {
        if (playerType == 1)
            return 2;
        else
            return 1;

    }

    static void getNewStateUsingAction(int[][] state, int playerType, Action action)
    {
        state[action.getQueenPositionCurrent()[0]][action.getQueenPositionCurrent()[1]] = 0;
        state[action.getQueenPositionNew()[0]][action.getQueenPositionNew()[1]] = playerType;
        state[action.getArrowPosition()[0]][action.getArrowPosition()[1]] = 7;

    }

    private static void setTerminalIfNotSet(Node node, ArrayList<Action> actions)
    {
        if(actions.size() == 0)
        {
            if (node.getPlayerType() == 1)
                node.setTerminal(2);
            else node.setTerminal(1);
        }
        else node.setTerminal(0);
    }
}
