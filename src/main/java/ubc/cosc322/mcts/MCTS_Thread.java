package ubc.cosc322.mcts;

public class MCTS_Thread extends Thread
{

    private final Node node;
   private final int numRolloutsOnParent;

    protected MCTS_Thread(Node node, int numRolloutsOnParent)
    {
    this.node = node;
    this.numRolloutsOnParent = numRolloutsOnParent;
    }



    protected Node getNode()
    {
        return node;
    }

    @Override
    public void run()
    {
        node.doRollout(node, numRolloutsOnParent);
    }
}
