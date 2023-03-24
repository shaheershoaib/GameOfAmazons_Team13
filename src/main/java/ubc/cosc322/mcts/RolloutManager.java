package ubc.cosc322.mcts;

import ubc.cosc322.actionutil.Action;
import ubc.cosc322.actionutil.ActionFactory;

import java.util.ArrayList;
import java.util.Random;

final class RolloutManager {

    static int rollout(Node node, int numRolloutsOnParent)
    {
        ActionFactory actionFactory = new ActionFactory(node.getState(), node.getPlayerType());
        ArrayList<Action> actions = actionFactory.getActions();


        if(node.getTerminal()==-1) // If the value to check if whether a node is a terminal node or not, then call the method to find it.
            RolloutManager.setTerminalIfNotSet(node, actions);

        if(node.getTerminal() != 0) // isTerminal() returns a value of 0 to indicate a node is not a terminal node. Hence, if it isn't, it is clearly a terminal node
            return node.getTerminal(); // terminal holds the value of who won. For example, if the value is 1, then white won. If the value is 2, then black won.
            // This is recursively returned to check if the nodes along the path of the rollout had won.

        else
        {
            node.setRollouts(node.getRollouts()+1); // Increment the number of rollouts on the current node
            Node nextNodeToRolloutOn = RolloutManager.getRandomNodeToRolloutOn(node, actions);

            int winner =  RolloutManager.rollout(nextNodeToRolloutOn, node.getRollouts()); // Do a rollout, and return the winner
            if(winner == node.getPlayerType()) // If the winner was the type of the current node:
            {
                node.setTotalWins(node.getTotalWins()+1);  // increment its wins by 1 (Ex: if white won and the current node is white, then increment its wins by 1)
                node.updateUCB1(numRolloutsOnParent, 0);
            }

            else
            {
                node.updateUCB1(numRolloutsOnParent, node.getPunishmentVal());
                node.updatePunishmentVal();
            }
            // System.out.println("Before: "+node.ucb1Score);
            // Ensure that the current node has its UCB1 score updated
            //System.out.println("After: "+node.ucb1Score);

            return winner; // Continue to return the winner through the recursive call stack

        }
    }

    private static Node createNode(Action action, Node node)
    {
        int childPlayer;
        if(node.getPlayerType() == 1)
            childPlayer = 2;
        else childPlayer = 1;

        int[][] childState = new int[10][10];
        for (int row = 0; row < 10; row++)
            for (int col = 0; col<10; col++)
                childState[row][col] = node.getState()[row][col];


        childState[action.getQueenPositionCurrent()[0]][action.getQueenPositionCurrent()[1]] = 0;
        childState[action.getQueenPositionNew()[0]][action.getQueenPositionNew()[1]] = node.getPlayerType();
        childState[action.getArrowPosition()[0]][action.getArrowPosition()[1]] = 7;
        return new Node(childState, childPlayer, action.getQueenPositionCurrent(), action.getQueenPositionNew(), action.getArrowPosition(), action.getId());
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

   private static Node getRandomNodeToRolloutOn(Node node, ArrayList<Action> actions) {
       int size = actions.size();
       Random rand = new Random();
       int randomIndex = rand.nextInt(size);

       Action randomAction = actions.get(randomIndex);
       Node randomNode;

       if (node.getCurrentChildren().containsKey(randomAction.getId()))
           randomNode = node.getCurrentChildren().get(randomAction.getId());
       else {
           randomNode = createNode(randomAction, node);
           node.getCurrentChildren().put(randomNode.getId(), randomNode);
           randomAction = null;
       }

       return randomNode;
   }


   static void printArray(int[][] state)
   {
       for (int i = 0; i < 10; i++) {
           for (int j = 0; j < 10; j++) {
               System.out.print(state[i][j]+" ");
           }
           System.out.println();
       }
   }

}
