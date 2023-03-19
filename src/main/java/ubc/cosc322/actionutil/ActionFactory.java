package ubc.cosc322.actionutil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ActionFactory {


    public ArrayList<Action> actions= new ArrayList<>();

    int[][] state;

    int player;
    int id;
    public ActionFactory(int[][] state, int player){
        this.state=state;
        this.player=player;
        this.id=0;

    }
    public ArrayList<Action> getActions()
    {
//player is 1 or 2
        //number 7 on the board is for arrows


        for (int i=0;i< 10;i++){
            for (int j=0;j<10;j++){

                if (state[i][j]==player){

                 queensAction(i,j,state);


                }
            }
        }


        return actions;
    }
    private void queensAction(int x, int y,int[][] state){
        int[] queensPositionCurrent= new int[2];
        queensPositionCurrent[0]=x;
        queensPositionCurrent[1]=y;




        boolean left = true;
        boolean right= true;
        boolean upLeft= true;
        boolean upRight= true;
        boolean up= true;
        boolean down= true;
        boolean downLeft= true;
        boolean downRight= true;
        for (int i=1;i<10;i++) {
            //checks the next down tile to see if something blocks it
            if (down){
                if ((i+x)==10||state[i+x][y]!=0){down=false;}
                else {
                    int[] queensPositionNew=new int[2];
                    queensPositionNew[0]=x+i;
                    queensPositionNew[1]=y;

                    arrowsPosition(queensPositionCurrent,queensPositionNew,state);

                }}
            if(downRight){
                if((i+x)==10||(i+y)==10||state[i+x][i+y]!=0){downRight=false;}
                else {
                    int[] queensPositionNew=new int[2];
                    queensPositionNew[0]=x+i;
                    queensPositionNew[1]=y+i;
                    arrowsPosition(queensPositionCurrent,queensPositionNew,state);
                }
            }
            if (downLeft){
                if((i+x)==10||(y-i)==-1||state[i+x][y-i]!=0){downLeft=false;}
                else {
                    int[] queensPositionNew=new int[2];
                    queensPositionNew[0]=x+i;
                    queensPositionNew[1]=y-i;
                    arrowsPosition(queensPositionCurrent,queensPositionNew,state);
                }
            }
            if (up){
                if ((x-i)==-1||state[x-i][y]!=0){up=false;}
                else {
                    int[] queensPositionNew=new int[2];
                    queensPositionNew[0]=x-i;
                    queensPositionNew[1]=y;
                    arrowsPosition(queensPositionCurrent,queensPositionNew,state);
                }
            }
            if (upRight){
                if ((x-i)==-1||(y+i)==10||state[x-i][y+i]!=0){upRight=false;}
                else {
                    int[] queensPositionNew=new int[2];
                    queensPositionNew[0]=x-i;
                    queensPositionNew[1]=y+i;
                    arrowsPosition(queensPositionCurrent,queensPositionNew,state);
                }
            }
            if (upLeft){
                if ((x-i)==-1||(y-i)==-1||state[x-i][y-i]!=0){upLeft=false;}
                else {
                    int[] queensPositionNew=new int[2];
                    queensPositionNew[0]=x-i;
                    queensPositionNew[1]=y-i;
                    arrowsPosition(queensPositionCurrent,queensPositionNew,state);
                }
            }
            if (left){
                if ((y-i)==-1||state[x][y-i]!=0){left=false;}
                else {
                    int[] queensPositionNew=new int[2];
                    queensPositionNew[0]=x;
                    queensPositionNew[1]=y-i;
                    arrowsPosition(queensPositionCurrent,queensPositionNew,state);
                }
            }
            if (right){
                if ((y+i)==10||state[x][y+i]!=0){right=false;}
                else {
                    int[] queensPositionNew=new int[2];
                    queensPositionNew[0]=x;
                    queensPositionNew[1]=y+i;
                    arrowsPosition(queensPositionCurrent,queensPositionNew,state);
                }
            }
        }

    }
    private void arrowsPosition(int[] queensPositionCurrent,int[] queensPositionNew,int[][] state){
        //change the tile to 0 when queen moves
        int[][] stateCopy = new int[10][10];
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                stateCopy[row][col] = state[row][col];
            }
        }

    stateCopy[queensPositionCurrent[0]][queensPositionCurrent[1]]=0;

        int x=  queensPositionNew[0];
        int y=  queensPositionNew[1];
        boolean left = true;
        boolean right= true;
        boolean upLeft= true;
        boolean upRight= true;
        boolean up= true;
        boolean down= true;
        boolean downLeft= true;
        boolean downRight= true;
        for (int i=1;i<10;i++) {
            //checks the next down tile to see if something blocks it
            if (down){
            if ((i+x)==10||stateCopy[i+x][y]!=0){down=false;}
            else {
                int[] arrowsPosition= new int[2];
                arrowsPosition[0]=x+i;
                arrowsPosition[1]=y;
                addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);

            }}
            if(downRight){
                if((i+x)==10||(i+y)==10||stateCopy[i+x][i+y]!=0){downRight=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x+i;
                    arrowsPosition[1]=y+i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (downLeft){
                if((i+x)==10||(y-i)==-1||stateCopy[i+x][y-i]!=0){downLeft=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x+i;
                    arrowsPosition[1]=y-i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (up) {
                if ((x-i)==-1||stateCopy[x-i][y]!=0){up=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x-i;
                    arrowsPosition[1]=y;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (upRight){
                if ((x-i)==-1||(y+i)==10||stateCopy[x-i][y+i]!=0){upRight=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x-i;
                    arrowsPosition[1]=y+i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (upLeft){
                if ((x-i)==-1||(y-i)==-1||stateCopy[x-i][y-i]!=0){upLeft=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x-i;
                    arrowsPosition[1]=y-i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (left){
                if ((y-i)==-1||stateCopy[x][y-i]!=0){left=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x;
                    arrowsPosition[1]=y-i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (right){
                if ((y+i)==10||stateCopy[x][y+i]!=0){right=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x;
                    arrowsPosition[1]=y+i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }

            }


    }
    private void addAction(int[] queensPositionCurrent,int[] queensPositionNew,int[] arrowsPosition){
        //can flip 0 and 1 incase x and y are different in the servers cordinate system

        int[][] newState = new int[10][10];

        for(int row=0; row<10; row++)
            for(int col=0; col<10; col++)
                newState[row][col] = state[row][col];

        int newPlayer;
        if(player == 1)
            newPlayer = 2;
        else newPlayer = 1;


        this.id++;
        newState[queensPositionCurrent[0]][queensPositionCurrent[1]] = 0;
        newState[queensPositionNew[0]][queensPositionNew[1]] = player;
        newState[arrowsPosition[0]][arrowsPosition[1]] = 7;


        Action action = new Action(queensPositionCurrent, queensPositionNew, arrowsPosition, this.id);
        actions.add(action);

    }



}
