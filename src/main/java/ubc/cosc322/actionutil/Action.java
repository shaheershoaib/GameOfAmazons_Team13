package ubc.cosc322.actionutil;

import java.util.ArrayList;

public class Action {
    private int[] queenPositionCurrent= new int[2];
    private int[] queenPositionNew= new int[2];
    private int[] arrowPosition= new int[2];
    private int id;
    Action(int[] queenPositionCurrent,int[] queenPositionNew, int[] arrowPosition, int id){
        this.arrowPosition=arrowPosition;
        this.queenPositionCurrent=queenPositionCurrent;
        this.queenPositionNew=queenPositionNew;
        this.id = id;
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

    public int getId()
    {
        return id;
    }
}
