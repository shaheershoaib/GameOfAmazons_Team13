package ubc.cosc322;

import java.util.ArrayList;

public class Action {
    private ArrayList queenPositionCurrent= new ArrayList<Integer>(2);
    private ArrayList queenPositionNew= new ArrayList<Integer>(2);
    private ArrayList arrowPosition= new ArrayList<Integer>(2);
    Action(ArrayList queenPositionCurrent,ArrayList queenPositionNew,ArrayList arrowPosition, int[][] state){
        this.arrowPosition=arrowPosition;
        this.queenPositionCurrent=queenPositionCurrent;
        this.queenPositionNew=queenPositionNew;
    }

    public ArrayList getArrowPosition() {
        return arrowPosition;
    }

    public ArrayList getQueenPositionCurrent() {
        return queenPositionCurrent;
    }

    public ArrayList getQueenPositionNew() {
        return queenPositionNew;
    }
}
