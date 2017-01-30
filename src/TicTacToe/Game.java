package TicTacToe;

import java.util.Scanner;

public class Game {
	Board board;
	Player player1, player2;
	
	public Game() {
		board = new Board();
		player1 = new ComputerPlayer('X');
		player2 = new Player('O');
	}

	private void selectMode() {
	    boolean validSelection = false;
	    Scanner in = new Scanner(System.in);
	    System.out.println("Select mode to test:");
        System.out.println("1. Teacher mode.");
        System.out.println("2. No teacher mode.");
        while (!validSelection) {
            int choice = in.nextInt();
            if (choice == 1) {
                validSelection = true;
                // Train in teacher mode and start
            } else if (choice == 2) {
                // Train in no teacher mode and start
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }
	
	public int initialTurn() {
		return (int) (Math.random() * 2 + 1);
	}

	public void displayBoard() {
		board.displayBoard();
	}

	// Function to make a move
	public boolean makeMove(Player player, int position) {
		int valueAtPosition = Character.getNumericValue(board.getBoardState()[position]);
		if ((valueAtPosition >= 0) && (valueAtPosition <= 8)) {
			board.updateBoardState(position, player.getSymbol());
			return true;
		} else {
			return false;
		}
	}
	
	// Function to check if a player won the game
	public boolean checkVictory(Player player) {
		char playerSymbol = player.getSymbol();
		char[] boardState = board.getBoardState();
		boolean flag = false;
		// Check columns
		for (int i = 0; i < 3; i++) {
			flag |= boardState[0+i] == playerSymbol &&
					boardState[3+i] == playerSymbol &&
					boardState[6+i] == playerSymbol;
		}
		// Check rows
		for (int i=0; i < 7; i = i + 3) {
			flag |= boardState[i] == playerSymbol &&
					boardState[i+1] == playerSymbol &&
					boardState[i+2] == playerSymbol;
		}
		// Check diagonals
		flag |= boardState[0] == playerSymbol &&
				boardState[4] == playerSymbol &&
				boardState[8] == playerSymbol;
		
		flag |= boardState[2] == playerSymbol &&
				boardState[4] == playerSymbol &&
				boardState[6] == playerSymbol;
		
		return flag;
	}
	
	public void playGame() {
		int moves = 0;
		int turn = initialTurn();
		int position;
		boolean gameOver = false;
		Scanner in = new Scanner(System.in);
		Player currentPlayer;
		while (moves < 9 && !gameOver) {
			currentPlayer = (turn == 1) ? player1 : player2;
			board.displayBoard();
			System.out.println(currentPlayer.getSymbol() + "'s turn: ");
			position = in.nextInt();
			if (position >= 0 || position <=8) {
				if (makeMove(currentPlayer, position)) {
					moves++;
					turn = (turn == 1) ? 2 : 1;
					if (checkVictory(currentPlayer)) {
						gameOver = true;
						board.displayBoard();
						System.out.println(currentPlayer.getSymbol() + " wins.");
					}
				} else {
					System.out.println("Invalid move. That position already has a symbol. Try again.");
					continue;
				}
			} else {
				System.out.println("Invalid position. Position must be between 0 and 8. Try again.");
			}
		}
		if (!gameOver) {
			board.displayBoard();
			System.out.println("The game was a draw.");
			gameOver = true;
		}
	}
	
	public static void main(String[] args){
	}
}
