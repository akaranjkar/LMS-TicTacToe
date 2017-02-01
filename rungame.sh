#!/bin/bash
function missing_message {
    echo "Class files seem to be missing. Please run compile.sh and try again"
}

if [[ -d out/TicTacToe ]]; then
    if [[ -f out/TicTacToe/ComputerPlayer.class && -f out/TicTacToe/Experience.class && -f out/TicTacToe/Game.class && -f out/TicTacToe/Learner.class && -f out/TicTacToe/Player.class ]]; then
        java -cp out/ TicTacToe.Game
    else
        missing_message
    fi
else
    missing_messag
fi
