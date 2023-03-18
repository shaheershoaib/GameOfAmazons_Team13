package ubc.cosc322;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class test {
    public static void main(String[] args) {
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



    for (int i = 0; i < 2600; i++)
    {


        node.doRollout();
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
    System.out.println("\tNode UCB1: "+node.getUcb1Score());

    for (int j = 0; j < 3000; j++) {
        if(banana.currentChildren.containsKey(j)) {
            System.out.println("\tChild of root node child Value: "+banana.currentChildren.get(j).getUcb1Score());
            break;
        }
    }
}




    }
}
