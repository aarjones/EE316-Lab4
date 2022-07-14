# EE316-Lab4
## Aaron R. Jones, Cole Caruso, Josh Herendeen
This repository holds our project for EE316 (Computer Engineering Junior Lab) Lab 4 - Digital Hangman using FPGA.  The host computer program was developed using Java and JavaFX (utilizing CSS), and the FPGA used was the the Digilent Cora Z7-10, coded using VHDL.

The system described in this project was capable of playing a GUI-based Hangman game.  Inputs were taken by the FPGA via a PS/2 keyboard.  The FPGA would then decode the PS/2 scan code into an ASCII code, which was sent to the host computer using a UART connection.  The host computer handled the internal logic for the Hangman game, including:
- Selection of COM port, baud rate, and number of allowable bad guesses
- Incorrect/Correct Letter Detection
- Selection of a random "key" word, sourced from a comprehensive list of >80k English words
- Display of word-in-progress and bad letters, as well as gradual appearance of the "hanged man"
- Background play of (royalty-free) music

The host computer would continually update an I<sup>2</sup>C LCD connected with the FPGA with the current state of the game, including:
- Progress on the word being guessed
- Running score after each game
- Selection of a new game
