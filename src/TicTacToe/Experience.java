package TicTacToe;

import java.io.IOException;

public class Experience {
    protected Learner learner = new Learner();

    // Select type of experience
    public double[] train(int mode) throws IOException {
        if (mode == 1) {
            learner.teacherMode();
        } else if (mode == 2) {
            learner.noTeacherMode();
        }
        return learner.getWeights();
    }

}
