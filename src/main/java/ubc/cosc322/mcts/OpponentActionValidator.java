package ubc.cosc322.mcts;

import ubc.cosc322.actionutil.Action;
import ubc.cosc322.actionutil.ActionFactory;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

import java.util.ArrayList;
import java.util.Map;

final class OpponentActionValidator {

    private static ArrayList<Integer> opponentOriginalQueenPosition;
    private static ArrayList<Integer> opponentNewQueenPosition;
    private static ArrayList<Integer> opponentArrowPosition;
    private static ArrayList<Action> allPossibleActionsFromCurrentNode;
    private static Action opponentAction;


    static boolean validateOpponentMove(Map<String, Object> msgDetails, ArrayList<Action> allPossibleActionsFromCurrentNode)
    {
        OpponentActionValidator.allPossibleActionsFromCurrentNode = allPossibleActionsFromCurrentNode;
        getOpponentMove(msgDetails);
        if(opponentActionValid())
        {
            updateOurCurrentNodeToMatchOpponentAction();
            return true;
        }
        return false;
    }

    private static void getOpponentMove(Map<String, Object> msgDetails) {
        opponentOriginalQueenPosition = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
        opponentNewQueenPosition = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
        opponentArrowPosition = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
    }




    private static boolean opponentActionValid() {
        for (Action action : allPossibleActionsFromCurrentNode) {
            if (action.getQueenPositionCurrent()[0] == 9 - opponentOriginalQueenPosition.get(0)+1
                    && action.getQueenPositionCurrent()[1] == opponentOriginalQueenPosition.get(1)-1
                    && action.getQueenPositionNew()[0] == 9 - opponentNewQueenPosition.get(0)+1
                    && action.getQueenPositionNew()[1] == opponentNewQueenPosition.get(1)-1
                    && action.getArrowPosition()[0] == 9 - opponentArrowPosition.get(0)+1
                    && action.getArrowPosition()[1] == opponentArrowPosition.get(1)-1) {
                opponentAction = action;
                return true;
            }

        }

        return false;
    }

    private static void updateOurCurrentNodeToMatchOpponentAction() {
        if (opponentAction != null) {

            if (MCTS_Manager.getNode().getCurrentChildren().containsKey(opponentAction.getId())) {
                Node child = MCTS_Manager.getNode().getCurrentChildren().get(opponentAction.getId());
                MCTS_Manager.setCurrentNode(child);


            } else {
                MCTS_Manager.setCurrentNode(createNewNodeFromStateUsingAction(MCTS_Manager.getNode().getState(), MCTS_Manager.getNode().getPlayerType(), opponentAction));
            }
        }
    }

   private static Node createNewNodeFromStateUsingAction(int[][] state, int playerTypeOfAction, Action action) {

        int[][] newState = NodeChildrenGenerator.cloneState(state);

        NodeChildrenGenerator.getNewStateUsingAction(newState, playerTypeOfAction, action);
        return new Node(newState, NodeChildrenGenerator.getPlayerTypeOfChildren(playerTypeOfAction), action.getQueenPositionCurrent(), action.getQueenPositionNew(), action.getArrowPosition(), action.getId());
    }


}
