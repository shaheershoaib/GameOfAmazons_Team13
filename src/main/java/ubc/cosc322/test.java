package ubc.cosc322;

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


try
{
    for (int i = 0; i < 10000; i++)
    {

        node.doRollout();
        numRollouts++;
        System.out.println(numRollouts);
    }
}

catch(OutOfMemoryError e){System.out.println(numRollouts);}

/*
        int[][] x = node.children.poll().getState();

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
