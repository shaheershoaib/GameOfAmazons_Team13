package ubc.cosc322;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.PriorityQueue;

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

        Node node = new Node (state, 1, null, null, null, 0, 1);


        int numRollouts = 0;


    for (int i = 0; i < 2500; i++)
    {


        node.doRollout();
        System.out.println(i);
      //  numRollouts++;
       // System.out.println(numRollouts);
    }

        System.out.println("At the end, Node ID: "+node.children.peek().getId());
        System.out.println("At the end, Node UCB1: "+node.children.peek().getUcb1Score());




        int size = node.children.size();
for(int i=0; i<size; i++)
{
    Node banana = node.children.poll();
    System.out.println("Node ID: "+banana.getId());
    System.out.println("\tNode UCB1: "+banana.getUcb1Score());
    System.out.println("\tNode rollouts "+banana.getRollouts());

    for (int j = 0; j < 3000; j++) {
        if(banana.currentChildren.containsKey(j)) {
            System.out.println("\tChild of root node child Value: "+banana.currentChildren.get(j).getUcb1Score());
            System.out.println("\tChild of root node child Value: "+banana.currentChildren.get(j).getRollouts());
            break;
        }
    }
}




    }
}
