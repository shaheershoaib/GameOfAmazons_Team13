package ubc.cosc322.mcts;

 class MCTS_Thread extends Thread
{

    private final Node node;
   private final int numRolloutsOnParent;

    public MCTS_Thread(Node node, int numRolloutsOnParent)
    {
    this.node = node;
    this.numRolloutsOnParent = numRolloutsOnParent;
    }



    public  Node getNode()
    {
        return node;
    }

    @Override
    public void run()
    {
        RolloutManager.rollout(node, numRolloutsOnParent);
    }
}
