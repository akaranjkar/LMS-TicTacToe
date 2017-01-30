package TicTacToe;

import java.io.IOException;

public class ComputerPlayer extends Player {
    Experience experience = new Experience();
    double[] weights;
    double leastValue = Double.NEGATIVE_INFINITY;

    public ComputerPlayer(char symbol) {
        super(symbol);
    }

    public void initialize(int mode) throws IOException {
        weights = experience.train(mode);
    }

    public int selectBestMove(char[] boardState) {
        // Check all available moves
        // Generate boardStates for the moves and calculate board value
        // Return move with the maximum board value
    }
}
