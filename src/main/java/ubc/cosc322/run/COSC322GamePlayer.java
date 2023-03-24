package ubc.cosc322.run;

import java.util.*;

import ubc.cosc322.actionutil.ActionFactory;
import ubc.cosc322.actionutil.Action;
import ubc.cosc322.mcts.MCTS_Manager;
import ubc.cosc322.mcts.Node;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

public class COSC322GamePlayer extends GamePlayer {

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;

	private String userName = null;
	private String passwd = null;

	private int myQueen = -1;

	private int[][] board = null;




	public static void main(String[] args) {
		COSC322GamePlayer player;
		String userName, passwd;
		if(args.length == 0)
		{

			Random random = new Random();
			userName = "MCTS_Team_13#" + random.nextInt(1000);
			passwd = "password";
			player = new COSC322GamePlayer(userName, passwd);


		}
		else player = new COSC322GamePlayer(args[0], args[1]);

		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}


	public COSC322GamePlayer(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;
		this.board = new int[10][10];

		// To make a GUI-based player, create an instance of BaseGameGUI
		// and implement the method getGameGUI() accordingly
		this.gamegui = new BaseGameGUI(this);
	}

	@Override
	public void connect() {
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
		// This method will be called by the GameClient when it receives a game-related
		// message
		// from the server.

		// For a detailed description of the message types and format,
		// see the method GamePlayer.handleGameMessage() in the game-client-api
		// document.
		//System.out.println("HELLO THIS IS YOUR MOM: "+messageType);
		System.out.println("Message Type: "+messageType);

		switch (messageType) {
			case GameMessage.GAME_STATE_BOARD:
				if (gamegui != null) {
					ArrayList<Integer> board = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE);
					this.board = initGameBoard();
					this.gamegui.setGameState(board);


				}
				break;

			case GameMessage.GAME_ACTION_START:


				@SuppressWarnings("unused")
				String playingWhiteQueens = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
				@SuppressWarnings("unused")
				String playingBlackQueens = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
				setMyQueen(playingBlackQueens);
				MCTS_Manager.setCurrentNode(new Node(this.board, 2, null, null, null, 0));
				MCTS_Manager.setThreads(4);

				if (myQueen == 2) {

					try
					{
						performRolloutsOnCurrentNodeFor30Seconds();
					}
					catch (InterruptedException e)
					{
						throw new RuntimeException(e);
					}
					makeADecision();
					printArray();
					updateGameBoardWithDecision();

					}
				break;

			case GameMessage.GAME_ACTION_MOVE:

				if (gamegui != null) {
					gamegui.updateGameState(msgDetails);

				}
				if(!isOpponentMoveValid(msgDetails)) {
					//this.getGameClient().sendMoveMessage(msgDetails);
					System.out.println("Opponent has made an invalid move");
				}
				else {

					if (MCTS_Manager.getNode().getTerminal() != 0) {
						System.out.println("Our AI has lost!");
						System.out.println("The winner is Queen" + MCTS_Manager.getNode().getTerminal());
						gameClient.leaveCurrentRoom();
						gameClient.logout();


					} else {
						try {
							System.out.println("AI will now make its move!");
							performRolloutsOnCurrentNodeFor30Seconds();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						makeADecision();
						printArray();
						updateGameBoardWithDecision();

					}
				}
				break;

			case AmazonsGameMessage.GAME_STATE_PLAYER_LOST:
				System.out.println("Our AI has won!");
				gameClient.leaveCurrentRoom();
				gameClient.logout();
		}
		return true;
	}

	private void setMyQueen(String playingBlackQueens) {
		if (this.userName.equals(playingBlackQueens)) {
			this.myQueen = 2;
		} else {
			this.myQueen = 1;
		}
	}

	private int[][] initGameBoard() {

		return new int[][]{
				{0, 0, 0, 2, 0, 0, 2, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
		};
	}


	@Override
	public void onLogin() {
		System.out.println("Congratualations!!! "
				+ "I am called because the server indicated that the login is successfully");
		System.out.println("The next step is to find a room and join it: "
				+ "the gameClient instance created in my constructor knows how!");
		this.userName = gameClient.getUserName();
		List<Room> rooms = this.gameClient.getRoomList();
		for (Room room : rooms) {
			System.out.println(room);
		}
		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}

		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Enter a room number");
			int roomIdx = scanner.nextInt();
			this.gameClient.joinRoom(rooms.get(roomIdx).getName());
		}




	}

	@Override
	public String userName() {
		return userName;
	}



	public void performRolloutsOnCurrentNodeFor30Seconds() throws InterruptedException {
		RolloutThread rolloutThread = new RolloutThread();
		rolloutThread.start();
		Thread.sleep(27000);
		rolloutThread.stopThread();
		System.out.println("AI has finished thinking");
	}

	public void makeADecision() {
		MCTS_Manager.makeMove();
		System.out.println("AI has made a decision");
	}

	public void updateGameBoardWithDecision() {
		ArrayList<Integer> queenCurrent = new ArrayList<Integer>();
		ArrayList<Integer> queenNew = new ArrayList<Integer>();
		ArrayList<Integer> arrowPos = new ArrayList<Integer>();


		queenCurrent.add(9 - MCTS_Manager.getNode().getQueenCurrent()[0] + 1);
		queenCurrent.add(MCTS_Manager.getNode().getQueenCurrent()[1] + 1);

		queenNew.add(9 - MCTS_Manager.getNode().getQueenNew()[0] + 1);
		queenNew.add(MCTS_Manager.getNode().getQueenNew()[1] + 1);

		arrowPos.add(9 - MCTS_Manager.getNode().getArrowPosition()[0] + 1);
		arrowPos.add(MCTS_Manager.getNode().getArrowPosition()[1] + 1);


		this.gamegui.updateGameState(queenCurrent, queenNew, arrowPos);
		this.getGameClient().sendMoveMessage(queenCurrent, queenNew, arrowPos);
		System.out.println("Game board has been updated with AI's decision");
	}

	public boolean isOpponentMoveValid(Map<String, Object> msgDetails) {
		return MCTS_Manager.isOpponentMoveValid(msgDetails);
	}

	public void printArray()
	{
		int[][] state = MCTS_Manager.getNode().getState();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(state[i][j]+" ");
			}
			System.out.println();
		}
	}

	class RolloutThread extends Thread{
		boolean running = true;
		public void run() {
			try {
				while(running)
					MCTS_Manager.doRollout();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		public void stopThread()
		{
			running = false;
		}
	}
}
