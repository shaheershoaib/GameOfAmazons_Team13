package ubc.cosc322;

import java.util.ArrayList;

public class ActionFactory {


    public ArrayList actions=new ArrayList<Action>();
    int[][] state;

    int player;
    ActionFactory(int[][] state,int player){
        this.state=state;
        this.player=player;

    }
    public ArrayList getActions()
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
    state[queensPositionCurrent[0]][queensPositionCurrent[1]]=0;

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
            if ((i+x)==10||state[i+x][y]!=0){down=false;}
            else {
                int[] arrowsPosition= new int[2];
                arrowsPosition[0]=x+i;
                arrowsPosition[1]=y;
                addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);

            }}
            if(downRight){
                if((i+x)==10||(i+y)==10||state[i+x][i+y]!=0){downRight=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x+i;
                    arrowsPosition[1]=y+i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (downLeft){
                if((i+x)==10||(y-i)==-1||state[i+x][y-i]!=0){downLeft=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x+i;
                    arrowsPosition[1]=y-i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (up) {
                if ((x-i)==-1||state[x-i][y]!=0){up=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x-i;
                    arrowsPosition[1]=y;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (upRight){
                if ((x-i)==-1||(y+i)==10||state[x-i][y+i]!=0){upRight=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x-i;
                    arrowsPosition[1]=y+i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (upLeft){
                if ((x-i)==-1||(y-i)==-1||state[x-i][y-i]!=0){upLeft=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x-i;
                    arrowsPosition[1]=y-i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (left){
                if ((y-i)==-1||state[x][y-i]!=0){left=false;}
                else {
                    int[] arrowsPosition= new int[2];
                    arrowsPosition[0]=x;
                    arrowsPosition[1]=y-i;
                    addAction(queensPositionCurrent,queensPositionNew,arrowsPosition);
                }
            }
            if (right){
                if ((y+i)==10||state[x][y+i]!=0){right=false;}
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
        ArrayList queensPositionCurrentA=new ArrayList<>(2);
        queensPositionCurrentA.add(queensPositionCurrent[0]);
        queensPositionCurrentA.add(queensPositionCurrent[1]);
        ArrayList queensPositionNewA=new ArrayList<>(2);
        queensPositionNewA.add(queensPositionNew[0]);
        queensPositionNewA.add(queensPositionNew[1]);
        ArrayList arrowsPositionA=new ArrayList<>(2);
        arrowsPositionA.add(arrowsPosition[0]);
        arrowsPositionA.add(arrowsPosition[1]);
        actions.add(new Action(queensPositionCurrentA,queensPositionNewA,arrowsPositionA));
    }


    public void printActions() {
        for (int i=0;i<actions.size();i++){
          //  System.out.println(actions.size());
            Action action= (Action) actions.get(i);
            System.out.print(action.getQueenPositionCurrent());
            System.out.print(action.getQueenPositionNew());
            System.out.println(action.getArrowPosition());
        }
    }
}
