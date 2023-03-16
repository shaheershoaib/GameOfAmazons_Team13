package ubc.cosc322;

import java.util.ArrayList;

public class Action {
    private int[] queenPositionCurrent= new int[2];
    private int[] queenPositionNew= new int[2];
    private int[] arrowPosition= new int[2];
    Action(int[] queenPositionCurrent,int[] queenPositionNew, int[] arrowPosition){
        this.arrowPosition=arrowPosition;
        this.queenPositionCurrent=queenPositionCurrent;
        this.queenPositionNew=queenPositionNew;
    }

    public int[] getArrowPosition() {
        return arrowPosition;
    }

    public int[] getQueenPositionCurrent() {
        return queenPositionCurrent;
    }

    public int[] getQueenPositionNew() {
        return queenPositionNew;
    }
}
