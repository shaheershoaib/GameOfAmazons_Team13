package ubc.cosc322.mcts;

import ubc.cosc322.actionutil.Action;
import ubc.cosc322.actionutil.ActionFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class MCTS_Manager {

   private static Node node;
   private static Node childNodeWithHighestUCB1Score;
   private static MCTS_Thread[] threads;
   private static int numThreads = 1;
   private static int terminal;

   public static void setCurrentNode(Node node)
   {
       MCTS_Manager.node = node;
       NodeChildrenGenerator.generateChildrenOfGivenNode(node);
       MCTS_Manager.childNodeWithHighestUCB1Score = node.getChildren().peek();
       terminal = MCTS_Manager.node.getTerminal();

   }



   public static void doRollout() throws InterruptedException {


       if(node.getTerminal()==0) //Rollout on current node it isn't terminal
       {
           createThreads();
           //System.out.println("Threads Created!");
           startThreads();
           joinThreads();
           //System.out.println("Thread are joined!");
           MCTS_Manager.childNodeWithHighestUCB1Score = node.getChildren().peek();
       }
       else System.out.println("We have lost, so we can't do a rollout!");

   }

   public static void makeMove()
   {
       setCurrentNode(childNodeWithHighestUCB1Score);
   }
   public static boolean isOpponentMoveValid(Map<String, Object> msgDetails)
   {
       System.out.println("My player type is: "+node.getPlayerType());
       ActionFactory actionFactory = new ActionFactory(node.getState(), node.getPlayerType());
       ArrayList<Action> actions = actionFactory.getActions();
       return OpponentActionValidator.validateOpponentMove(msgDetails, actions);

   }


    private static ArrayList<Node> getNodesWithHighestUCB1Value()
    {
        double highestUCB1Score = node.getChildren().peek().getUcb1Score();
        ArrayList<Node> nodesWithHighestUCB1Score  = new ArrayList<>();
        LinkedList<Node> discardedNodes = new LinkedList<>();
        for(int i=0; i<node.getChildren().size(); i++)
        {
            Node childNode = node.getChildren().poll();
            if(childNode.getUcb1Score() == highestUCB1Score)
            {
                nodesWithHighestUCB1Score.add(childNode);
            }
            else
            {
                discardedNodes.add(childNode);
            }


        }

        for(Node discardedNode: discardedNodes)
        {
            node.getChildren().add(discardedNode);
        }
        discardedNodes = null;

        return nodesWithHighestUCB1Score;

    }


   /** HELPER METHODS **/
   private static void createThreads()
   {
       threads = new MCTS_Thread[numThreads];
   }



   private static void startThreads()
   {

       ArrayList<Node> nodesWithHighestUCB1Value = getNodesWithHighestUCB1Value();
       for(int i=0; i<threads.length; i++)
       {
           if(!nodesWithHighestUCB1Value.isEmpty())
           {
               Random random = new Random();
               int randomIdx = random.nextInt(nodesWithHighestUCB1Value.size());
               threads[i] = new MCTS_Thread(nodesWithHighestUCB1Value.get(randomIdx), node.getRollouts());
               threads[i].start();
               nodesWithHighestUCB1Value.remove(randomIdx);

           }
           else
               if(!node.getChildren().isEmpty())
                    nodesWithHighestUCB1Value = getNodesWithHighestUCB1Value();
       }

       int size = nodesWithHighestUCB1Value.size();
       for (Node childNode : nodesWithHighestUCB1Value) {
           node.getChildren().add(childNode);
       }
       nodesWithHighestUCB1Value = null;
   }


   private static void joinThreads() throws InterruptedException
   {
       for (MCTS_Thread thread : threads) {

           if (thread!=null) {
               thread.join();
               node.getChildren().add(thread.getNode());
           }

       }

   }


   public static void setThreads(int numThreads)
   {
       MCTS_Manager.numThreads = numThreads;
   }

    public static Node getNode() {
        return node;
    }



    /** DO ROLLOUT HELPER METHODS **/


}
