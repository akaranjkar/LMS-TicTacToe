package TicTacToe;

import java.util.ArrayList;
import java.util.List;

public class BoardGenerator {

    // Print a game
    private void printGame(List<char[]> game) {
        for (char[] boardState : game)
            System.out.print(String.valueOf(boardState) + " ");
        System.out.println();
    }

    private boolean winningBoard(char[] boardState, char playerSymbol) {
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

    private void place(List<char[]> game, char playerSymbol) {
        for (int i = 0; i < 9; i++) {
            char[] boardState = game.get(game.size() - 1);
            int valueAtPosition = Character.getNumericValue(boardState[i]);
            if ((valueAtPosition >= 0) && (valueAtPosition <= 8)) {
                char[] app = boardState.clone();
                app[i] = playerSymbol;
                if (!winningBoard(app, playerSymbol)) {
                    List<char[]> newGame = new ArrayList<>(game);
                    newGame.add(app);
                    playerSymbol = (playerSymbol == 'X') ? 'O' : 'X';
                    place(newGame, playerSymbol);
                } else {
                    List<char[]> newGame = new ArrayList<>(game);
                    newGame.add(app);
                    printGame(newGame);
                }
            }
        }
    }

    public static void main(String[] args) {
        BoardGenerator b = new BoardGenerator();
        List<char[]> g = new ArrayList<>();
        g.add("012345678".toCharArray());
        b.place(g, 'X');
    }
}
