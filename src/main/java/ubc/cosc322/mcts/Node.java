package ubc.cosc322.mcts;
import ubc.cosc322.actionutil.Action;
import ubc.cosc322.actionutil.ActionFactory;


import java.util.*;

public class Node
{
   private final int[][] state;
   private int rollouts;
   private int totalWins;
   private final int playerType; // 1 == White, 2 == Black

    private final PriorityQueue<Node> children = new PriorityQueue<>(Comparator.comparingDouble(Node::getUcb1Score).reversed());

  //  ArrayList<Action> actionArrayList;
   private final HashMap<Integer, Node> currentChildren = new HashMap<>();
    private double ucb1Score;
    private int terminal; // Save the if the node is a terminal node or not if we're visiting this node again
    private final int[] queenCurrent;
    private final int[] queenNew;
    private final int[] arrowPosition;
    private int id;

   MCTS_Thread[] threads;
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
        this.rollouts = 1;

    }


    public double getAverageWins(){
        return totalWins/(double)rollouts;
    }

    public int[][] getState()
    {
        return state;
    }

    public void doRollout() throws InterruptedException
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

            for(int i=0; i< threads.length; i++)
            {
                if(children!=null)
                {
                    threads[i] = new MCTS_Thread(children.poll(), this.rollouts);
                    threads[i].start();
                }

            }

            for(int i=0; i<threads.length; i++)
            {
                if(threads[i].getNode()!=null)
                {
                    threads[i].join();
                    children.add(threads[i].getNode());

                }

            }
        }

        //Node nodeWithHighestUCB1Score = children.poll(); // We only use peek() as we don't actually want the node to be removed

           // doRollout(nodeWithHighestUCB1Score, this.rollouts); // Even though this method returns an integer, this is not useful for the root node. The method only returns an integer
            //children.add(nodeWithHighestUCB1Score);



                                          // in order to update totalWins of descendant nodes

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



    public int getId()
    {
        return id;
    }

    public int getPlayerType()
    {
        return playerType;

    }

    public int getRollouts()
    {
        return rollouts;
    }

    public void setRollouts(int rollouts) {
        this.rollouts = rollouts;
    }

    public int getTerminal() {
        return terminal;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public PriorityQueue<Node> getChildren() {
        return children;
    }

    public HashMap<Integer, Node> getCurrentChildren() {
        return currentChildren;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }


}
