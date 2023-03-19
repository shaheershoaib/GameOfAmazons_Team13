package ubc.cosc322;

import java.util.*;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

public class COSC322Test extends GamePlayer {

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;

	private String userName = null;
	private String passwd = null;

	private final int whiteQueen = 2;
	private final int blackQueen = 1;
	private final int ARROW = 7;
	private int myQueen = -1;

	private int[][] board = null;
	static Node currentNode = null;

	private ArrayList<Integer> opponentOriginalQueenPosition, opponentNewQueenPosition, opponentArrowPosition;
	private ArrayList<Action> allPossibleActionsFromCurrentNode;
	private Action opponentAction;



	public static void main(String[] args) {
		COSC322Test player;
		String userName, passwd;
		if(args.length == 0)
		{

			Random random = new Random();
			userName = "MCTS_Team_13#" + random.nextInt(1000);
			passwd = "password";
			player = new COSC322Test(userName, passwd);


		}
		else player = new COSC322Test(args[0], args[1]);

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


	public COSC322Test(String userName, String passwd) {
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
				assert (!playingWhiteQueens.equals(playingBlackQueens));
				setMyQueen(playingWhiteQueens);
				currentNode = new Node(this.board, myQueen, null, null, null, 0, 1);

				if (myQueen == 1) {
					performRolloutsOnCurrentNodeFor30Seconds();
					makeADecision();
					updateGameBoardWithDecision();
				}
				break;

			case GameMessage.GAME_ACTION_MOVE:
				getOpponentMove(msgDetails);
				findAllPossibleActionsFromPreviousBoard();
				if (opponentActionValid())
					updateOurCurrentNodeToMatchOpponentAction();
				else
					this.getGameClient().sendMoveMessage(msgDetails);
				performRolloutsOnCurrentNodeFor30Seconds();
				makeADecision();
				updateGameBoardWithDecision();
				break;
		}
		return true;
	}

	private void setMyQueen(String playingWhiteQueens) {
		if (this.userName.equals(playingWhiteQueens)) {
			this.myQueen = 1;
		} else {
			this.myQueen = 2;
		}
	}

	private int[][] initGameBoard() {
		int[][] state= new int[][]{
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

		return state;
	}

	private void displayGameBoard() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(this.board[i][j]);
			}
			System.out.println();
		}

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
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Enter a room number");
			int roomIdx = scanner.nextInt();
			this.gameClient.joinRoom(rooms.get(roomIdx).getName());
		}

		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
	}

	@Override
	public String userName() {
		return userName;
	}

	static class RunRollouts extends TimerTask {
		@Override
		public void run() {
			while (true) {
				try
				{
					currentNode.doRollout();
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
	}

	public void performRolloutsOnCurrentNodeFor30Seconds() {
		Timer timer = new Timer();
		RunRollouts timedTask = new RunRollouts();
		timer.schedule(timedTask, 0, 1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timer.cancel();
	}

	public void makeADecision() {
		currentNode = currentNode.children.poll();
	}

	public void updateGameBoardWithDecision() {
		ArrayList<Integer> queenCurrent = new ArrayList<Integer>();
		ArrayList<Integer> queenNew = new ArrayList<Integer>();
		ArrayList<Integer> arrowPos = new ArrayList<Integer>();

		for (int i = 0; i < 2; i++) {
			queenCurrent.add(currentNode.getQueenCurrent()[i]);
			queenNew.add(currentNode.getQueenNew()[i]);
			arrowPos.add(currentNode.getArrowPosition()[i]);
		}

		this.gamegui.updateGameState(queenCurrent, queenNew, arrowPos);
	}

	public void getOpponentMove(Map<String, Object> msgDetails) {
		this.opponentOriginalQueenPosition = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
		this.opponentNewQueenPosition = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
		this.opponentArrowPosition = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
	}

	public void findAllPossibleActionsFromPreviousBoard() {
		ActionFactory nodeActionFactory = new ActionFactory(currentNode.getState(), currentNode.getPlayerType());
		allPossibleActionsFromCurrentNode = nodeActionFactory.getActions();

	}

	public boolean opponentActionValid() {
		for (Action action : allPossibleActionsFromCurrentNode) {
			if ((action.getQueenPositionCurrent()[0] == opponentOriginalQueenPosition.get(0)
					&& action.getQueenPositionCurrent()[1] == opponentOriginalQueenPosition.get(1)
					&& action.getQueenPositionNew()[0] == opponentNewQueenPosition.get(0)
					&& action.getQueenPositionNew()[1] == opponentNewQueenPosition.get(1)
					&& action.getArrowPosition()[0] == opponentArrowPosition.get(0)
					&& action.getArrowPosition()[1] == opponentArrowPosition.get(1))) {
				opponentAction = action;
				return true;
			}

		}

		return false;
	}

	public void updateOurCurrentNodeToMatchOpponentAction() {
		if (opponentAction != null) {

			if (currentNode.currentChildren.containsKey(opponentAction.getId())) {
				Node child = currentNode.currentChildren.get(opponentAction.getId());
				ActionFactory actionFactoryOfChild = new ActionFactory(child.getState(), child.getPlayerType());
				ArrayList<Action> childActions = actionFactoryOfChild.getActions();
				generatePriorityQueueForNode(childActions, child);

			} else {
				currentNode = createNewNodeFromStateUsingAction(currentNode.getState(), getPlayerTypeOfChildren(currentNode.getPlayerType()), opponentAction);
			}
		}
	}

	public Node createNewNodeFromStateUsingAction(int[][] state, int playerTypeOfAction, Action action) {

		int[][] newState = cloneState(state);

		newState = getNewStateUsingAction(newState, playerTypeOfAction, action);
		return new Node(newState, playerTypeOfAction, action.getQueenPositionCurrent(), action.getQueenPositionNew(), action.getArrowPosition(), action.getId(), 1);
	}

	public void createChildNode(Node parentNode, Action action) {
		int[][] childState = cloneState(parentNode.getState());
		int childPlayerType = getPlayerTypeOfChildren(parentNode.getPlayerType());

		getNewStateUsingAction(childState, parentNode.getPlayerType(), action);

		parentNode.children.add(new Node(childState, childPlayerType, action.getQueenPositionCurrent(), action.getQueenPositionNew(), action.getArrowPosition(), action.getId()));

	}

	public int[][] cloneState(int[][] state)
	{
		int[][] childState = new int[10][10];
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
				childState[row][col] = state[row][col];
		return childState;
	}

	public int[][] getNewStateUsingAction(int[][] state, int playerType, Action action)
	  {                        
		  state[action.getQueenPositionCurrent()[0]][action.getQueenPositionCurrent()[1]] = 0;
		  state[action.getQueenPositionNew()[0]][action.getQueenPositionNew()[1]] = playerType;
		  state[action.getArrowPosition()[0]][action.getArrowPosition()[1]] = ARROW;
  
		  return state;
	  }

	public void generatePriorityQueueForNode(ArrayList<Action> actions, Node node) {
		for (Action action : actions) {
			Node child = currentNode.currentChildren.get(action.getId());
			if (child != null)
				node.children.add(child);
			else {
				createChildNode(node, action);
			}
		}
	}

	public int getPlayerTypeOfChildren(int playerType) {
		if (playerType == 1)
			return 2;
		else
			return 1;

	}
}
