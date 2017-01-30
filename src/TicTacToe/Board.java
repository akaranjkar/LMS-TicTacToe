package TicTacToe;

public class Board {
	private char[] boardState = {};
	
	private void initializeBoard() {
		boardState = "012345678".toCharArray();
	}
	
	public char[] getBoardState() {
		return boardState;
	}
	
	public void setBoardState(String state) {
		boardState = state.toCharArray();
	}
	
	public void updateBoardState(int position, char symbol) {
		boardState[position] = symbol;
	}
	
	public void displayBoard() {
		System.out.println(boardState[0] + " " + boardState[1] + " " + boardState[2]);
		System.out.println(boardState[3] + " " + boardState[4] + " " + boardState[5]);
		System.out.println(boardState[6] + " " + boardState[7] + " " + boardState[8]);
	}
	
	public Board() {
		initializeBoard();
	}
	
//	public static void main(String[] args) {
//		Board myBoard = new Board();
//		myBoard.updateBoardState(4, 'X');
//		myBoard.updateBoardState(5, 'O');
//		
//		char[] s = myBoard.getBoardState();
//		
//		for (int i = 0; i < 9; i++) {
//			System.out.println(Character.getNumericValue(s[i]));
//		}
//	}
}
