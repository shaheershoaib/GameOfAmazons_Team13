package ubc.cosc322.mcts;

import ubc.cosc322.actionutil.Action;
import ubc.cosc322.actionutil.ActionFactory;

import java.util.ArrayList;
import java.util.Map;

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



   /** HELPER METHODS **/
   private static void createThreads()
   {
       threads = new MCTS_Thread[numThreads];
   }

   private static void startThreads()
   {
       for(int i=0; i<threads.length; i++)
       {
           if(node.getChildren()!=null)
           {
               threads[i] = new MCTS_Thread(node.getChildren().poll(), node.getRollouts());
               threads[i].start();
           }

       }
   }

   private static void joinThreads() throws InterruptedException {
       for(int i=0; i<threads.length; i++)
       {

               threads[i].join();
           if(threads[i].getNode()!=null)
           {
               node.getChildren().add(threads[i].getNode());

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
