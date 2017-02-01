#!/bin/bash
if [[ -d out ]]; then
    echo "Found previous output directory. Cleaning..."
    rm -rf out
fi
mkdir -p out/TicTacToe
javac -d out/ src/TicTacToe/*.java
echo "Finished compiling. Run rungame.sh to play"
