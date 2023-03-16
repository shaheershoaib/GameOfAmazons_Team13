package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Node
{
   private final int[][] state;
   private int rollouts;
   private int totalWins;
   private final int playerType; // 1 == White, 2 == Black

   public PriorityQueue<Node> children;
   List<Node> childrenAsList;
    private double ucb1Score;
    private int terminal; // Save the if the node is a terminal node or not if we're visiting this node again
    private final int[] queenCurrent;
    private final int[] queenNew;
    private final int[] arrowPosition;

    public Node(int[][] state, int playerType, int[] queenCurrent, int[] queenNew, int[] arrowPosition)
    {
        this.state = state;
        this.playerType = playerType;
        this.ucb1Score = Double.POSITIVE_INFINITY; // Let an unexplored node have a default value of Positive Infinity, as nodes that haven't been generated should have a high priority.
        this.terminal = -1; // There is no way to tell at the moment if this node is a terminal node or not. So, set it to -1
        this.queenCurrent = queenCurrent;
        this.queenNew = queenNew;
        this.arrowPosition = arrowPosition;
    }

    public double getAverageWins(){
        return totalWins/(double)rollouts;
    }

    public int[][] getState()
    {
        return state;
    }

    public void doRollout()
    {

        if(this.terminal == -1)
        {

            ActionFactory actions = new ActionFactory(this.state, this.playerType);
            this.children = actions.getActions();
            if (this.children.size() == 0)
            {
                if (this.playerType == 1)
                    this.terminal = 2;
                 else this.terminal = 1;
            }
                else this.terminal = 0;

        }


        if(this.terminal==0) //Rollout on current node it isn't terminal
        {
            this.rollouts++;

            if (children == null)
            {
                ActionFactory actions = new ActionFactory(state, playerType);
                children = actions.getActions();
            }
            Node nodeWithHighestUCB1Score = children.peek(); // We only use peek() as we don't actually want the node to be removed


            doRollout(nodeWithHighestUCB1Score, rollouts); // Even though this method returns an integer, this is not useful for the root node. The method only returns an integer
        }                                     // in order to update totalWins of descendant nodes

    }

    private int doRollout(Node node, int numRolloutsOnParent)
    {


        if(node.terminal==-1) // If the value to check if whether a node is a terminal node or not, then call the method to find it.
                            // Saving the result in a terminal variable will save time when checking if a node is a terminal node IF the node has already been visited
        {
            ActionFactory actions = new ActionFactory(node.state, node.playerType);
            node.children = actions.getActions();
            if(node.children.size() == 0)
            {
                if (node.playerType == 1)
                    node.terminal = 2;
                else node.terminal = 1;
            }
            else node.terminal = 0;




        }


        if(node.terminal != 0) // isTerminal() returns a value of 0 to indicate a node is not a terminal node. Hence, if it isn't, it is clearly a terminal node
            return node.terminal; // terminal holds the value of who won. For example, if the value is 1, then white won. If the value is 2, then black won.
                                    // This is recursively returned to check if the nodes along the path of the rollout had won.

        else
        {
            node.rollouts++; // Increment the number of rollouts on the current node
        if(node.children == null)
        {
            ActionFactory actions = new ActionFactory(node.state, node.playerType);
            node.children = actions.getActions();
            actions = null;
        }
            if(node.childrenAsList == null) // childrenAsList holds the nodes in priority queue, "children", as a direct copy. This allows us to choose a random child
                copyChildrenInPriorityQueueToArrayList(node); // If the Queue has not yet been copied over to the ArrayList, meaning this is first time we're visiting this node, copy it over, and save it
            Node randomNodeFromChildQueue = peekRandom(node);

            int winner =  doRollout(randomNodeFromChildQueue, node.rollouts); // Do a rollout, and return the winner
            if(winner == node.playerType) // If the winner was the type of the current node:
            {
                node.totalWins++;  // increment its wins by 1 (Ex: if white won and the current node is white, then increment its wins by 1)
            }
            node.updateUCB1(numRolloutsOnParent); // Ensure that the current node has its UCB1 score updated
            return winner; // Continue to return the winner through the recursive call stack

        }
    }

    public void copyChildrenInPriorityQueueToArrayList(Node node){
        node.childrenAsList = new ArrayList<>(node.children);
    }


    public Node peekRandom(Node node)
    {
        Random random = new Random();
        int index = random.nextInt(node.childrenAsList.size());

        // Return the element at the random index
        return node.childrenAsList.get(index);
    }



    public int isTerminal() // Can check the ArrayList of actions
    {
        int amazonCantMoveCounter = 0; // If 4 amazons cannot move, then the player has lost
        boolean availableMoveExists = false;
        for (int row = 0; row < 10 && amazonCantMoveCounter<4 && !availableMoveExists; row++)
            for (int col = 0; col < 10 && amazonCantMoveCounter<4 && !availableMoveExists; col++)
            {
                if (state[row][col] == playerType) // If the current tile has an amazon of the current playerType (White or Black)
                {
                    if
                    (
                                    (col != 0 && state[row][col - 1] == 0) || // Left
                                    (col != 9 && state[row][col + 1] == 0) ||  //Right
                                    (row != 0 && state[row - 1][col] == 0) || //Top
                                    (row != 9 && state[row + 1][col] == 0) || //Bottom
                                    (col != 0 && row != 0 && state[row - 1][col - 1] == 0) || // Top Left
                                    (col != 9 && row != 0 && state[row - 1][col + 1] == 0) || // Top Right
                                    (col != 0 && row != 9 && state[row + 1][col - 1] == 0) || // Bottom Left
                                    (col != 9 && row != 9 && state[row + 1][col + 1] == 0)    // Bottom Right

                    ) {availableMoveExists = true;} // If this if-condition passes, then clearly there is an available move. Hence, we can break the loops

                    else amazonCantMoveCounter++; // If the if-condition fails, then it meant that for the amazon that was being checked had no available move. Hence, increment the count of amazons that don't have a move.

                }

            }

        if(amazonCantMoveCounter == 4)
        {
            if(playerType == 1) //If white, return that black won
                return 2;
            else if(playerType == 2) //If black, return that white won
                return 1;

        }

        return 0; // Not a terminal node

    }

    public void updateUCB1(int parentRollouts) // Calculate the UCB1 score. Current letting C = sqrt(2)
    {
        double c = Math.sqrt(2);
        int nodeRollouts = this.rollouts;
        double explorationTerm = c * Math.sqrt(Math.log(parentRollouts) / nodeRollouts);
        this.ucb1Score = getAverageWins() + explorationTerm;
    }


    public double getUcb1Score()
    {
        return ucb1Score;
    }

    public int[] getQueenCurrent()
    {
        return queenCurrent;
    }

    public int[] getQueenNew()
    {
        return queenNew;
    }

    public int[] getArrowPosition()
    {
        return arrowPosition;
    }

    public void printArray(int[][] state)
    {

        for (int row = 0; row < 10; row++)
        {
            for (int col = 0; col < 10; col++)
            {
                System.out.print(state[row][col]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public double calculateMobilityHeuristic() {
        ActionFactory actionFactoryCurrent = new ActionFactory(state, playerType);
        int currentPlayerMoves = actionFactoryCurrent.getActions().size();

        int opponent;
        if (playerType == 1) {
            opponent = 2;
        } else {
            opponent = 1;
        }

        ActionFactory actionFactoryOpponent = new ActionFactory(state, opponent);
        int opponentMoves = actionFactoryOpponent.getActions().size();

        return (currentPlayerMoves - opponentMoves) / (double)(currentPlayerMoves + opponentMoves);
    }
}
