package TicTacToe;

import java.io.*;
import java.util.*;

public class Learner {

    protected int numberOfFeatures = 7;
    private double learningRate = 0.01;
    private double initialWeightValue = 0.1;
    private double weights[] = new double[numberOfFeatures + 1];

    private List<List<char[]>> trainingGames = new ArrayList<>();
    private List<char[]> playerBoardStates = new ArrayList<>();

    public double[] getWeights() {
        return weights;
    }

    // Initialize all the weights to 0.1
    private void initializeWeights() {
        for (int i = 0; i < numberOfFeatures + 1; i++)
            weights[i] = initialWeightValue;
    }

    public Learner() {
        initializeWeights();
    }

    // Read training data from the data file for teacher mode
    private void readTrainingData() throws IOException {
        String teacherModeFile = "data";
        FileInputStream fstream = new FileInputStream(System.getProperty("user.dir") + "/resource/" + teacherModeFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fstream));
        String line;
        int i = 0;

        while ((line = bufferedReader.readLine()) != null) {
            List<char[]> game = new ArrayList<>();
            String[] boardValues = line.trim().split(" ");
            for (String board : boardValues)
                game.add(board.toCharArray());
            trainingGames.add(game);
        }
        bufferedReader.close();
    }

    // No teacher mode
    public void noTeacherMode() {
        List<List<char[]>> noTeacherTrainingGames = new ArrayList<>();
        int numberOfGames = 10000;
        for (int i = 0; i < numberOfGames; i++) {
            List<char[]> game = new ArrayList<>();
            int moves = 0;
            int turn = initialTurn();
            boolean gameOver = false;
            char[] boardState = "012345678".toCharArray();
            while (moves < 9 && !gameOver) {
                if (turn == 1) {
                    // X's turn
                    boardState[randomMove(boardState)] = 'X';
                    char[] currentState = boardState.clone();
                    game.add(currentState);
                    moves++;
                    if (tripleAttribute(boardState, 'X') >= 1) // X won this game
                        gameOver = true;
                    else
                        turn = (turn == 1) ? 2 : 1;

                } else {
                    // O's turn
                    boardState[randomMove(boardState)] = 'O';
                    char[] currentState = boardState.clone();
                    game.add(currentState);
                    moves++;
                    if (tripleAttribute(boardState, 'O') >= 1) // O won this game
                        gameOver = true;
                    else
                        turn = (turn == 2) ? 1 : 2;
                }
            }
            noTeacherTrainingGames.add(game);
        }

        for (int i = 0; i < noTeacherTrainingGames.size(); i++) {
            learnFromGame(noTeacherTrainingGames.get(i));
        }
        displayWeights();
    }

    // Teacher mode
    public void teacherMode() throws IOException {
        readTrainingData();
        for (int i = 0; i < trainingGames.size(); i++) {
            learnFromGame(trainingGames.get(i));
        }
        displayWeights();
    }

    // Learn from a game
    private void learnFromGame(List<char[]> game) {
        playerBoardStates.clear();
        // Check if player won or lost
        int playerResult = tripleAttribute(game.get(game.size() - 1), 'X');
        int opponentResult = tripleAttribute(game.get(game.size() - 1), 'O');
        // Get all the board states for the player
        if (singleAttribute(game.get(0), 'X') == 1) {
            // Player started
            for (int i = 0; i < game.size(); i += 2) {
                playerBoardStates.add(game.get(i));
            }
        } else {
            // Opponent started
            for (int i = 1; i < game.size(); i += 2) {
                playerBoardStates.add(game.get(i));
            }
        }
        // Call LMS depending on the outcome of the game
        if (playerResult >= 1) {
            lms(playerBoardStates, 100);
        } else if (opponentResult >= 1) {
            lms(playerBoardStates, -100);
        } else {
            lms(playerBoardStates, 0);
        }
    }

    // Evaluate features for a single board state
    protected int[] evaluateAttributes(char[] boardState, char playerSymbol) {
        char opponentSymbol = playerSymbol == 'X' ? 'O' : 'X';
        int[] attributes = new int[numberOfFeatures + 1];
        attributes[0] = 1;
        // Feature 1: Number of single X's on the board with two other spots open in a line
        attributes[1] = singleAttribute(boardState, playerSymbol);
        // Feature 1: Number of single O's on the board with two other spots open in a line
        attributes[2] = singleAttribute(boardState, opponentSymbol);
        // Feature 1: Number of two X's in a line with the third spot open
        attributes[3] = doubleAttribute(boardState, playerSymbol);
        // Feature 1: Number of two O's in a line with the third spot open
        attributes[4] = doubleAttribute(boardState, opponentSymbol);
        // Feature 1: Number of three X's in a line. Victory!
        attributes[5] = tripleAttribute(boardState, playerSymbol);
        // Feature 1: Number of two O's in a line and X blocks the third spot (defensive move by X)
        attributes[6] = blockAttribute(boardState, playerSymbol);
        // Feature 1: Number of two X's in a line and X blocks the third spot (defensive move by O)
        attributes[7] = blockAttribute(boardState, opponentSymbol);

        return attributes;
    }

    // LMS function
    private void lms(List<char[]> playerBoardStates, double vfinal) {
        double vhead = 0;
        double vtrain = vfinal;
        for (int i = playerBoardStates.size() - 1; i >= 0; i--) {
            char[] boardState = playerBoardStates.get(i);
            int[] attributes = evaluateAttributes(boardState, 'X');
            // Calculate vhead
            for (int j = 0; j <= numberOfFeatures; j++)
                vhead += weights[j] * attributes[j];
            // LMS update
            for (int j = 0; j <= numberOfFeatures; j++) {
                weights[j] += learningRate * (vtrain - vhead) * attributes[j];
            }
            vtrain = vhead;
        }
    }

    // One symbol and two open in a line
    private int singleAttribute(char[] boardState, char playerSymbol) {
        int count = 0;
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (boardState[0 + i] == playerSymbol && openPosition(boardState[3 + i]) && openPosition(boardState[6 + i]))
                count++;
            if (openPosition(boardState[0 + i]) && boardState[3 + i] == playerSymbol && openPosition(boardState[6 + i]))
                count++;
            if (openPosition(boardState[0 + i]) && openPosition(boardState[3 + i]) && boardState[6 + i] == playerSymbol)
                count++;
        }
        // Check rows
        for (int i = 0; i < 7; i = i + 3) {
            if (boardState[i] == playerSymbol && openPosition(boardState[i + 1]) && openPosition(boardState[i + 2]))
                count++;
            if (openPosition(boardState[i]) && boardState[i + 1] == playerSymbol && openPosition(boardState[i + 2]))
                count++;
            if (openPosition(boardState[i]) && openPosition(boardState[i + 1]) && boardState[i + 2] == playerSymbol)
                count++;
        }
        // Left diagonal
        if (boardState[0] == playerSymbol && openPosition(boardState[4]) && openPosition(boardState[8])) count++;
        if (openPosition(boardState[0]) && boardState[4] == playerSymbol && openPosition(boardState[8])) count++;
        if (openPosition(boardState[0]) && openPosition(boardState[4]) && boardState[8] == playerSymbol) count++;
        // Right diagonal
        if (boardState[2] == playerSymbol && openPosition(boardState[4]) && openPosition(boardState[6])) count++;
        if (openPosition(boardState[2]) && boardState[4] == playerSymbol && openPosition(boardState[6])) count++;
        if (openPosition(boardState[2]) && openPosition(boardState[4]) && boardState[6] == playerSymbol) count++;

        return count;
    }

    // Two symbols in a line with the third spot open
    private int doubleAttribute(char[] boardState, char playerSymbol) {
        int count = 0;
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (boardState[0 + i] == playerSymbol && boardState[3 + i] == playerSymbol && openPosition(boardState[6 + i]))
                count++;
            if (boardState[0 + i] == playerSymbol && openPosition(boardState[3 + i]) && boardState[6 + i] == playerSymbol)
                count++;
            if (openPosition(boardState[0 + i]) && boardState[3 + i] == playerSymbol && boardState[6 + i] == playerSymbol)
                count++;
        }
        // Check rows
        for (int i = 0; i < 7; i = i + 3) {
            if (boardState[i] == playerSymbol && boardState[i + 1] == playerSymbol && openPosition(boardState[i + 2]))
                count++;
            if (boardState[i] == playerSymbol && openPosition(boardState[i + 1]) && boardState[i + 2] == playerSymbol)
                count++;
            if (openPosition(boardState[i]) && boardState[i + 1] == playerSymbol && boardState[i + 2] == playerSymbol)
                count++;
        }
        // Left diagonal
        if (boardState[0] == playerSymbol && boardState[4] == playerSymbol && openPosition(boardState[8])) count++;
        if (boardState[0] == playerSymbol && openPosition(boardState[4]) && boardState[8] == playerSymbol) count++;
        if (openPosition(boardState[0]) && boardState[4] == playerSymbol && boardState[8] == playerSymbol) count++;
        // Right diagonal
        if (boardState[2] == playerSymbol && boardState[4] == playerSymbol && openPosition(boardState[6])) count++;
        if (boardState[2] == playerSymbol && openPosition(boardState[4]) && boardState[6] == playerSymbol) count++;
        if (openPosition(boardState[2]) && boardState[4] == playerSymbol && boardState[6] == playerSymbol) count++;

        return count;
    }

    // Three symbols in a line
    private int tripleAttribute(char[] boardState, char playerSymbol) {
        int count = 0;
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (boardState[0 + i] == playerSymbol && boardState[3 + i] == playerSymbol && boardState[6 + i] == playerSymbol)
                count++;
        }
        // Check rows
        for (int i = 0; i < 7; i = i + 3) {
            if (boardState[i] == playerSymbol && boardState[i + 1] == playerSymbol && boardState[i + 2] == playerSymbol)
                count++;
        }
        // Left diagonal
        if (boardState[0] == playerSymbol && boardState[4] == playerSymbol && boardState[8] == playerSymbol) count++;
        // Right diagonal
        if (boardState[2] == playerSymbol && boardState[4] == playerSymbol && boardState[6] == playerSymbol) count++;

        return count;
    }

    // Two opponent symbols in a line and player blocks third spot
    private int blockAttribute(char[] boardState, char playerSymbol) {
        char opponentSymbol = (playerSymbol == 'X') ? 'O' : 'X';
        int count = 0;
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (boardState[0 + i] == playerSymbol && boardState[3 + i] == opponentSymbol && boardState[6 + i] == opponentSymbol)
                count++;
            if (boardState[0 + i] == opponentSymbol && boardState[3 + i] == playerSymbol && boardState[6 + i] == opponentSymbol)
                count++;
            if (boardState[0 + i] == opponentSymbol && boardState[3 + i] == opponentSymbol && boardState[6 + i] == playerSymbol)
                count++;
        }
        // Check rows
        for (int i = 0; i < 7; i = i + 3) {
            if (boardState[i] == playerSymbol && boardState[i + 1] == opponentSymbol && boardState[i + 2] == opponentSymbol)
                count++;
            if (boardState[i] == opponentSymbol && boardState[i + 1] == playerSymbol && boardState[i + 2] == opponentSymbol)
                count++;
            if (boardState[i] == opponentSymbol && boardState[i + 1] == opponentSymbol && boardState[i + 2] == playerSymbol)
                count++;
        }
        // Left diagonal
        if (boardState[0] == playerSymbol && boardState[4] == opponentSymbol && boardState[8] == opponentSymbol)
            count++;
        if (boardState[0] == opponentSymbol && boardState[4] == playerSymbol && boardState[8] == opponentSymbol)
            count++;
        if (boardState[0] == opponentSymbol && boardState[4] == opponentSymbol && boardState[8] == playerSymbol)
            count++;
        // Right diagonal
        if (boardState[2] == playerSymbol && boardState[4] == opponentSymbol && boardState[6] == opponentSymbol)
            count++;
        if (boardState[2] == opponentSymbol && boardState[4] == playerSymbol && boardState[6] == opponentSymbol)
            count++;
        if (boardState[2] == opponentSymbol && boardState[4] == opponentSymbol && boardState[6] == playerSymbol)
            count++;

        return count;
    }

    // Helper functions
    // Choose random player for first move
    private int initialTurn() {
        return (int) (Math.random() * 2 + 1);
    }

    // Choose a random move
    private int randomMove(char[] boardState) {
        int randomPosition;
        boolean validMove = false;
        do {
            randomPosition = (int) (Math.random() * 9);
            if (openPosition(boardState[randomPosition]))
                validMove = true;
        } while (!validMove);
        return randomPosition;
    }

    // Check if a position is open
    protected boolean openPosition(char boardPosition) {
        int valueAtPosition = Character.getNumericValue(boardPosition);
        return ((valueAtPosition >= 0) && (valueAtPosition <= 8));
    }

    // Print a game
    private void printGame(List<char[]> game) {
        for (char[] boardState : game)
            System.out.print(String.valueOf(boardState) + " ");
        System.out.println();
    }

    // Print all training games from a training session
    private void printAllTrainingGames(List<List<char[]>> trainingGames) {
        for (int i = 0; i < trainingGames.size(); i++) {
            printGame(trainingGames.get(i));
        }
    }

    // Print weight values
    private void displayWeights() {
        for (int i = 0; i < numberOfFeatures + 1; i++) {
            System.out.print("w" + i + ": " + weights[i] + " ");
        }
        System.out.println();
    }
}
