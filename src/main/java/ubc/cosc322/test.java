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



    for (int i = 0; i < 4000; i++)
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
    //System.out.println("Node ID: "+banana.getId());
    //System.out.println("\tNode UCB1: "+node.getUcb1Score());
}

    /*
        Node y = node.children.poll();
        int[][] x = y.getState();
        System.out.println("ID: "+y.getId());
        for (int row = 0; row < 10; row++)
        {
            for (int col = 0; col < 10; col++)
            {
                System.out.print(x[row][col]+" ");
            }
            System.out.println();
        }
*/



        /*
ActionFactory actions= new ActionFactory(state,1);
PriorityQueue<Node> a= actions.getActions();
actions.printActions();
System.out.println(a.size());
*/




    }
}
