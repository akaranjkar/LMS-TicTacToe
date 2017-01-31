package TicTacToe;

import java.io.IOException;

public class ComputerPlayer extends Player {
    private Experience experience = new Experience();
    private double[] weights;

    public ComputerPlayer(char symbol) {
        super(symbol);
    }

    public void initialize(int mode) throws IOException {
        weights = experience.train(mode);
    }

    private double vhead(char[] boardState) {
        double vhead = 0;
        int[] attributes = experience.learner.evaluateAttributes(boardState, symbol);
        for (int i = 0; i < experience.learner.numberOfFeatures; i++) {
            vhead += weights[i] * attributes[i];
        }
        return vhead;
    }

    public int selectBestMove(char[] boardState) {
        int position = -1;
        double maxScore = Double.NEGATIVE_INFINITY;
        // Check all available moves
        for (int i = 0; i < 9; i++) {
            if (experience.learner.openPosition(boardState[i])) {
                char[] tempBoardState = boardState.clone();
                tempBoardState[i] = symbol;
                double tempBoardValue = vhead(tempBoardState);
                if (tempBoardValue >= maxScore) {
                    maxScore = tempBoardValue;
                    position = i;
                }
            }
        }
        return position;
    }
}
