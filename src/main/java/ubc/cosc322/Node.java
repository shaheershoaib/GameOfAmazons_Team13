package ubc.cosc322;

import java.util.*;

public class Node
{
   private final int[][] state;
   private int rollouts;
   private int totalWins;
   private final int playerType; // 1 == White, 2 == Black

    public PriorityQueue<Node> children = new PriorityQueue<>(Comparator.comparingDouble(Node::getUcb1Score).reversed());

    ArrayList<Action> actionArrayList;
   HashMap<Integer, Node> currentChildren;
    private double ucb1Score;
    private int terminal; // Save the if the node is a terminal node or not if we're visiting this node again
    private final int[] queenCurrent;
    private final int[] queenNew;
    private final int[] arrowPosition;
    private int id;

    public Node(int[][] state, int playerType, int[] queenCurrent, int[] queenNew, int[] arrowPosition, int id, int op)
    {
        this.state = state;
        this.playerType = playerType;
        this.ucb1Score = Double.POSITIVE_INFINITY; // Let an unexplored node have a default value of Positive Infinity, as nodes that haven't been generated should have a high priority.
        this.terminal = -1; // There is no way to tell at the moment if this node is a terminal node or not. So, set it to -1
        this.queenCurrent = queenCurrent;
        this.queenNew = queenNew;
        this.arrowPosition = arrowPosition;
        this.id = id;
        currentChildren = new HashMap<>();
        this.rollouts = 1;
        //Get actions
        ActionFactory actions = new ActionFactory(this.state, this.playerType);
        ArrayList<Action> actionArrayList = new ArrayList<>();
        actionArrayList = actions.getActions();



        for(Action action: actionArrayList)
            children.add(createNode(action, this));

    }

    public Node(int[][] state, int playerType, int[] queenCurrent, int[] queenNew, int[] arrowPosition, int id)
    {
        this.state = state;
        this.playerType = playerType;
        this.ucb1Score = Double.POSITIVE_INFINITY; // Let an unexplored node have a default value of Positive Infinity, as nodes that haven't been generated should have a high priority.
        this.terminal = -1; // There is no way to tell at the moment if this node is a terminal node or not. So, set it to -1
        this.queenCurrent = queenCurrent;
        this.queenNew = queenNew;
        this.arrowPosition = arrowPosition;
        this.id = id;
        currentChildren = new HashMap<>();

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
    //printArray(state);
        if(this.terminal == -1)
        {

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


            Node nodeWithHighestUCB1Score = children.poll(); // We only use peek() as we don't actually want the node to be removed

            doRollout(nodeWithHighestUCB1Score, this.rollouts); // Even though this method returns an integer, this is not useful for the root node. The method only returns an integer
            children.add(nodeWithHighestUCB1Score);



        }                                     // in order to update totalWins of descendant nodes

    }

    private int doRollout(Node node, int numRolloutsOnParent)
    {

        if(node.actionArrayList == null)
        {
            ActionFactory actions = new ActionFactory(node.state, node.playerType);
            node.actionArrayList = actions.getActions();
        }


        //printArray(node.state);
        if(node.terminal==-1) // If the value to check if whether a node is a terminal node or not, then call the method to find it.
                            // Saving the result in a terminal variable will save time when checking if a node is a terminal node IF the node has already been visited
        {

            if(node.actionArrayList.size() == 0)
            {
                if (node.playerType == 1)
                    node.terminal = 2;
                else node.terminal = 1;
            }
            else node.terminal = 0;

        }
        //System.out.println(node.terminal);


        if(node.terminal != 0) // isTerminal() returns a value of 0 to indicate a node is not a terminal node. Hence, if it isn't, it is clearly a terminal node
            return node.terminal; // terminal holds the value of who won. For example, if the value is 1, then white won. If the value is 2, then black won.
                                    // This is recursively returned to check if the nodes along the path of the rollout had won.

        else
        {
            node.rollouts++; // Increment the number of rollouts on the current node

            int size = node.actionArrayList.size();

            Random rand = new Random();
            int randomIndex = rand.nextInt(size);

            Action randomAction = node.actionArrayList.get(randomIndex);
            Node newNode;

            if(node.currentChildren.containsKey(randomAction.getId()))
                newNode = node.currentChildren.get(randomAction.getId());
            else
            {
                newNode = createNode(randomAction, node);
                node.currentChildren.put(newNode.getId(), newNode);
                randomAction = null;
            }


            int winner =  doRollout(newNode, node.rollouts); // Do a rollout, and return the winner
            if(winner == node.playerType) // If the winner was the type of the current node:
            {
                node.totalWins++;  // increment its wins by 1 (Ex: if white won and the current node is white, then increment its wins by 1)
            }

           // System.out.println("Before: "+node.ucb1Score);
            node.updateUCB1(numRolloutsOnParent); // Ensure that the current node has its UCB1 score updated
            //System.out.println("After: "+node.ucb1Score);

            return winner; // Continue to return the winner through the recursive call stack

        }
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
        return this.ucb1Score;
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

    public Node createNode(Action action, Node node)
    {
        int childPlayer;
        if(node.playerType == 1)
            childPlayer = 2;
        else childPlayer = 1;

        int[][] childState = new int[10][10];
        for (int row = 0; row < 10; row++)
            for (int col = 0; col<10; col++)
                childState[row][col] = node.state[row][col];


        childState[action.getQueenPositionCurrent()[0]][action.getQueenPositionCurrent()[1]] = 0;
        childState[action.getQueenPositionNew()[0]][action.getQueenPositionNew()[1]] = node.playerType;
        childState[action.getArrowPosition()[0]][action.getArrowPosition()[1]] = 7;
       return new Node(childState, childPlayer, action.getQueenPositionCurrent(), action.getQueenPositionNew(), action.getArrowPosition(), action.getId());
    }

    public int getId()
    {
        return id;
    }

    public int getPlayerType()
    {
        return playerType;
    }

    
}
