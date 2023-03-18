package ubc.cosc322;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
public class StateGameManager extends GamePlayer
{

	private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;

		private int blackQueen = 2;
		private int whiteQueen = 1;
		private int arrow = 8;
		private int myQueen = -1;

		private int[][] board = null;
		static Node currentNode = null;

		static class MyTimerTask extends TimerTask {
			@Override
			public void run() {
				while(true){
					currentNode.doRollout();
				}
			}
		}
    
	public static void main(String[] args)
	{
    	StateGameManager player = new StateGameManager(args[0], args[1]);
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
	/**
     * Any name and passwd 
     * @param userName
      * @param passwd
     */
    public StateGameManager(String userName, String passwd) 
    {
    	this.userName = userName;
    	this.passwd = passwd;
			this.board = new int[10][10];
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }
	
	@Override
	public void connect() 
	{
    	gameClient = new GameClient(userName, passwd, this);
	}

	@Override
	public GameClient getGameClient() {
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		return this.gamegui;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	System.out.println(messageType);
    	System.out.println(msgDetails);
    	
    	switch(messageType) 
    	{
    	case GameMessage.GAME_STATE_BOARD:
				@SuppressWarnings("unused") ArrayList<Integer> board = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
				initGameBoard(board);
				displayGameBoard();
				this.gamegui.setGameState(board);
    		break;
			case GameMessage.GAME_ACTION_START:
				@SuppressWarnings("unused") String playingWhiteQueens = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
				@SuppressWarnings("unused") String playingBlackQueens = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
				assert(!playingWhiteQueens.equals(playingBlackQueens));
				setMyQueen(playingWhiteQueens);
				currentNode = new Node (this.board, myQueen, null, null, null, 0, 1);
				
					Timer timer = new Timer();
					MyTimerTask timerTask = new MyTimerTask();
					if(myQueen == 1)
					{
					
			
					// Schedule the timer task to run every 1000 milliseconds (1 second)
					timer.schedule(timerTask, 0, 25000);
					try {
						Thread.sleep(25000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					timer.cancel();

					currentNode = currentNode.children.poll();
					ArrayList<Integer> queenCurrent= new ArrayList<Integer>();
					ArrayList<Integer> queenNew = new ArrayList<Integer>();
					ArrayList<Integer> arrowPos = new ArrayList<Integer>();

					for (int i = 0; i < this.board.length; i++) {
						queenCurrent.add(currentNode.getQueenCurrent()[i]);
						queenNew.add(currentNode.getQueenNew()[i]);
						arrowPos.add(currentNode.getArrowPosition()[i]);
					}
					
					
					this.gamegui.updateGameState(queenCurrent, queenNew, arrowPos);
					
					// Schedule the timer task to run every 1000 milliseconds (1 second)
					

					}
					


					
				break;
    	case GameMessage.GAME_ACTION_MOVE:
				// GET OPPONENTS ACTION
				// oppositionMoveHandler(msgDetails);
				ArrayList<Integer> queenCurrent = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
				ArrayList<Integer> queenNew = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
				ArrayList<Integer> arrowPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);


				

				
				// CHECK IF THE ACTION IS LEGAL
				// IF SO, UPDATE THE GUI AND OUT LOCAL BOARD AND LET AI WORK
				this.gamegui.updateGameState(queenCurrent, queenNew, arrowPos);
				// OTHERWISE REPORT ILLEGAL MOVE
				this.getGameClient().sendMoveMessage(msgDetails);
    		break;
    	// default:
			// 	assert(false);
    	// 	break;  	
    	}
    	return true;
	}

	private void setMyQueen(String playingWhiteQueens) {
		if(this.userName.equals(playingWhiteQueens)){
			this.myQueen = 1;
		} else {
			this.myQueen = 2;
		}
		return;
	}

	private void initGameBoard(ArrayList<Integer> board){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				this.board[i][j] = board.get(11 * (i+1) + (j+1));
			}
		}
		return;
	}

	private void displayGameBoard(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				System.out.print(this.board[i][j]);
			}
		}
		return;
	}

	@Override
	public void onLogin()
	{
		System.out.println("Congratualations!!! "
    			+ "I am called because the server indicated that the login is successfully");
    	System.out.println("The next step is to find a room and join it: "
    			+ "the gameClient instance created in my constructor knows how!"); 
    	this.userName = gameClient.getUserName();
    	List<Room> rooms = this.gameClient.getRoomList();
    	for(Room room: rooms) 
    	{
    		System.out.println(room);
    	}
    	this.gameClient.joinRoom(rooms.get(0).getName());
    	if(gamegui != null) 
    	{
    		gamegui.setRoomInformation(gameClient.getRoomList());
    	}
	}

	@Override
	public String userName()
	{
		return userName;
	}
	
	
	
}
