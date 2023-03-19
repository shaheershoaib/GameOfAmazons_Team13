package ubc.cosc322;

public class MCTS_Thread extends Thread
{

    private Node node;
   private int numRolloutsOnParent;

    public MCTS_Thread(Node node, int numRolloutsOnParent)
    {
    this.node = node;
    this.numRolloutsOnParent = numRolloutsOnParent;
    }



    public Node getNode()
    {
        return node;
    }

    @Override
    public void run()
    {
        node.doRollout(node, numRolloutsOnParent);
    }
}
