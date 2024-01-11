# Project Title: Mancala/Ayo Game

A simple and fun implementation of the classic board games Mancala and Ayo.

## Description

This project is a Java-based implementation of two traditional board games: Mancala and Ayo. 
It allows users to play these strategy games either against another player or the computer. 
The games are known for their simple rules but deep strategic elements, making them enjoyable 
for players of all ages.

Kalah (Mancala) Rules - Quick Guide
Setup: Each player's pits are filled with a fixed number of stones.
Play: Players take turns picking up all the stones from one of their pits.
Sowing: Moving counter-clockwise, one stone is placed into each subsequent pit.
Skipping Stores: Skip your opponent’s store while sowing.
Free Turn: If your last stone lands in your store, you get another turn.
Capture: If your last stone lands in an empty pit on your side and the opposite pit has stones, capture all stones from both pits and put them in your store.
Game End: The game ends when all pits on one side are empty.
Winning: The player with the most stones in their store wins.

Ayoayo Rules - Quick Guide
Setup: Begin with a set number of stones in each pit.
Excluding Starting Pit: When sowing from a pit, the starting pit is skipped.
Sowing: Distribute stones counter-clockwise, one in each pit.
Multi-Lap Play: If the last stone lands in a non-empty pit, pick up all stones from that pit and continue sowing.
Capture: Capture occurs if the last stone lands in an empty pit on your side and the opposite pit contains stones; capture all stones from the opposite pit.
Ending the Game: The game ends when one player's side is empty.
Winning: The player with the most stones in their store at the end of the game wins.


## Getting Started

### Dependencies

Java Runtime Environment (JRE) 11 or later.
Any modern OS (Windows, macOS, Linux)


### Executing program

- Compile the Java Source Files:
    - gradle build

- Run the GUI (compiled file class)
    -  java -jar TextUI.jar

- Expected Output:
    - A Screen that says 'Welcome to Mancala/Ayo Game'
    - Two Buttons that allow you to choose between playing mancala and Ayo


## Limitations

Graphical User Interface (GUI) Issues:

The project currently lacks a fully functional GUI, although players are still able to interact with the game it does
not fully work.

There are one known failed JUnit tests. The failure indicates potential bugs / logical errors in the game's rules or mechanics. 
The game might not always behave as expected, particularly in edge-case scenarios or complex game scenarios.

## Author Information

Name: Hafsa Jama
Student #: 1232742
Email: jamah@uoguelph.ca

## Development History
November 20 
    - Part 1 - Refactoring 
November 21
    - Part 2 of the Assignment
November 22 - 28 
    - Writing the Kalah / Ayo Classes
    - Debugging
    - Fixing game logic because of failed Junit tests
    - GUI
    - Additional Testing
November 29 - 30
    - GUI
    - Additional Testing
    - Debugging Errors 

## Acknowledgments
    - Used class notes and tutorials to help complete the assignment