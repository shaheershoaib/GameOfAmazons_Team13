package ubc.cosc322.test;
import ubc.cosc322.actionutil.ActionFactory;
import ubc.cosc322.actionutil.Action;
import ubc.cosc322.mcts.MCTS_Manager;
import ubc.cosc322.mcts.Node;

import java.util.ArrayList;


public class test {
    public static void main(String[] args) throws InterruptedException
    {
        int[][] state= new int[][]{
                {0, 0, 0, 2, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
        };

        Node node = new Node (state, 2, null, null, null, 0);




        MCTS_Manager.setCurrentNode(node);
        MCTS_Manager.setThreads(6);


          // System.out.println("Not this one");





        int count = 0;

    for (int i = 0; i < 2500; i++)
    {

        MCTS_Manager.doRollout();
        count++;
        System.out.println(count);


    }



        System.out.println("At the end, Node ID: "+node.getChildren().peek().getId());
        System.out.println("At the end, Node UCB1: "+node.getChildren().peek().getUcb1Score());
        System.out.println("Children Size: "+node.getChildren().size());




        int size = node.getChildren().size();
for(int i=0; i<size; i++)
{
    Node banana = node.getChildren().poll();
    System.out.println("Node ID: "+banana.getId());
    System.out.println("\tNode UCB1: "+banana.getUcb1Score());
    System.out.println("\tNode rollouts "+banana.getRollouts());

    for (int j = 0; j < 3000; j++) {
        if(banana.getCurrentChildren().containsKey(j)) {
            System.out.println("\tChild of root node child Value: "+banana.getCurrentChildren().get(j).getUcb1Score());
            System.out.println("\tChild of root node child Value: "+banana.getCurrentChildren().get(j).getRollouts());
            break;
        }
    }
}




    }
}
