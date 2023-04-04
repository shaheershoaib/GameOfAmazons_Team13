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
    private double punishmentVal = 0.4;

    private final PriorityQueue<Node> children = new PriorityQueue<>(Comparator.comparingDouble(Node::getUcb1Score).reversed());

  //  ArrayList<Action> actionArrayList;
   private final HashMap<Integer, Node> currentChildren = new HashMap<>();
    private double ucb1Score;
    private int terminal; // Save the if the node is a terminal node or not if we're visiting this node again
    private final int[] queenCurrent;
    private final int[] queenNew;
    private final int[] arrowPosition;
    private int id;
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
/*
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

    public double calculateTerritoryHeuristic() {
        int currentPlayerTerritory = countTerritory(playerType);
        int opponent = playerType == 1 ? 2 : 1;
        int opponentTerritory = countTerritory(opponent);

        return (currentPlayerTerritory - opponentTerritory) / (double)(currentPlayerTerritory + opponentTerritory);
    }

    public int countTerritory(int player) {
        int territory = 0;

        for (int row = 0; row < state.length; row++) {
            for (int col = 0; col < state[row].length; col++) {
                if (state[row][col] == player) {
                    territory++;
                }
            }
        }

        return territory;
    }

*/



    public double getAverageWins(){
        return totalWins/(double)rollouts;
    }

    public int[][] getState()
    {
        return state;
    }



    public void updateUCB1(int parentRollouts, double punishment) // Calculate the UCB1 score. Current letting C = sqrt(2)
    {

        double c = Math.sqrt(2);
        int nodeRollouts = this.rollouts;
        double explorationTerm = c * Math.sqrt(Math.log(parentRollouts) / nodeRollouts);
        this.ucb1Score = getAverageWins() + explorationTerm - 0.4*punishment;

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

    public void setUcb1Score(double ucb1Score) {
        this.ucb1Score = ucb1Score;
    }

    public void updatePunishmentVal()
    {
        this.punishmentVal = this.punishmentVal + 0.3;
    }

    public double getPunishmentVal() {
        return punishmentVal;
    }
}
