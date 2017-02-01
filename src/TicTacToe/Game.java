package TicTacToe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private ComputerPlayer player1;
    private Player player2;

    public Game() {
        player1 = new ComputerPlayer('X');
        player2 = new Player('O');
    }

    // Select mode to train the computer player
    private void selectMode() throws IOException {
        boolean validSelection = false;
        Scanner in = new Scanner(System.in);
        System.out.println("Select mode to test:");
        System.out.println("1. Teacher mode.");
        System.out.println("2. No teacher mode.");
        while (!validSelection) {
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(in.nextLine());
            if (choice == 1 || choice == 2) {
                validSelection = true;
                // Train and start
                player1.initialize(choice);
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Decide random first move
    public int initialTurn() {
        return (int) (Math.random() * 2 + 1);
    }

    // Display the board
    public void displayBoard(char[] boardState) {
        System.out.println();
        System.out.println(" " + boardState[0] + " | " + boardState[1] + " | " + boardState[2]);
        System.out.println("-----------");
        System.out.println(" " + boardState[3] + " | " + boardState[4] + " | " + boardState[5]);
        System.out.println("-----------");
        System.out.println(" " + boardState[6] + " | " + boardState[7] + " | " + boardState[8]);
        System.out.println();
    }

    // Function to make a move
    public boolean makeMove(char[] boardState, Player player, int position) {
        int valueAtPosition = Character.getNumericValue(boardState[position]);
        if ((valueAtPosition >= 0) && (valueAtPosition <= 8)) {
            boardState[position] = player.getSymbol();
            return true;
        } else {
            return false;
        }
    }

    // Function to check if a player won the game
    public boolean checkVictory(char[] boardState, Player player) {
        char playerSymbol = player.getSymbol();
        boolean flag = false;
        // Check columns
        for (int i = 0; i < 3; i++) {
            flag |= boardState[0 + i] == playerSymbol &&
                    boardState[3 + i] == playerSymbol &&
                    boardState[6 + i] == playerSymbol;
        }
        // Check rows
        for (int i = 0; i < 7; i = i + 3) {
            flag |= boardState[i] == playerSymbol &&
                    boardState[i + 1] == playerSymbol &&
                    boardState[i + 2] == playerSymbol;
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

    // Function to play the game
    public void playGame() {
        int position;
        int humanWins = 0;
        int computerWins = 0;
        int draws = 0;
        boolean playAgain = true;
        Scanner in = new Scanner(System.in);
        while (playAgain) {
            int moves = 0;
            int turn = initialTurn();
            boolean gameOver = false;
            char[] boardState = "012345678".toCharArray();
            while (moves < 9 && !gameOver) {
                if (turn == 1) {
                    // Computer plays
                    position = player1.selectBestMove(boardState);
                    if (position != -1)
                        makeMove(boardState, player1, position); // Need to throw exception if position is -1
                    moves++;
                    turn = (turn == 1) ? 2 : 1;
                    System.out.println(player1.getSymbol() + " played position " + position + ".");
                    if (checkVictory(boardState, player1)) {
                        gameOver = true;
                        computerWins++;
                        System.out.println(player1.getSymbol() + " wins.");
                        displayBoard(boardState);
                    }
                } else {
                    //Human plays
                    displayBoard(boardState);
                    System.out.print(player2.getSymbol() + "'s turn: ");
                    position = Integer.parseInt(in.nextLine());
                    if (position >= 0 && position <= 8) {
                        if (makeMove(boardState, player2, position)) {
                            moves++;
                            displayBoard(boardState);
                            turn = (turn == 2) ? 1 : 2;
                            if (checkVictory(boardState, player2)) {
                                gameOver = true;
                                humanWins++;
                                System.out.println(player2.getSymbol() + " wins.");
                                displayBoard(boardState);
                            }
                        } else {
                            System.out.println("Invalid move. That position already has a symbol. Try again.");
                        }
                    } else {
                        System.out.println("Invalid position. Position must be between 0 and 8. Try again.");
                    }
                }
            }
            if (!gameOver) {
                displayBoard(boardState);
                System.out.println("The game was a draw.");
                gameOver = true;
                draws++;
            }
            // Check for another game
            System.out.print("Do you want to play another game? (Y/N): ");
            String gameChoice = in.nextLine();
            if (!(gameChoice.equalsIgnoreCase("yes") || gameChoice.equalsIgnoreCase("y"))) {
                playAgain = false;
            }
        }
        // Display stats
        displayStats(computerWins, humanWins, draws);
    }

    // Function to display statistics of the games played
    private void displayStats(int computerWins, int humanWins, int draws) {
        int totalGames = computerWins + humanWins + draws;
        System.out.println("\nStatistics:");
        System.out.println("Games played: " + totalGames);
        System.out.println("Computer wins: " + computerWins);
        System.out.println("Human wins: " + humanWins);
        System.out.println("Draws: " + draws + "\n");
    }

    // Main function. Create game, select training mode and start
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.selectMode();
        game.playGame();
    }
}
